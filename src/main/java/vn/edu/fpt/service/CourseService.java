package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import vn.edu.fpt.dto.*;
import vn.edu.fpt.dto.course.*;
import vn.edu.fpt.dto.home.HomeDto;
import vn.edu.fpt.dto.lesson.LessonNoteSiderbarDTO;
import vn.edu.fpt.dto.lesson.LessonSiderbarDTO;
import vn.edu.fpt.dto.lesson.SectionSiderbarDTO;
import vn.edu.fpt.entity.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.FeedbackStatus;
import vn.edu.fpt.exception.CourseNotFoundException;
import vn.edu.fpt.exception.CourseValidationException;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.repository.*;
import vn.edu.fpt.enums.CourseLevel;
import vn.edu.fpt.mapper.DtoMapper;

import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.service.lesson.LessonNoteService;
import vn.edu.fpt.service.lesson.LessonService;
import vn.edu.fpt.util.AppConstants;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final DtoMapper dtoMapper;
    private final CategoryRepository categoryRepository;
    private final AzureBlobService azureBlobService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonService lessonService;
    private final FeedbackService feedbackService;
    private final LessonNoteService lessonNoteService;
    private final EnrollmentRepository enrollmentRepository;

    public Page<CourseDto>findByInstructorAndStatus(User instructor, Pageable pageable, CourseStatus courseStatus) {
        return courseRepository.findByInstructorAndStatus(instructor, pageable, courseStatus).map(dtoMapper::toCourseDto);
    }

    public void deleteCourseById(Integer courseId, User user) {
        Course course = getInstructorOwnedCourse(courseId, user);

        if (course.getStatus() != CourseStatus.DRAFT && course.getStatus() != CourseStatus.REJECTED) {
            throw new CourseValidationException("status", "Chỉ có thể xóa khóa học ở trạng thái Bản nháp (DRAFT) hoặc Bị từ chối (REJECTED).");
        }

        if (enrollmentRepository.countByCourseId(courseId) > 0) {
            throw new CourseValidationException("enrollment", "Khóa học đã có học viên đăng ký, không thể xóa.");
        }

        if (hasText(course.getIntroVideoUrl())) {
            try {
                azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, course.getIntroVideoUrl());
            } catch (Exception e) {
                System.err.println("Warning: Khong the xoa video gioi thieu khoa hoc: " + e.getMessage());
            }
        }

        courseRepository.deleteCourseById(courseId);
    }

    public Course getInstructorOwnedCourse(Integer courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học có id = " + courseId));
        if (user == null || course.getInstructor() == null || !course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền thao tác với khóa học này");
        }
        return course;
    }

    public void hidePublishedCourse(Integer courseId, User user) {
        Course course = getInstructorOwnedCourse(courseId, user);
        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new CourseValidationException("status", "Chỉ khóa học đang bán mới được ẩn.");
        }
        course.setStatus(CourseStatus.HIDDEN);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
    }

    public void publishHiddenCourse(Integer courseId, User user) {
        Course course = getInstructorOwnedCourse(courseId, user);
        if (course.getStatus() != CourseStatus.HIDDEN) {
            throw new CourseValidationException("status", "Chỉ khóa học đã ẩn mới được hiện lại.");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
    }

    public List<Feedback> getInstructorCourseReviews(Integer courseId, User user) {
        getInstructorOwnedCourse(courseId, user);
        return feedbackRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
    }

    public Course save(User user, CourseCreateDto courseCreateDto) {
        Course course;
        boolean isUpdate = courseCreateDto.getId() != null;
        String normalizedTitle = courseCreateDto.getTitle() != null ? courseCreateDto.getTitle().trim() : "";

        if (isUpdate) {
            course = courseRepository.findById(courseCreateDto.getId())
                    .orElseThrow(() -> new CourseNotFoundException("Khóa học không tìm thấy"));
                    
            if (vn.edu.fpt.enums.CourseStatus.PUBLISHED.equals(course.getStatus()) || 
                vn.edu.fpt.enums.CourseStatus.PENDING.equals(course.getStatus())) {
                throw new CourseValidationException("status", "Không thể chỉnh sửa khóa học khi đang ở trạng thái " + course.getStatus().getLabel() + ".");
            }

            if (courseRepository.existsDuplicateTitleForInstructor(user.getId(), normalizedTitle, course.getId())) {
                throw new CourseValidationException(
                        "title",
                        "Bạn đã tạo một khóa học với tiêu đề này. Vui lòng sử dụng tiêu đề khác.");
            }
        } else {
            validateInstructorProfileReadyForCreateCourse(user);
            course = new Course();
            if (courseRepository.existsDuplicateTitleForInstructor(user.getId(), normalizedTitle, null)) {
                throw new CourseValidationException(
                        "title",
                        "Bạn đã tạo một khóa học với tiêu đề này. Vui lòng sử dụng tiêu đề khác.");
            }
            course.setCreatedAt(LocalDateTime.now());
            course.setStatus(CourseStatus.DRAFT);
            course.setInstructor(user);
        }

        if (normalizedTitle.length() < 3) {
            throw new CourseValidationException(
                    "title",
                    "Tiêu đề khóa học phải có ít nhất 3 ký tự.");
        }

        String descriptionText = toPlainText(courseCreateDto.getDescription());
        if (descriptionText.length() < 50) {
            throw new CourseValidationException(
                    "description",
                    "Mô tả khóa học phải có tối thiểu 50 ký tự.");
        }

        validateCoursePrice(courseCreateDto.getPrice());

        String thumbnailUrl = course.getThumbnailUrl();
        if (courseCreateDto.getThumbnailFile() != null && !courseCreateDto.getThumbnailFile().isEmpty()) {
            thumbnailUrl = azureBlobService.saveFile(courseCreateDto.getThumbnailFile(),
                    AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS);
        }

        String introVideoUrl = course.getIntroVideoUrl();
        if (hasText(courseCreateDto.getIntroVideoUrl())) {
            String newIntroVideoUrl = courseCreateDto.getIntroVideoUrl().trim();
            if (hasText(introVideoUrl) && !introVideoUrl.equals(newIntroVideoUrl)) {
                try {
                    azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, introVideoUrl);
                } catch (Exception e) {
                    System.err.println("Warning: Khong the xoa video gioi thieu cu: " + e.getMessage());
                }
            }
            introVideoUrl = newIntroVideoUrl;
        }

        Category category = categoryRepository.findById(courseCreateDto.getCategoryId())
                .orElseThrow(() -> new CourseValidationException("categoryId", "Category không tồn tại"));

        course.setTitle(normalizedTitle);
        course.setDescription(courseCreateDto.getDescription());
        course.setCategory(category);
        course.setPrice(courseCreateDto.getPrice());
        course.setThumbnailUrl(thumbnailUrl);
        course.setIntroVideoUrl(introVideoUrl);
        course.setLevel(courseCreateDto.getLevel());
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public void validateInstructorProfileReadyForCreateCourse(User user) {
        List<String> missingFields = getMissingInstructorProfileFields(user);
        if (!missingFields.isEmpty()) {
            throw new CourseValidationException(
                    "profile",
                    "Vui lòng cập nhật đầy đủ hồ sơ giảng viên trước khi tạo khóa học: "
                            + String.join(", ", missingFields) + ".");
        }
    }

    private List<String> getMissingInstructorProfileFields(User user) {
        List<String> missingFields = new ArrayList<>();
        if (user == null || !hasText(user.getFirstName()))
            missingFields.add("Tên");
        if (user == null || !hasText(user.getLastName()))
            missingFields.add("Họ");
        if (user == null || !hasText(user.getEmail()))
            missingFields.add("Email");
        if (user == null || !hasText(user.getPhone()))
            missingFields.add("Số điện thoại");
        if (user == null || !hasText(user.getBio()))
            missingFields.add("Mô tả bản thân");
        if (user == null || !hasText(user.getAvatarUrl()))
            missingFields.add("Ảnh đại diện");
        return missingFields;
    }

    private void validateCoursePrice(BigDecimal price) {
        if (price == null) {
            throw new CourseValidationException("price", "Vui lòng nhập giá khóa học.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CourseValidationException("price", "Giá khóa học không được âm.");
        }
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        if (price.compareTo(BigDecimal.valueOf(2000)) < 0) {
            throw new CourseValidationException("price",
                    "Giá khóa học phải từ 2.000 VNĐ trở lên hoặc bằng 0 nếu miễn phí.");
        }
        if (price.remainder(BigDecimal.valueOf(1000)).compareTo(BigDecimal.ZERO) != 0) {
            throw new CourseValidationException("price", "Giá khóa học phải chia hết cho 1.000 VNĐ.");
        }
    }

    private String toPlainText(String html) {
        if (html == null) {
            return "";
        }
        return HtmlUtils.htmlUnescape(html)
                .replaceAll("<[^>]*>", " ")
                .replace('\u00A0', ' ')
                .trim();
    }

    /// getCourseForEdit
    public CourseCreateDto getCourseForEdit(Integer coureId, User user) {
        Course course = courseRepository.findById(coureId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khoá học có id = " + coureId));

        if (!course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa khoá học này");
        }

        CourseCreateDto courseCreateDto = new CourseCreateDto();
        courseCreateDto.setId(course.getId());
        courseCreateDto.setTitle(course.getTitle());
        courseCreateDto.setDescription(course.getDescription());
        courseCreateDto.setPrice(course.getPrice());
        courseCreateDto.setThumbnailUrl(course.getThumbnailUrl());
        courseCreateDto.setIntroVideoUrl(course.getIntroVideoUrl());
        courseCreateDto.setIntroVideoPreviewUrl(resolveVideoPreviewUrl(course.getIntroVideoUrl()));
        courseCreateDto.setLevel(course.getLevel());
        courseCreateDto.setCategoryId(course.getCategory() != null ? course.getCategory().getId() : null);

        return courseCreateDto;
    }

    /// Show Chi tiết khoá học
    public CourseRespon getCourseDetailToView(Integer courseId, User user) {
        if (user.getRole() != vn.edu.fpt.enums.RoleType.ADMIN && user.getRole() != vn.edu.fpt.enums.RoleType.MANAGER) {
            getInstructorOwnedCourse(courseId, user);
        }
        Course course = courseRepository.findDetailById(courseId);
        CourseRespon courseRespon = new CourseRespon();
        courseRespon.setTittle(course.getTitle());
        courseRespon.setId(course.getId());
        courseRespon.setCategory(course.getCategory().getName());
        courseRespon.setDescription(course.getDescription());
        courseRespon.setPrice(course.getPrice());
        courseRespon.setLevel(course.getLevel());
        courseRespon.setCreateAt(course.getCreatedAt());
        courseRespon.setThumnaiUrl(course.getThumbnailUrl());
        courseRespon.setIntroVideoUrl(course.getIntroVideoUrl());
        courseRespon.setIntroVideoPreviewUrl(resolveVideoPreviewUrl(course.getIntroVideoUrl()));

        List<SectionRespon> sections = course.getSections().stream().map(section -> {
            SectionRespon sr = new SectionRespon();
            sr.setId(section.getId());
            sr.setPosition(section.getPosition());
            sr.setTitle(section.getTitle());
            sr.setCreateAt(section.getCreatedAt());

            List<LessonRespon> lessons = section.getLessons().stream().map(lesson -> {
                LessonRespon lr = new LessonRespon();
                lr.setId(lesson.getId());
                lr.setPosition(lesson.getPosition());
                lr.setTitle(lesson.getTitle());
                lr.setCreateAt(lesson.getCreatedAt());
                lr.setVideoUrl(lesson.getVideoUrl());
                lr.setDutationSecond(lesson.getDurationSeconds());

                List<LessonMaterialRespon> materials = lesson.getMaterials().stream().map(material -> {
                    LessonMaterialRespon lms = new LessonMaterialRespon();
                    lms.setId(material.getId());
                    lms.setFile_name(material.getFileName());
                    return lms;
                }).toList();
                lr.setMaterials(materials);
                return lr;
            }).toList();
            sr.setLessons(lessons);
            return sr;
        }).toList();

        courseRespon.setSections(sections);

        return courseRespon;
    }

    public CourseDto getCourseDetail(Integer id) {
        Course course = courseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new CourseNotFoundException("Khóa học không tìm thấy"));
        CourseDto dto = dtoMapper.toCourseDto(course);
        dto.setIntroVideoUrl(resolveVideoPreviewUrl(course.getIntroVideoUrl()));
        return dto;
    }

    public String resolveVideoPreviewUrl(String blobName) {
        if (!hasText(blobName)) {
            return null;
        }
        if (blobName.startsWith("http://") || blobName.startsWith("https://")) {
            return blobName;
        }
        return azureBlobService.generateSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, blobName);
    }

    public Page<CourseListDto> getPagedCoursesSummary(
            String search,
            Integer categoryId,
            List<Double> ratings,
            List<String> prices,
            String sort,
            int page,
            int size) {
        int currentPage = Math.max(page, 1);

        Double minRating = (ratings != null && !ratings.isEmpty())
                ? ratings.stream().filter(Objects::nonNull).min(Double::compareTo).orElse(null)
                : null;

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        if (prices != null && !prices.isEmpty()) {
            for (String price : prices) {
                if (price != null && price.contains("-")) {
                    String[] parts = price.split("-");
                    try {
                        BigDecimal min = new BigDecimal(parts[0].trim());
                        BigDecimal max = new BigDecimal(parts[1].trim());
                        if (minPrice == null || min.compareTo(minPrice) < 0)
                            minPrice = min;
                        if (maxPrice == null || max.compareTo(maxPrice) > 0)
                            maxPrice = max;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        Pageable pageable;
        Page<Course> coursePage;

        if ("rating".equals(sort)) {
            pageable = PageRequest.of(currentPage - 1, size);
            coursePage = courseRepository.findPublishedCoursesOrderByRating(search, categoryId, minRating, minPrice, maxPrice,
                    pageable);
        } else {
            Sort sortObj = switch (sort == null ? "" : sort) {
                case "price-asc" -> Sort.by(Sort.Direction.ASC, "price");
                case "price-desc" -> Sort.by(Sort.Direction.DESC, "price");
                default -> Sort.by(Sort.Direction.DESC, "id");
            };
            pageable = PageRequest.of(currentPage - 1, size, sortObj);
            coursePage = courseRepository.findPublishedCourses(search, categoryId, minRating, minPrice, maxPrice, pageable);
        }

        if (coursePage.isEmpty() && currentPage > 1 && coursePage.getTotalPages() > 0) {
            pageable = PageRequest.of(coursePage.getTotalPages() - 1, size, pageable.getSort());
            if ("rating".equals(sort)) {
                coursePage = courseRepository.findPublishedCoursesOrderByRating(search, categoryId, minRating, minPrice,
                        maxPrice, pageable);
            } else {
                coursePage = courseRepository.findPublishedCourses(search, categoryId, minRating, minPrice, maxPrice,
                        pageable);
            }
        }

        return coursePage.map(this::toCourseListDto);
    }

    private CourseListDto toCourseListDto(Course course) {
        return CourseListDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .thumbnailUrl(course.getThumbnailUrl())
                .price(course.getPrice())
                .level(course.getLevel())
                .instructorFirstName(course.getInstructor() != null ? course.getInstructor().getFirstName() : null)
                .instructorLastName(course.getInstructor() != null ? course.getInstructor().getLastName() : null)
                .instructorId(course.getInstructor() != null ? course.getInstructor().getId() : null)
                .categoryId(course.getCategory() != null ? course.getCategory().getId() : null)
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)
                .averageRating(course.getAverageRating())
                .ratingCount((long) course.getRatingCount())
                .totalLessonsCount((long) course.getTotalLessonsCount())
                .enrollmentsCount((long) course.getEnrollmentsCount())
                .totalDurationSeconds(course.getTotalDurationSeconds())
                .build();
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findByIdWithEnrollmentAndLessonProgress(Integer courseId) {
        return courseRepository.findByIdWithEnrollmentAndLessonProgress(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học với id " + courseId));
    }

    public Course findById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học có id " + courseId));
    }

    public String getThumbnailUrl(Course course) {
        String thumbnailUrl = course.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            return AppConstants.AZURE_STORAGE_BASE_URL + "/" + AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS
                    + "/" + thumbnailUrl;
        }
        return null;
    }

    public Course findByCourseIdAndUserId(Integer courseId, Integer userId) {
        Course course = courseRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng chưa mua khóa học này"));
        if (CourseStatus.DRAFT.equals(course.getStatus())) {
            throw new ResourceNotFoundException("Khóa học không tồn tại hoặc chưa được xuất bản.");
        }
        return course;
    }

    public CourseSubmitReviewDto getSubmitReview(Integer courseId, User user, boolean acceptedPolicy) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học có id " + courseId));

        CourseSubmitReviewDto dto = new CourseSubmitReviewDto();
        dto.setCourseId(course.getId());
        dto.setCourseTitle(course.getTitle());
        dto.setCourseStatus(course.getStatus());

        boolean owner = user != null
                && course.getInstructor() != null
                && course.getInstructor().getId().equals(user.getId());
        boolean editableStatus = course.getStatus() == CourseStatus.DRAFT
                || course.getStatus() == CourseStatus.REJECTED
                || course.getStatus() == CourseStatus.RESUBMIT;
        boolean noPendingRequest = course.getStatus() != CourseStatus.PENDING;

        long sectionCount = courseRepository.countSectionsByCourseId(courseId);
        long lessonCount = courseRepository.countLessonsByCourseId(courseId);
        long videoLessonCount = courseRepository.countLessonsHavingVideoByCourseId(courseId);
        long materialCount = courseRepository.countMaterialsByCourseId(courseId);
        long quizCount = courseRepository.countQuizzesByCourseId(courseId);

        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Tiêu đề khóa học: 10-120 ký tự",
                hasLengthBetween(course.getTitle(), 10, 120),
                "Tiêu đề khóa học phải có từ 10 đến 120 ký tự");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Mô tả khóa học: tối thiểu 50 ký tự",
                plainLength(course.getDescription()) >= 50,
                "Mô tả khóa học cần tối thiểu 50 ký tự");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Ảnh đại diện khóa học đã tải lên",
                hasText(course.getThumbnailUrl()),
                "Chưa tải ảnh đại diện khóa học");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Danh mục, trình độ và giá đã điền",
                course.getCategory() != null && hasText(course.getLevel()) && course.getPrice() != null,
                "Chưa điền đủ danh mục, trình độ hoặc giá khóa học");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Có ít nhất 1 section",
                sectionCount > 0,
                "Khóa học cần có ít nhất 1 section");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Mỗi section có ít nhất 1 bài học",
                sectionCount > 0 && courseRepository.countSectionsWithoutLessons(courseId) == 0,
                "Mỗi section cần có ít nhất 1 bài học");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Có ít nhất 1 video bài học",
                videoLessonCount > 0,
                "Thiếu video bài học");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Có ít nhất 1 tài liệu học tập",
                materialCount > 0,
                "Thiếu tài liệu học tập");
        addCheck(dto.getContentChecks(), dto.getMissingMessages(),
                "Có ít nhất 1 quiz / bài tập cuối khóa",
                quizCount > 0,
                "Thiếu quiz / bài tập cuối khóa");

        addCheck(dto.getBusinessChecks(), dto.getMissingMessages(),
                "Người gửi là giảng viên sở hữu khóa học",
                owner,
                "Bạn không phải giảng viên sở hữu khóa học này");
        addCheck(dto.getBusinessChecks(), dto.getMissingMessages(),
                "Khóa học ở trạng thái Draft, Rejected hoặc Resubmit",
                editableStatus,
                "Chỉ khóa học bản nháp, bị từ chối hoặc duyệt lại mới được gửi duyệt");
        addCheck(dto.getBusinessChecks(), dto.getMissingMessages(),
                "Không tồn tại request đang chờ duyệt",
                noPendingRequest,
                "Khóa học đang chờ duyệt, không thể gửi thêm request");
        addCheck(dto.getBusinessChecks(), dto.getMissingMessages(),
                "Manager / bộ phận duyệt được gán tự động",
                true,
                null);

        CourseSubmitReviewDto.CheckItem policy = new CourseSubmitReviewDto.CheckItem(
                "Đã tick cam kết bản quyền và chính sách",
                acceptedPolicy,
                true);
        dto.getBusinessChecks().add(policy);
        if (!acceptedPolicy) {
            dto.getMissingMessages().add("Chưa tick cam kết bản quyền và chính sách");
        }

        int total = dto.getContentChecks().size() + dto.getBusinessChecks().size();
        int completed = 0;
        for (CourseSubmitReviewDto.CheckItem item : dto.getContentChecks()) {
            if (item.isPassed())
                completed++;
        }
        for (CourseSubmitReviewDto.CheckItem item : dto.getBusinessChecks()) {
            if (item.isPassed())
                completed++;
        }

        dto.setTotalCount(total);
        dto.setCompletedCount(completed);
        dto.setPercent(total == 0 ? 0 : (int) Math.round(completed * 100.0 / total));
        dto.setSubmitReady(completed == total);
        return dto;
    }

    public void submitCourseForApproval(Integer courseId, User user, boolean acceptedPolicy) {
        CourseSubmitReviewDto review = getSubmitReview(courseId, user, acceptedPolicy);
        if (!review.isSubmitReady()) {
            throw new CourseValidationException("submitReview", String.join("; ", review.getMissingMessages()));
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học có id " + courseId));
        if (course.getStatus() == CourseStatus.REJECTED || course.getStatus() == CourseStatus.RESUBMIT) {
            course.setStatus(CourseStatus.RESUBMIT);
        } else {
            course.setStatus(CourseStatus.PENDING);
        }
        course.setRejectionReason(null);
        course.setApprovedAt(null);
        course.setApprovedBy(null);
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
    }

    private void addCheck(List<CourseSubmitReviewDto.CheckItem> checks, List<String> missingMessages,
            String label, boolean passed, String missingMessage) {
        checks.add(new CourseSubmitReviewDto.CheckItem(label, passed));
        if (!passed && missingMessage != null) {
            missingMessages.add(missingMessage);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean hasLengthBetween(String value, int min, int max) {
        if (!hasText(value)) {
            return false;
        }
        int length = value.trim().length();
        return length >= min && length <= max;
    }

    private int plainLength(String value) {
        if (value == null) {
            return 0;
        }
        return value.replaceAll("<[^>]*>", "").trim().length();
    }

    public HomeDto getHomeData(User currentUser) {
        long totalCourses = courseRepository.countByStatus(CourseStatus.PUBLISHED);
        long totalInstructors = userRepository.countInstructors();
        long totalLearners = userRepository.countLearners();

        long totalFeedbacks = feedbackRepository.count();
        int ratingPercent = 98;
        if (totalFeedbacks > 0) {
            long highRatingFeedbacks = feedbackRepository.countByRatingGreaterThanEqual(4);
            ratingPercent = (int) Math.round((double) highRatingFeedbacks / totalFeedbacks * 100);
        }

        HomeDto.HomeDtoBuilder builder = HomeDto.builder()
                .totalCourses(totalCourses)
                .totalInstructors(totalInstructors)
                .totalLearners(totalLearners)
                .fiveStarRatingPercent(ratingPercent);

        if (currentUser == null) {
            return builder.hasFavorites(false).build();
        }

        User user = userRepository.findById(currentUser.getId()).orElse(currentUser);
        if (!user.isFavoriteSetupCompleted()) {
            return builder.hasFavorites(false).build();
        }

        Set<vn.edu.fpt.entity.UserFavoriteCategory> favoriteCategories = user.getFavoriteCategories();
        if (favoriteCategories == null || favoriteCategories.isEmpty()) {
            return builder.hasFavorites(false).build();
        }

        List<CategoryDto> favoriteChildren = new java.util.ArrayList<>();
        for (vn.edu.fpt.entity.UserFavoriteCategory ufc : favoriteCategories) {
            Category c = ufc.getCategory();
            if ("ACTIVE".equals(c.getStatus()) && c.getParent() != null) {
                favoriteChildren.add(dtoMapper.toCategoryDto(c));
            }
        }

        if (favoriteChildren.isEmpty()) {
            return builder.hasFavorites(false).build();
        }

        Category parent = null;
        for (vn.edu.fpt.entity.UserFavoriteCategory ufc : favoriteCategories) {
            Category c = ufc.getCategory();
            if (c.getParent() != null) {
                parent = c.getParent();
                break;
            }
        }

        if (parent == null) {
            return builder.hasFavorites(false).build();
        }

        CategoryDto parentCategoryDto = dtoMapper.toCategoryDto(parent);

        Map<Integer, List<CourseListDto>> coursesMap = new HashMap<>();


        for (CategoryDto child : favoriteChildren) {
            List<CourseListDto> top4 = courseRepository.findTop4ByCategoryIdsOrderByAverageRatingDesc(
                    java.util.Collections.singletonList(child.getId()),
                    PageRequest.of(0, 4));
            coursesMap.put(child.getId(), top4);
        }

        return builder
                .hasFavorites(true)
                .parentCategory(parentCategoryDto)
                .favoriteChildren(favoriteChildren)
                .coursesMap(coursesMap)
                .build();
    }

    public CourseContentSidebarDTO viewCourseContent(User user, Integer courseId, Integer sectionId, Integer lessonId) {
        CourseContentSidebarDTO courseContentSidebarDTO = new CourseContentSidebarDTO();

        Course course = courseRepository.findByCourseIdAndUserId(courseId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Người dùng chưa mua khóa học này hoặc khóa học không tồn tại trong hệ thống!"));

        List<SectionSiderbarDTO> sectionSiderbarDTOS = courseSectionRepository
                .findSectionSiderbarDTOByCourseId(courseId);

        Set<Integer> lessonIdCompleted = lessonProgressRepository.findByUserIdAndCourseId(user.getId(), courseId);

        int totalLesson = 0;
        String currentLessonTitle = "";

        for (SectionSiderbarDTO dto : sectionSiderbarDTOS) {
            List<LessonSiderbarDTO> lessonSiderbarDTOS = lessonRepository.findLessonBySecionId(dto.getId());
            totalLesson += lessonSiderbarDTOS.size();
            boolean sectionCompleted = true;
            for (LessonSiderbarDTO lessonSiderbarDTO : lessonSiderbarDTOS) {
                if (lessonSiderbarDTO.getId().equals(lessonId)) {
                    lessonSiderbarDTO.setCurrentLesson(true);
                    currentLessonTitle = lessonSiderbarDTO.getTitle();
                }
                if (lessonIdCompleted.contains(lessonSiderbarDTO.getId())) {
                    lessonSiderbarDTO.setCompleted(true);
                } else {
                    sectionCompleted = false;
                }
            }
            dto.setCompleted(sectionCompleted);
            dto.setTotalLessonBySection(lessonSiderbarDTOS.size());
            dto.setLessons(lessonSiderbarDTOS);
        }

        courseContentSidebarDTO.setSections(sectionSiderbarDTOS);
        courseContentSidebarDTO.setTotalLesson(totalLesson);
        courseContentSidebarDTO.setCompletedLesson(lessonIdCompleted.size());
        courseContentSidebarDTO.setCurrentLessonId(lessonId);
        courseContentSidebarDTO.setCurrentLessonTitle(currentLessonTitle);
        courseContentSidebarDTO.setCourseId(courseId);
        courseContentSidebarDTO.setThumbanailURL(AppConstants.AZURE_STORAGE_BASE_URL + "/"
                + AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/" + course.getThumbnailUrl());

        Lesson nextLesson = lessonService.findNextLessonByCurrentLesson(lessonId, courseId, totalLesson,
                lessonIdCompleted.size());
        if (nextLesson != null) {
            courseContentSidebarDTO.setNextLessonId(nextLesson.getId());
            courseContentSidebarDTO.setNextLessonTitle(nextLesson.getTitle());
            if (nextLesson.getCourseSection() != null) {
                courseContentSidebarDTO.setNextSectionId(nextLesson.getCourseSection().getId());
            }
        }

        double progressVal = 0.0;
        if (totalLesson != 0 && totalLesson > 0 && lessonIdCompleted != null) {
            progressVal = ((double) lessonIdCompleted.size() * 100.0) / totalLesson;
        }
        courseContentSidebarDTO.setProgressPercent(progressVal);

        Lesson currentLesson = lessonService.findByIdWithMaterials(lessonId);
        if (currentLesson != null) {
            courseContentSidebarDTO.setLesson(dtoMapper.toLessonDto(currentLesson));
            List<LessonMaterialDto> materialDtos = new ArrayList<>();
            if (currentLesson.getMaterials() != null) {
                for (LessonMaterial m : currentLesson.getMaterials()) {
                    materialDtos.add(dtoMapper.toLessonMaterialDto(m));
                }
            }
            courseContentSidebarDTO.setMaterials(materialDtos);
        }

        courseContentSidebarDTO.setCourseDescription(course.getDescription());
        courseContentSidebarDTO.setLessonProgressStatus(lessonIdCompleted.contains(lessonId));
        courseContentSidebarDTO.setCurrentSectionId(sectionId);

        boolean showReviewPrompt = false;
        if (progressVal >= AppConstants.PERCENT_COMPLETED_LESSON_TO_COMMENT) {
            boolean hasReviewed = feedbackService.hasUserReviewedCourse(user.getId(), courseId);
            if (!hasReviewed) {
                showReviewPrompt = true;
            }
        }

        courseContentSidebarDTO.setShowReviewPrompt(showReviewPrompt);

        // tải ghi chú của tôi
        List<LessonNoteSiderbarDTO> lessonNoteSiderbarDTOS = lessonNoteService
                .findLessonNoteByUserIdAndLessonId(user.getId(), lessonId);

        courseContentSidebarDTO.setLessonNoteSiderbarDTOS(lessonNoteSiderbarDTOS);
        return courseContentSidebarDTO;
    }

    public boolean canUserReviewCourse(User user, Integer courseId) {
        if (user == null || courseId == null) {
            return false;
        }
        Optional<Course> courseOpt = courseRepository.findByCourseIdAndUserId(courseId, user.getId());
        if (courseOpt.isEmpty()) {
            return false;
        }
        List<SectionSiderbarDTO> sectionSiderbarDTOS = courseSectionRepository
                .findSectionSiderbarDTOByCourseId(courseId);
        Set<Integer> lessonIdCompleted = lessonProgressRepository.findByUserIdAndCourseId(user.getId(), courseId);
        int totalLesson = 0;
        for (SectionSiderbarDTO dto : sectionSiderbarDTOS) {
            List<LessonSiderbarDTO> lessonSiderbarDTOS = lessonRepository.findLessonBySecionId(dto.getId());
            totalLesson += lessonSiderbarDTOS.size();
        }
        double progressVal = 0.0;
        if (totalLesson > 0 && lessonIdCompleted != null) {
            progressVal = ((double) lessonIdCompleted.size() * 100.0) / totalLesson;
        }
        return progressVal >= AppConstants.PERCENT_COMPLETED_LESSON_TO_COMMENT;
    }

    public Feedback addCourseReview(User user, Integer courseId, Integer rating, String comment) {
        if (comment != null && comment.trim().length() > 500) {
            throw new IllegalArgumentException("Nội dung nhận xét không được vượt quá 500 ký tự.");
        }

        if (feedbackService.hasUserReviewedCourse(user.getId(), courseId)) {
            throw new IllegalArgumentException("Bạn đã gửi đánh giá cho khóa học này trước đó rồi!");
        }

        if (!canUserReviewCourse(user, courseId)) {
            throw new IllegalArgumentException(
                    "Bạn cần hoàn thành ít nhất 30% tiến trình bài học để đánh giá khóa học này!");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Không tìm thấy khóa học"));

        Feedback feedback = Feedback.builder()
                .user(user)
                .course(course)
                .rating(rating)
                .comment(comment)
                .status(FeedbackStatus.VISIBLE)
                .createdAt(LocalDateTime.now())
                .build();

        return feedbackRepository.save(feedback);
    }

    public Map<String, Object> addCourseReviewAndBuildData(User user, Integer courseId, Integer rating,
            String comment) {
        Feedback feedback = addCourseReview(user, courseId, rating, comment);

        Map<String, Object> fbData = new HashMap<>();
        fbData.put("id", feedback.getId());
        fbData.put("rating", feedback.getRating());
        fbData.put("comment", feedback.getComment());
        fbData.put("createdAt", feedback.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("avatarUrl", user.getFullAvatarUrl());
        fbData.put("user", userData);

        return fbData;
    }

    public void editCourseReview(Integer feedbackId, Integer rating, String comment, User user) {
        feedbackService.updateReview(feedbackId, rating, comment, user);
    }

    public void deleteCourseReview(Integer feedbackId, User user) {
        feedbackService.deleteReview(feedbackId, user);
    }

    public boolean hasUserReviewedCourse(Integer userId, Integer courseId) {
        return feedbackService.hasUserReviewedCourse(userId, courseId);
    }

    public List<CourseGrantDTO> findAllCourseGrant() {
        return courseRepository.findAllCourseGrantDTO();
    }

    public List<CourseGrantDTO> findAvailableCoursesForUser(Integer userId) {
        return courseRepository.findAvailablePublishedCoursesForUser(userId);
    }
}
