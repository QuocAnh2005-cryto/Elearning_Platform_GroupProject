package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.cart.CartDto;
import vn.edu.fpt.dto.cart.CartItemDto;
import vn.edu.fpt.dto.cart.CartPageDetailsDto;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.OrderStatus;
import vn.edu.fpt.repository.*;
import vn.edu.fpt.mapper.DtoMapper;

import java.util.*;

@Service
@Transactional
public class CartService {
    private final CartRepository repository;
    private final CartItemService cartItemService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final DtoMapper dtoMapper;
    private final OrderRepository orderRepository;

    public CartService(CartRepository cartRepository,
                       CartItemService cartItemService,
                       CourseRepository courseRepository,
                       UserRepository userRepository,
                       EnrollmentRepository enrollmentRepository,
                       DtoMapper dtoMapper,
                       OrderRepository orderRepository) {
        this.repository = cartRepository;
        this.cartItemService = cartItemService;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.dtoMapper = dtoMapper;
        this.orderRepository = orderRepository;
    }

    public Cart getOrCreateCartForUser(User user) {
        return repository.findByUser(user)
                .orElseGet(() -> repository.save(Cart.builder().user(user).build()));
    }

    public Cart getOrCreateCartForUserWithDetails(User user) {
        return repository.findByUserWithItemsAndCourses(user)
                .orElseGet(() -> repository.save(Cart.builder().user(user).build()));
    }

    public List<Cart> findAll() {
        return repository.findAll();
    }

    public Optional<Cart> findById(Integer id) {
        return repository.findById(id);
    }

    public Cart save(Cart entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public CartPageDetailsDto getCartPageDetails(User user) {
        Cart cart = getOrCreateCartForUserWithDetails(user);
        
        // Auto-remove owned courses
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            List<CartItem> toRemove = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                if (item.getCourse() != null && enrollmentRepository.existsByUserAndCourse(user, item.getCourse())) {
                    toRemove.add(item);
                }
            }
            if (!toRemove.isEmpty()) {
                for (CartItem item : toRemove) {
                    cartItemService.deleteById(item.getId());
                    cart.getItems().remove(item);
                }
            }
        }

        CartDto cartDto = dtoMapper.toCartDto(cart);

        // Nhóm các CartItemDto theo Giảng viên của khóa học
        Map<UserDto, List<CartItemDto>> itemsByInstructor = new HashMap<>();
        if (cartDto.getItems() != null) {
            for (CartItemDto item : cartDto.getItems()) {
                if (item.getCourse() != null && item.getCourse().getInstructor() != null) {
                    UserDto instructor = item.getCourse().getInstructor();
                    if (!itemsByInstructor.containsKey(instructor)) {
                        itemsByInstructor.put(instructor, new ArrayList<>());
                    }
                    itemsByInstructor.get(instructor).add(item);
                }
            }
        }

        int cartSize = cartItemService.countItemsInCart(cart);

        long subtotal = 0;
        long selectedItemsCount = 0;

        Map<Integer, String> instructorCheckboxState = new HashMap<>(); // "checked", "unchecked", "indeterminate"

        for (Map.Entry<UserDto, List<CartItemDto>> entry : itemsByInstructor.entrySet()) {
            UserDto instructorDto = entry.getKey();
            List<CartItemDto> itemsList = entry.getValue();

            long instSubtotal = 0;
            long groupSelectedCount = 0;

            for (CartItemDto item : itemsList) {
                if (item.isSelected()) {
                    long price = item.getCourse().getPrice().longValue();

                    subtotal += price;
                    selectedItemsCount++;

                    instSubtotal += price;
                    groupSelectedCount++;
                }
            }

            // Xác định trạng thái checkbox của giảng viên
            if (groupSelectedCount == itemsList.size()) {
                instructorCheckboxState.put(instructorDto.getId(), "checked");
            } else if (groupSelectedCount == 0) {
                instructorCheckboxState.put(instructorDto.getId(), "unchecked");
            } else {
                instructorCheckboxState.put(instructorDto.getId(), "indeterminate");
            }
        }

        long total = subtotal;
        if (total < 0) total = 0;

        boolean allSelected = true;
        boolean noneSelected = true;

        if (cart.getItems().isEmpty()) {
            allSelected = false;
        } else {
            for (CartItem item : cart.getItems()) {
                if (item.isSelected()) {
                    noneSelected = false;
                } else {
                    allSelected = false;
                }
            }
        }
        String globalCheckboxState = allSelected ? "checked" : (noneSelected ? "unchecked" : "indeterminate");

        return CartPageDetailsDto.builder()
                .cart(cartDto)
                .itemsByInstructor(itemsByInstructor)
                .cartSize(cartSize)
                .subtotal(subtotal)
                .total(total)
                .selectedItemsCount(selectedItemsCount)
                .instructorCheckboxState(instructorCheckboxState)
                .globalCheckboxState(globalCheckboxState)
                .build();
    }

    public Map<String, Object> addCourseToCart(User user, Integer courseId) {
        Cart cart = getOrCreateCartForUser(user);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId));
        
        if (!CourseStatus.PUBLISHED.equals(course.getStatus())) {
            throw new IllegalArgumentException("Không thể thêm vào giỏ hàng vì khóa học chưa được xuất bản.");
        }

        boolean newlyAdded = cartItemService.addCourseToCart(cart, course);
        int cartSize = cartItemService.countItemsInCart(cart);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("newlyAdded", newlyAdded);
        response.put("message", newlyAdded ? "Đã thêm khóa học vào giỏ hàng thành công!" : "Khóa học này đã có sẵn trong giỏ hàng.");
        response.put("cartSize", cartSize);
        return response;
    }

    public void removeCartItem(Integer cartItemId) {
        cartItemService.deleteById(cartItemId);
    }

    public int getCartCount(User user) {
        Cart cart = getOrCreateCartForUser(user);
        return cartItemService.countItemsInCart(cart);
    }

    public void  toggleSelect(Integer cartItemId, Boolean selected) {
        CartItem item = cartItemService.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng."));
        if (selected != null) {
            item.setSelected(selected);
        } else {
            item.setSelected(!item.isSelected());
        }
        cartItemService.save(item);
    }

    public void toggleSelectInstructor(User user, Integer instructorId, Boolean selected) {
        Cart cart = getOrCreateCartForUser(user);
        for (CartItem item : cart.getItems()) {
            if (item.getCourse() != null && item.getCourse().getInstructor() != null
                    && item.getCourse().getInstructor().getId().equals(instructorId)) {
                item.setSelected(selected);
                cartItemService.save(item);
            }
        }
    }

    public void toggleSelectAll(User user, Boolean selected) {
        Cart cart = getOrCreateCartForUser(user);
        for (CartItem item : cart.getItems()) {
            item.setSelected(selected);
            cartItemService.save(item);
        }
    }


    public Map<String, Object> checkoutCart(User user) {
        Map<String, Object> response = new HashMap<>();
        Cart cart = getOrCreateCartForUser(user);
        Set<CartItem> items = cart.getItems();

        if (items == null || items.isEmpty()) {
            response.put("success", false);
            response.put("message", "Giỏ hàng rỗng.");
            return response;
        }

        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        if (selectedItems.isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng chọn ít nhất một khóa học để thanh toán.");
            return response;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        java.math.BigDecimal orderSubtotal = java.math.BigDecimal.ZERO;

        for (CartItem item : selectedItems) {
            if (item.getCourse() == null) continue;
            Course course = item.getCourse();
            java.math.BigDecimal price = course.getPrice();
            if (price == null) price = java.math.BigDecimal.ZERO;

            OrderItem orderItem = OrderItem.builder()
                    .priceSnapshot(price)
                    .courseTitleSnapshot(course.getTitle())
                    .course(course)
                    .build();

            orderItems.add(orderItem);
            orderSubtotal = orderSubtotal.add(price);
        }

        // Create and save the Order
        Order order = Order.builder()
                .user(user)
                .totalAmount(orderSubtotal)
                .status(OrderStatus.COMPLETED)
                .paymentMethod("ATM / Internet Banking")
                .build();

        for (OrderItem oi : orderItems) {
            order.addItem(oi);
        }

        orderRepository.save(order);

        // Process enrollments and remove from cart
        for (CartItem item : selectedItems) {
            Course course = item.getCourse();
            if (course == null) continue;
            boolean alreadyEnrolled = enrollmentRepository.existsByUserAndCourse(user, course);
            if (!alreadyEnrolled) {
                Enrollment enrollment = Enrollment.builder()
                        .user(user)
                        .course(course)
                        .progressPercent(java.math.BigDecimal.ZERO)
                        .build();
                enrollmentRepository.save(enrollment);
            }
            cart.removeItem(item);
            cartItemService.deleteById(item.getId());
        }

        save(cart);

        response.put("success", true);
        response.put("message", "Thanh toán thành công! Khóa học đã được thêm vào Việc Học Của Tôi.");
        return response;
    }
}
