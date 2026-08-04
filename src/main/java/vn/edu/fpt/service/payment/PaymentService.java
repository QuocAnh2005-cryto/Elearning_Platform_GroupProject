package vn.edu.fpt.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.cart.CartPageDetailsDto;
import vn.edu.fpt.dto.transaction_manager.CourseDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionCountByStatusDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionDetailDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionListDTO;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.enums.OrderStatus;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.exception.BadRequestException;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.repository.PaymentRepository;
import vn.edu.fpt.service.CartService;
import vn.edu.fpt.service.OrderService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.PaymentLink;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final CartService cartService;
    private final PayOsService payOsService;
    private final OrderService orderService;
    private final PayOS payOS;


    public List<Payment> findAll() {
        return repository.findAll();
    }

    public Optional<Payment> findById(Integer id) {
        return repository.findById(id);
    }

    public Payment save(Payment entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Payment checkout() {
        User user = SecurityUtils.getCurrentUser();

        Cart cart = cartService.getOrCreateCartForUser(user);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Giỏ hàng trống");
        }

        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        if (selectedItems.isEmpty()) {
            throw new BadRequestException("Vui lòng chọn ít nhất một khóa học để thanh toán");
        }

        CartPageDetailsDto cartDetails = cartService.getCartPageDetails(user);
        if (cartDetails.getTotal() > 0 && cartDetails.getTotal() < 2000) {
            throw new BadRequestException("Tổng giá trị đơn hàng phải từ 2,000 VNĐ trở lên để thực hiện thanh toán!");
        }


        List<Order> orderByUser = orderService.findOrderByUser(user);
        Order orderContinue = null;
        Set<Integer> courseIdSelectedInCart = selectedItems.stream().map(cartItem -> {
            return cartItem.getCourse().getId();
        }).collect(Collectors.toSet());
        for (Order order : orderByUser) {
            if (order.getItems().size() != selectedItems.size()) {
                continue;
            }
            Set<Integer> courseIdInOrderItem = order.getItems().stream().map(orderItem -> {
                return orderItem.getCourse().getId();
            }).collect(Collectors.toSet());
            if (courseIdInOrderItem.containsAll(courseIdSelectedInCart)) {
                orderContinue = order;
                break;
            }
        }

        if (orderContinue != null && orderContinue.getPayment().getExpiredAt().isAfter(LocalDateTime.now())) {
            Payment payment = orderContinue.getPayment();
            log.info("Tiếp tục lấy đơn hàng đang ở trạng thái pending với ID: {}", payment.getId());
            return payment;
        }


        BigDecimal totalAmount = BigDecimal.valueOf(cartDetails.getTotal());

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .paymentMethod(AppConstants.PAYMENT_GATEWAY)
                .build();

        for (CartItem item : selectedItems) {
            Course course = item.getCourse();
            long coursePrice = course.getPrice().longValue();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .course(course)
                    .priceSnapshot(BigDecimal.valueOf(coursePrice))
                    .courseTitleSnapshot(course.getTitle())
                    .build();

            order.addItem(orderItem);
        }

        order = orderService.save(order);

        if (cartDetails.getTotal() == 0) {
            Payment payment = Payment.builder()
                    .order(order)
                    .gateway("FREE")
                    .amount(BigDecimal.ZERO)
                    .status(PaymentStatus.PENDING)
                    .gatewayOrderCode(String.valueOf(System.currentTimeMillis() / 1000))
                    .paymentUrl("")
                    .qrCodeUrl("")
                    .accountNumber("")
                    .description("Free Enrollment")
                    .bankName("")
                    .accountHolder("")
                    .expiredAt(LocalDateTime.now().plusMinutes(1))
                    .build();
            Payment savedPayment = repository.save(payment);
            payOsService.completePayment(savedPayment);
            return savedPayment;
        }

        Payment payment = payOsService.createPaymentOrder(order, AppConstants.RETURN_URL, AppConstants.CANCEL_URL);

        log.info("Khởi tạo thanh toán thành công cho Payment ID: {}", payment.getId());
        return payment;
    }

    public Payment getPaymentStatus(Integer paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin thanh toán"));
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new BadRequestException("Vui lòng đăng nhập để kiểm tra trạng thái thanh toán!");
        }
        if (payment.getOrder() != null && payment.getOrder().getUser() != null) {
            boolean isOwner = payment.getOrder().getUser().getId().equals(currentUser.getId());
            boolean isStaff = currentUser.getRoles().stream().anyMatch((r) -> {
                        return r.getName().equals("ROLE_ADMIN") || r.getName().equals("ROLE_MANAGER");
                    }
            );
            if (!isOwner && !isStaff) {
                throw new BadRequestException("Bạn không có quyền truy cập thông tin thanh toán này!");
            }
        }
        if (payment.getStatus() == PaymentStatus.PENDING) {
            syncStatusFromPayOs(payment);
        }

        return payment;
    }

    private void syncStatusFromPayOs(Payment payment) {
        try {
            long orderCode = Long.parseLong(payment.getGatewayOrderCode());
            PaymentLink info = payOS.paymentRequests().get(orderCode);
            log.info("Kiểm tra trực tiếp trạng thái trên PayOS cho mã đơn hàng {}: {}", orderCode, info.getStatus());
            String payOsStatus = (info.getStatus() != null) ? info.getStatus().toString() : "";
            if ("PAID".equalsIgnoreCase(payOsStatus)) {
                payOsService.completePayment(payment);
            } else if ("CANCELLED".equalsIgnoreCase(payOsStatus)) {
                payOsService.cancelPayment(payment);
            } else if ("EXPIRED".equalsIgnoreCase(payOsStatus)) {
                payOsService.expirePayment(payment);
            }
        } catch (Exception e) {
            log.warn("Không thể truy vấn trạng thái từ PayOS cho Payment ID: {}, Lỗi: {}", payment.getId(), e.getMessage());
        }
    }

    public void cancelPaymentManually(Integer paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin thanh toán."));
        User currentUser = SecurityUtils.getCurrentUser();
        User orderOwner = payment.getOrder().getUser();
        if (currentUser == null || !orderOwner.getId().equals(currentUser.getId())) {
            throw new BadRequestException("Không có quyền thực hiện hành động này.");
        }
        payOsService.cancelPaymentAndInvalidatePayOs(payment);
    }

    public Integer getNumberAllPayment() {
        return repository.getNumberAllPayment();
    }

    public Integer getNumberAllPaymentWithStatus(PaymentStatus paymentStatus) {
        return repository.getNumberAllPaymentWithStatus(paymentStatus);
    }

    public TransactionCountByStatusDTO gettransactionCountByStatusDTO() {
        List<Object[]> result = repository.gettransactionCountByStatusDTO();
        TransactionCountByStatusDTO dto = new TransactionCountByStatusDTO(0, 0, 0, 0, 0);
        for (Object[] o : result) {
            PaymentStatus paymentStatus = (PaymentStatus) o[0];
            Long count = (Long) o[1];
            if (paymentStatus == PaymentStatus.PENDING) {
                dto.setNumberOfTransactionPending(count.intValue());
            } else if (paymentStatus == PaymentStatus.PAID) {
                dto.setNumberOfTransactionSuccess(count.intValue());
            } else if (paymentStatus == PaymentStatus.CANCELLED) {
                dto.setNumberOfTransactionCanceled(count.intValue());
            } else if (paymentStatus == PaymentStatus.EXPIRED) {
                dto.setNumberOfTransactionExpired(count.intValue());
            } else if (paymentStatus == PaymentStatus.FAILED) {
                dto.setNumberOfTransactionFailed(count.intValue());
            }
        }
        return dto;
    }

    public Page<TransactionListDTO> getTransactionByFilter(String statuss, LocalDateTime fromDate, LocalDateTime toDate, String keyword, int page) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        PaymentStatus status = null;
        if (statuss != null && !statuss.isBlank()) {
            status = PaymentStatus.valueOf(statuss);
        }
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_PAYMENT_RECORD_PER_PAGE);
        return repository.getTransactionByFilter(status, fromDate, toDate, keyword, pageable);
    }

    public TransactionDetailDTO getTransactionDetailByPaymentId(Integer paymentId) {
        TransactionDetailDTO transactionDetailDTO = repository.getTransactionDetailByPaymentId(paymentId);
        List<CourseDTO> courseDTOS = repository.getListItemByPaymentId(paymentId);
        for (CourseDTO courseDTO : courseDTOS) {
            courseDTO.setThumbnailUrl(AppConstants.AZURE_STORAGE_BASE_URL + "/" + AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/" + courseDTO.getThumbnailUrl());
        }
        transactionDetailDTO.setCourses(courseDTOS);
        return transactionDetailDTO;
    }

    public Map<String, Object> getPaymentPageData(Integer paymentId) {
        Map<String, Object> data = new HashMap<>();
        User currentUser = SecurityUtils.getCurrentUser();
        data.put("currentUser", currentUser);

        if (paymentId != null) {
            Payment payment = repository.findById(paymentId).orElse(null);
            if (payment != null) {
                data.put("payment", payment);
                Order order = payment.getOrder();
                data.put("order", order);
                if (order != null && order.getItems() != null) {
                    Map<User, List<OrderItem>> itemsByInstructor = new HashMap<>();
                    for (OrderItem orderItem : order.getItems()) {
                        User instructor = orderItem.getCourse().getInstructor();
                        List<OrderItem> items = itemsByInstructor.get(instructor);
                        if (items == null) {
                            items = new ArrayList<>();
                            itemsByInstructor.put(instructor, items);
                        }
                        items.add(orderItem);
                    }
                    data.put("itemsByInstructor", itemsByInstructor);
                }
                data.put("qrCode", AppConstants.QR_CODE_BASE_URL + payment.getQrCodeUrl());
                data.put("payOsAccountNumber", payment.getAccountNumber());
                data.put("payOsDescription", payment.getDescription());
                data.put("payOsBankName", payment.getBankName());
                data.put("payOsAccountHolder", payment.getAccountHolder());
            }
        }
        return data;
    }
}
