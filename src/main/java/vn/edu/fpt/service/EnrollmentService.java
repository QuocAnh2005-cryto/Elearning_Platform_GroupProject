package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.enums.EnrollmentGrantReason;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.LogAction;
import vn.edu.fpt.enums.OrderStatus;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.exception.CourseNotFoundException;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.repository.EnrollmentRepository;
import vn.edu.fpt.repository.OrderRepository;
import vn.edu.fpt.repository.PaymentRepository;
import vn.edu.fpt.service.lesson.LessonProgressService;
import vn.edu.fpt.service.lesson.LessonService;
import vn.edu.fpt.service.payment.PayOsService;
import vn.edu.fpt.util.SecurityUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository repository;
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;
    private final SystemLogService systemLogService;
    private final UserService userService;
    private final CourseService courseService;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PayOsService payOsService;

    public Set<Integer> getEnrolledCourseIds(User user) {
        Set<Integer> enrolledCourseIds = new HashSet<>();
        if (user != null) {
            List<Enrollment> userEnrollments = repository.findByUser(user);
            for (Enrollment e : userEnrollments) {
                if (e.getCourse() != null) {
                    enrolledCourseIds.add(e.getCourse().getId());
                }
            }
        }
        return enrolledCourseIds;
    }

    public List<Enrollment> findAll() {
        return repository.findAll();
    }

    public Optional<Enrollment> findById(Integer id) {
        return repository.findById(id);
    }

    public Enrollment save(Enrollment entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Enrollment findEnrollmentByCourseIdAndUserId(Integer courseId, Integer userId) {
        Enrollment enrollment = repository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng chưa mua khóa học này"));
        return enrollment;
    }

    public void updateEnrollmentProgressPercent(Enrollment enrollment, Integer courseId, Integer sectionId) {
        Integer totalNumberOfLesson = lessonService.findNumberOfLessonByCourseId(courseId);
        if (totalNumberOfLesson == 0) {
            return;
        }
        Integer totalNumberOfLessonCompleted = lessonProgressService
                .findNumberOfLessonCompletedByEnrollment(enrollment);
        BigDecimal percent = BigDecimal.valueOf((double) totalNumberOfLessonCompleted / totalNumberOfLesson * 100);
        if (percent.compareTo(BigDecimal.valueOf(100)) < 0) {
            enrollment.setProgressPercent(percent);
        } else {
            if (enrollment.getCompletedAt() == null) {
                enrollment.setProgressPercent(percent);
                enrollment.setCompletedAt(LocalDateTime.now());
            }
        }
        repository.save(enrollment);
    }

    public void grantAccessCourse(Integer userId, Integer courseId, String reason, String note, Boolean sendEmail) {
        if (userId == null) {
            throw new RuntimeException("Không có id người dùng");
        }
        if (courseId == null) {
            throw new CourseNotFoundException("Không tìm thấy khóa học");
        }
        if (reason == null) {
            throw new RuntimeException("Lý do không được để trống");
        }
        if (EnrollmentGrantReason.OTHER.name().equals(reason)) {
            if (note == null || note.trim().isEmpty()) {
                throw new RuntimeException("Vui lòng điền mục ghi chú khi chọn lý do khác");
            }
        }
        if (sendEmail == null) {
            sendEmail = false;
        }
        Course courseObj = courseService.findById(courseId);
        if (!CourseStatus.PUBLISHED.equals(courseObj.getStatus())) {
            throw new RuntimeException("Chỉ có thể cấp quyền cho khóa học đã được xuất bản");
        }
        if (repository.existsByUserAndCourse(User.builder().id(userId).build(),
                Course.builder().id(courseId).build())) {
            throw new RuntimeException(
                    "Người dùng với id = " + userId + " đã tham gia khóa học với id = " + courseId + " trước đó");
        }
        if (EnrollmentGrantReason.PAYMENT_RECOVERY.name().equals(reason)) {
            User user = User.builder().id(userId).build();
            List<Order> orders = orderRepository.findByUser(user);
            boolean foundMissedOrder = false;
            if (orders != null) {
                for (Order order : orders) {
                    if (order.getStatus() != OrderStatus.COMPLETED && order.getStatus() != OrderStatus.PAID) {
                        for (OrderItem item : order.getItems()) {
                            if (item.getCourse() != null && item.getCourse().getId().equals(courseId)) {
                                if (order.getPayment() != null) {
                                    String status = payOsService.getPaymentStatusByOrderCode(order.getPayment().getGatewayOrderCode());
                                    OrderStatus orderStatus;
                                    try {
                                        orderStatus = OrderStatus.valueOf(status);
                                    } catch (IllegalArgumentException e) {
                                        throw new RuntimeException("Không tồn tại trạng thái thanh toán được đồng bộ từ PayOs");
                                    }
                                    if (OrderStatus.PAID.equals(orderStatus)) {
                                        foundMissedOrder = true;
                                        order.setStatus(OrderStatus.COMPLETED);
                                        orderRepository.save(order);
                                        Payment payment = order.getPayment();
                                        payment.setStatus(PaymentStatus.PAID);
                                        payment.setPaidAt(LocalDateTime.now());
                                        paymentRepository.save(payment);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (foundMissedOrder) {
                        break;
                    }
                }
            }
            if (!foundMissedOrder) {
                throw new RuntimeException(
                        "Học viên chưa từng tạo đơn hàng lỗi hoặc chờ thanh toán cho khóa học này, hoặc đơn hàng chưa được thanh toán thành công trên PayOS.");
            }
        }

        Enrollment enrollment = Enrollment.builder()
                .user(User.builder().id(userId).build())
                .course(Course.builder().id(courseId).build())
                .progressPercent(BigDecimal.ZERO)
                .build();
        repository.save(enrollment);

        String courseTitle = courseObj != null ? courseObj.getTitle() : "Khóa học #" + courseId;
        String finalNote = (note == null || note.trim().isEmpty()) ? "NONE" : note.trim();
        SystemLog systemLog = SystemLog.builder()
                .action(LogAction.MANUAL_ENROLLMENT_GRANTED)
                .user(SecurityUtils.getCurrentUser())
                .targetType(Enrollment.class.getName())
                .targetId(enrollment.getId().toString())
                .meta("Cấp quyền khóa học: " + courseTitle + " (ID: " + courseId + ") - Lý do: " + reason
                        + " - Ghi chú: " + finalNote)
                .build();
        systemLogService.save(systemLog);
        if (sendEmail) {
            emailService.sendGrantAccessCourseEmail(userService.findById(userId).getEmail(),
                    courseService.findById(courseId));
        }
    }

    public String grantAccessCourses(Integer userId, List<Integer> courseIds, String reason, String note,
                                     Boolean sendEmail) {
        if (userId == null) {
            throw new RuntimeException("Không có id người dùng");
        }
        if (courseIds == null || courseIds.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ít nhất một khóa học");
        }
        if (reason == null) {
            throw new RuntimeException("Lý do không được để trống");
        }

        int successCount = 0;
        StringBuilder alreadyEnrolledMsg = new StringBuilder();
        List<Course> enrolledCourses = new java.util.ArrayList<>();

        for (Integer courseId : courseIds) {
            try {
                grantAccessCourse(userId, courseId, reason, note, false);
                enrolledCourses.add(courseService.findById(courseId));
                successCount++;
            } catch (RuntimeException e) {
                if (e.getMessage() != null && e.getMessage().contains("đã tham gia khóa học")) {
                    alreadyEnrolledMsg.append(e.getMessage()).append(". ");
                } else {
                    throw e;
                }
            }
        }

        if (successCount == 0 && alreadyEnrolledMsg.length() > 0) {
            throw new RuntimeException(alreadyEnrolledMsg.toString());
        }

        if (sendEmail != null && sendEmail && !enrolledCourses.isEmpty()) {
            String toEmail = userService.findById(userId).getEmail();
            emailService.sendGrantAccessCoursesEmail(toEmail, enrolledCourses);
        }

        String msg = "Thành công thêm người dùng vào " + successCount + " khóa học.";
        if (alreadyEnrolledMsg.length() > 0) {
            msg += " Lưu ý: " + alreadyEnrolledMsg.toString();
        }
        return msg;
    }
}
