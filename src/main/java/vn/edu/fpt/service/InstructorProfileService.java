package vn.edu.fpt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.user.InstructorProfileViewDto;
import vn.edu.fpt.dto.user.ProfileDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.FeedbackRepository;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;

@Service
@Transactional
public class InstructorProfileService {
    private final UserService userService;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;

    public InstructorProfileService(UserService userService, FeedbackRepository feedbackRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.feedbackRepository = feedbackRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public InstructorProfileViewDto getCurrentInstructorProfile(boolean editMode) {
        User currentUser = SecurityUtils.getCurrentUser();
        return buildProfileView(buildProfileDto(currentUser), currentUser, editMode);
    }

    public InstructorProfileViewDto getCurrentInstructorProfileForInvalidForm(ProfileDto profileDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        profileDto.setAvatar_url(buildAvatarUrl(currentUser));
        profileDto.setEmail(currentUser.getEmail());
        return buildProfileView(profileDto, currentUser, true);
    }

    public void updateCurrentInstructorProfile(ProfileDto profileDto) {
        userService.updateProfileInstuctor(SecurityUtils.getCurrentUser(), profileDto);
    }

    public void updateCurrentInstructorPassword(String oldPassword, String newPassword, String confirmPassword) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Không tìm thấy thông tin tài khoản.");
        }

        User user = userService.findById(currentUser.getId());

        if (newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ mật khẩu mới.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }

        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            if (oldPassword == null || oldPassword.isBlank()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu hiện tại.");
            }
            if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
            }
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.save(user);
    }

    private InstructorProfileViewDto buildProfileView(ProfileDto profileDto, User instructor, boolean editMode) {
        Double averageRating = feedbackRepository.getAverageRatingByInstructorId(instructor.getId());
        double safeAverage = averageRating != null ? averageRating : 0.0;
        double roundedAverage = Math.round(safeAverage * 10.0) / 10.0;
        int ratingStars = (int) Math.round(safeAverage);
        long ratingCount = feedbackRepository.countRatingsByInstructorId(instructor.getId());

        return new InstructorProfileViewDto(profileDto, roundedAverage, ratingStars, ratingCount, editMode);
    }

    private ProfileDto buildProfileDto(User user) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setFirstname(user.getFirstName());
        profileDto.setLastname(user.getLastName());
        profileDto.setBio(user.getBio());
        profileDto.setAvatar_url(buildAvatarUrl(user));
        profileDto.setEmail(user.getEmail());
        profileDto.setPhone(user.getPhone());
        return profileDto;
    }

    private String buildAvatarUrl(User user) {
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            return null;
        }
        return AppConstants.AZURE_STORAGE_BASE_URL + "/"
                + AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS + "/"
                + user.getAvatarUrl();
    }
}
