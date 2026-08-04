package vn.edu.fpt.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.user.LearnerInfomationGrantAccessDTO;
import vn.edu.fpt.dto.user.ProfileDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.exception.UserValidationException;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.Validation;

import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.enums.LogAction;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.util.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {


    private AzureBlobService azureBlobService;
    private UserRepository repository;
    private Validation validation;
    private AuthService authService;
    private DtoMapper dtoMapper;
    private CourseRepository courseRepository;
    private SystemLogService systemLogService;

    public UserService(AzureBlobService azureBlobService, UserRepository repository,
                       Validation validation, AuthService authService,
                       DtoMapper dtoMapper, CourseRepository courseRepository,
                       SystemLogService systemLogService) {
        this.azureBlobService = azureBlobService;
        this.repository = repository;
        this.validation = validation;
        this.authService = authService;
        this.dtoMapper = dtoMapper;
        this.courseRepository = courseRepository;
        this.systemLogService = systemLogService;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public User save(User entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }



    public void updateProfileInstuctor(User user, ProfileDto profileDto) {

        if (profileDto.getPhone() != null && !profileDto.getPhone().isBlank()) {
            User tmp = repository.findUserByPhone(profileDto.getPhone().trim());
            if (tmp != null && !tmp.getId().equals(user.getId())) {
                throw new UserValidationException("phone", "Số điện thoại này đã được sử dụng.");
            }
            user.setPhone(profileDto.getPhone().trim());
        } else {
            user.setPhone(null);
        }

        if (profileDto.getFile() != null && !profileDto.getFile().isEmpty()) {
            if (profileDto.getFile().getSize() > 2 * 1024 * 1024) {
                throw new UserValidationException("file","Vui lòng chọn ảnh có kích thước không vượt quá 2MB.");
            }

            String url = azureBlobService.saveFile(profileDto.getFile(), AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS);
            user.setAvatarUrl(url);
        }

        user.setFirstName(profileDto.getFirstname().trim());
        user.setLastName(profileDto.getLastname().trim());
        user.setBio(profileDto.getBio() != null ? profileDto.getBio().trim() : "");
        user.setUpdatedAt(LocalDateTime.now());

        repository.save(user);
    }

    // =========================================================================
    // ACADEMIC MANAGER (QUẢN LÝ HỌC THUẬT) SECTION
    // =========================================================================

    /**
     * Tìm kiếm và lọc danh sách giảng viên (hỗ trợ phân trang).
     */
    public Page<UserDto> searchAndFilterInstructors(String keyword, UserStatus status, Pageable pageable) {
        Page<User> instructorPage = repository.searchAndFilterInstructors(keyword, status, pageable);
        return instructorPage.map(dtoMapper::toInstructorListDto);
    }

    /**
     * Lấy thông tin chi tiết giảng viên dưới dạng DTO.
     */
    public UserDto getInstructorDetail(Integer id) {
        User instructor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + id));
        UserDto dto = dtoMapper.toUserDto(instructor);
        dto.setCourseCount((int) instructor.getCourses().stream().filter(c -> c.getStatus() == vn.edu.fpt.enums.CourseStatus.PUBLISHED).count());
        return dto;
    }

    /**
     * Lấy danh sách khóa học của một giảng viên.
     */
    public List<CourseDto> getInstructorCourses(Integer instructorId) {
        User instructor = repository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + instructorId));
        return courseRepository.findByInstructorAndStatus(instructor, vn.edu.fpt.enums.CourseStatus.PUBLISHED)
                .stream()
                .map(dtoMapper::toSimpleCourseDto)
                .toList();
    }

    /**
     * Cập nhật trạng thái tài khoản giảng viên (ACTIVE / BANNED).
     */
    public void updateInstructorStatus(Integer id, UserStatus status) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("Bạn không thể tự thay đổi trạng thái tài khoản của chính mình!");
        }

        User instructor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + id));
        instructor.setStatus(status);
        repository.save(instructor);

        // Log action to SystemLog
        if (currentUser != null) {
            LogAction action = (status == UserStatus.ACTIVE) ? LogAction.UNBLOCK_USER : 
                               (status == UserStatus.BANNED) ? LogAction.BLOCK_USER : null;
            if (action != null) {
                String meta = "Giảng viên: " + instructor.getLastName() + " " + instructor.getFirstName() + " (" + instructor.getEmail() + ")";
                systemLogService.log(currentUser, action, "USER", String.valueOf(id), meta);
            }
        }
    }

    // =========================================================================
    // ADMIN SECTION
    // =========================================================================

    /**
     * Tìm kiếm và lọc danh sách manager (hỗ trợ phân trang).
     */
    public Page<UserDto> searchAndFilterManagers(String keyword, UserStatus status, Pageable pageable) {
        Page<User> managerPage = repository.searchAndFilterManagers(keyword, status, pageable);
        return managerPage.map(dtoMapper::toUserDto);
    }

    /**
     * Lấy thông tin chi tiết manager dưới dạng DTO.
     */
    public UserDto getManagerDetail(Integer id) {
        User manager = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy manager với ID: " + id));
        return dtoMapper.toUserDto(manager);
    }

    /**
     * Cập nhật trạng thái tài khoản manager (ACTIVE / BANNED).
     */
    public void updateManagerStatus(Integer id, UserStatus status) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("Bạn không thể tự thay đổi trạng thái tài khoản của chính mình!");
        }

        User manager = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy manager với ID: " + id));
        manager.setStatus(status);
        repository.save(manager);
    }

    public Page<LearnerInfomationGrantAccessDTO> findAllLearnerByFilter(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_LEARNER_RECORD_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = repository.findAllLearnerByFilter(keyword, pageable);
        return users.map(dtoMapper::toLearnerInfomationGrantAccessDto);
    }
}

