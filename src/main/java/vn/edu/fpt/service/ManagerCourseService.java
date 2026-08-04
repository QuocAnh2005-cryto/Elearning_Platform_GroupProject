package vn.edu.fpt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.LogAction;
import vn.edu.fpt.exception.CourseNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.util.SecurityUtils;

@Service
@Transactional
public class ManagerCourseService {

    private final CourseRepository courseRepository;
    private final DtoMapper dtoMapper;
    private final EmailService emailService;
    private final SystemLogService systemLogService;

    public ManagerCourseService(CourseRepository courseRepository, DtoMapper dtoMapper, EmailService emailService, SystemLogService systemLogService) {
        this.courseRepository = courseRepository;
        this.dtoMapper = dtoMapper;
        this.emailService = emailService;
        this.systemLogService = systemLogService;
    }

    public Page<CourseDto> searchAndFilter(String keyword, CourseStatus status, Integer categoryId, Pageable pageable) {
        return courseRepository.searchAndFilter(keyword, status, categoryId, pageable)
                .map(dtoMapper::toSimpleCourseDto);
    }

    public void updateCourseStatus(Integer id, CourseStatus status) {
        updateCourseStatus(id, status, null);
    }

    public void updateCourseStatus(Integer id, CourseStatus status, String rejectionReason) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Khóa học không tìm thấy"));

        if (status != CourseStatus.PUBLISHED && status != CourseStatus.REJECTED) {
            throw new IllegalStateException("Quản lý chỉ được phê duyệt hoặc từ chối khóa học.");
        }
        
        if (course.getStatus() != CourseStatus.PENDING && course.getStatus() != CourseStatus.RESUBMIT) {
            throw new IllegalStateException("Khóa học này đã được phê duyệt hoặc từ chối bởi một quản lý khác trước đó.");
        }

        updateCourseState(course, status, rejectionReason);
        courseRepository.save(course);

        logCourseStatusChange(course, status, rejectionReason);
        sendCourseStatusNotification(course, status, rejectionReason);
    }

    private void updateCourseState(Course course, CourseStatus status, String rejectionReason) {
        course.setStatus(status);
        if (status == CourseStatus.REJECTED) {
            course.setRejectionReason(rejectionReason);
        } else if (status == CourseStatus.PUBLISHED) {
            course.setRejectionReason(null); // Xóa lý do từ chối nếu khóa học được phê duyệt
        }
    }

    private void logCourseStatusChange(Course course, CourseStatus status, String rejectionReason) {
        vn.edu.fpt.entity.User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) return;

        LogAction action = (status == CourseStatus.PUBLISHED) ? LogAction.APPROVE_COURSE : 
                           (status == CourseStatus.REJECTED) ? LogAction.REJECT_COURSE : null;
                           
        if (action != null) {
            String meta = (status == CourseStatus.REJECTED && rejectionReason != null && !rejectionReason.isBlank()) 
                          ? rejectionReason 
                          : ((status == CourseStatus.PUBLISHED) ? "Khóa học đã được phê duyệt" : course.getTitle());
            systemLogService.log(currentUser, action, "COURSE", String.valueOf(course.getId()), meta);
        }
    }

    private void sendCourseStatusNotification(Course course, CourseStatus status, String rejectionReason) {
        if (course.getInstructor() == null || course.getInstructor().getEmail() == null) return;

        try {
            String fullName = (course.getInstructor().getLastName() + " " + course.getInstructor().getFirstName()).trim();
            String email = course.getInstructor().getEmail();

            if (status == CourseStatus.PUBLISHED) {
                emailService.sendCourseApprovedEmail(email, fullName, course.getTitle());
            } else if (status == CourseStatus.REJECTED) {
                emailService.sendCourseRejectedEmail(email, fullName, course.getTitle(), rejectionReason);
            }
        } catch (Exception e) {
            System.err.println("Failed to send course approval/rejection email: " + e.getMessage());
        }
    }
}
