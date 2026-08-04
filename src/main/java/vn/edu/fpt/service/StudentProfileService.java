package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.dto.user.StudentLessonNoteDto;
import vn.edu.fpt.dto.user.StudentProfileDashboardDto;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.LessonNoteRepository;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.util.AppConstants;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentProfileService {

    private final DtoMapper dtoMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AzureBlobService azureBlobService;
    private final LessonNoteRepository lessonNoteRepository;

    public StudentProfileDashboardDto getDashboardData(User user) {
        int enrollmentsCount = user.getEnrollments().size();
        long certificatesCount = 0;
        for (Enrollment e : user.getEnrollments()) {
            if (e.getProgressPercent() != null && e.getProgressPercent().doubleValue() >= 100) {
                certificatesCount++;
            }
        }
                
        int totalHours = 0;
        for (Enrollment en : user.getEnrollments()) {
            double pct = en.getProgressPercent() != null ? en.getProgressPercent().doubleValue() : 0.0;
            totalHours += (int) (pct * 8.0 / 100.0);
        }
        if (totalHours == 0 && enrollmentsCount > 0) {
            totalHours = 2;
        }

        // Fetch lesson notes
        List<LessonNote> notesList = lessonNoteRepository.findByUser_IdOrderByCreatedAtDesc(user.getId());
        Map<Integer, StudentLessonNoteDto> courseNotesMap = new LinkedHashMap<>();
        for (LessonNote note : notesList) {
            Lesson lesson = note.getLesson();
            if (lesson == null) continue;
            CourseSection cs = lesson.getCourseSection();
            if (cs == null) continue;
            Course course = cs.getCourse();
            if (course == null) continue;

            StudentLessonNoteDto courseDto = courseNotesMap.computeIfAbsent(course.getId(), cid -> StudentLessonNoteDto.builder()
                    .courseId(course.getId())
                    .courseTitle(course.getTitle())
                    .notes(new ArrayList<>())
                    .build());

            courseDto.getNotes().add(StudentLessonNoteDto.NoteDetailDto.builder()
                    .id(note.getId())
                    .lessonId(lesson.getId())
                    .sectionId(cs.getId())
                    .lessonTitle(lesson.getTitle())
                    .videoTimeSeconds(note.getVideoTimeSeconds())
                    .formattedTime(formatTime(note.getVideoTimeSeconds()))
                    .noteContent(note.getNoteContent())
                    .createdAt(note.getCreatedAt())
                    .build());
        }

        List<StudentLessonNoteDto> groupedNotes = new ArrayList<>(courseNotesMap.values());

        return StudentProfileDashboardDto.builder()
                .currentUser(dtoMapper.toUserDto(user))
                .enrollmentsCount(enrollmentsCount)
                .certificatesCount(certificatesCount)
                .studyHours(totalHours)
                .lessonNotes(groupedNotes)
                .build();
    }

    private String formatTime(Integer videoTimeSeconds) {
        if (videoTimeSeconds == null) return "00:00";
        int h = videoTimeSeconds / 3600;
        int m = (videoTimeSeconds % 3600) / 60;
        int s = videoTimeSeconds % 60;
        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        }
        return String.format("%02d:%02d", m, s);
    }

    public User updateProfile(User user, String firstName, String lastName, String email, String phone, 
                              MultipartFile avatarFile, boolean deleteAvatar) throws Exception {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên và địa chỉ email không được để trống!");
        }

        // Check email duplication
        Optional<User> existingUserOpt = userRepository.findByEmail(email.trim());
        if (existingUserOpt.isPresent() && !existingUserOpt.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Địa chỉ email này đã được sử dụng bởi một tài khoản khác!");
        }

        // Check phone duplication
        if (phone != null && !phone.isBlank()) {
            User existingPhoneUser = userRepository.findUserByPhone(phone.trim());
            if (existingPhoneUser != null && !existingPhoneUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Số điện thoại này đã được sử dụng bởi một tài khoản khác!");
            }
            user.setPhone(phone.trim());
        } else {
            user.setPhone(null);
        }

        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setEmail(email.trim());

        if (deleteAvatar) {
            user.setAvatarUrl(null);
        } else if (avatarFile != null && !avatarFile.isEmpty()) {
            if (avatarFile.getSize() > 1024 * 1024) {
                throw new IllegalArgumentException("Kích thước ảnh đại diện không được vượt quá 1MB!");
            }
            try {
                String uploadedUrl = azureBlobService.saveFile(avatarFile, AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS);
                user.setAvatarUrl(uploadedUrl);
            } catch (Exception e) {
                throw new Exception("Lỗi xảy ra trong quá trình lưu ảnh đại diện lên cloud storage.");
            }
        }

        return userRepository.save(user);
    }

    public User changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        user = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin tài khoản."));

        if (newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin mật khẩu mới.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }

        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            // User already has a password, verify old password
            if (oldPassword == null || oldPassword.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu hiện tại.");
            }
            if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
            }
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
