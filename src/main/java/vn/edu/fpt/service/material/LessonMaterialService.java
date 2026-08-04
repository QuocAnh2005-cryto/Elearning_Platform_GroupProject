package vn.edu.fpt.service.material;

import com.azure.storage.blob.BlobClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.dto.LessonMaterialDto;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonMaterial;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.repository.LessonMaterialRepository;
import vn.edu.fpt.repository.LessonRepository;
import vn.edu.fpt.repository.EnrollmentRepository;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonMaterialService {

    private static final Set<String> ALLOWED_MATERIAL_EXTENSIONS = Set.of(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    );
    private final LessonMaterialRepository repository;
    private final AzureBlobService azureBlobService;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;

    public boolean hasAccessToMaterial(User user, LessonMaterial material) {
        if (user == null || material == null || material.getLesson() == null ||
                material.getLesson().getCourseSection() == null) {
            return false;
        }
        Course course = material.getLesson().getCourseSection().getCourse();
        RoleType role = user.getRole();
        if (role == RoleType.ADMIN || role == RoleType.MANAGER) {
            return true;
        }
        if (role == RoleType.INSTRUCTOR) {
            return course.getInstructor() != null && course.getInstructor().getId().equals(user.getId());
        }
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }


    public void saveAllMaterial(List<MultipartFile> file, Integer lessonId, User user){

        if(file == null || file.isEmpty()) return;

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        for(MultipartFile f : file){
            if(f == null || f.isEmpty()) continue;

            String fileName = f.getOriginalFilename();
            String fileType = getValidatedMaterialExtension(fileName);
            Long fileSize = f.getSize();
            validateMaterialFileSize(fileSize);

            String url = azureBlobService.saveFile(f, "materials");
            LessonMaterial lessonMaterial = new LessonMaterial();
            lessonMaterial.setLesson(lesson);
            lessonMaterial.setCourse(lesson.getCourseSection() != null ? lesson.getCourseSection().getCourse() : null);
            lessonMaterial.setFileType(fileType);
            lessonMaterial.setFileName(fileName);
            lessonMaterial.setFileSize(fileSize);
            lessonMaterial.setFileUrl(url);
            lessonMaterial.setInstructor(user);
            lesson.setCreatedAt(LocalDateTime.now());

            repository.save(lessonMaterial);
        }
    }

    public void saveAllMaterialDirect(List<String> blobNames, List<String> fileNames, List<Long> fileSizes, Integer lessonId, User user) {
        if (blobNames == null || blobNames.isEmpty()) return;

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        for (int i = 0; i < blobNames.size(); i++) {
            String blobName = blobNames.get(i);
            if (blobName == null || blobName.isBlank()) continue;

            String fileName = fileNames.get(i);
            String fileType = getValidatedMaterialExtension(fileName);
            Long fileSize = fileSizes != null && i < fileSizes.size() ? fileSizes.get(i) : null;
            validateMaterialFileSize(fileSize);

            LessonMaterial lessonMaterial = new LessonMaterial();
            lessonMaterial.setLesson(lesson);
            lessonMaterial.setCourse(lesson.getCourseSection() != null ? lesson.getCourseSection().getCourse() : null);
            lessonMaterial.setFileType(fileType);
            lessonMaterial.setFileName(fileName);
            lessonMaterial.setFileSize(fileSize);
            lessonMaterial.setFileUrl(blobName);
            lessonMaterial.setInstructor(user);
            lesson.setCreatedAt(LocalDateTime.now());

            repository.save(lessonMaterial);
        }
    }

    public List<LessonMaterial> findAll() {
        return repository.findAll();
    }

    public Optional<LessonMaterial> findById(Integer id) {
        return repository.findById(id);
    }

    public LessonMaterial save(LessonMaterial entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Optional<LessonMaterial> findByLessonId(Integer lessonId) {
        return repository.findFirstByLesson_IdOrderByIdAsc(lessonId);
    }

    public List<LessonMaterialDto> getInstructorMaterialLibrary(User instructor) {
        return repository.findLibraryByInstructorId(instructor.getId())
                .stream()
                .map(this::toLibraryDto)
                .toList();
    }

    public Page<LessonMaterialDto> getInstructorMaterialLibrary(User instructor,
                                                                String keyword,
                                                                Integer courseId,
                                                                Integer sectionId,
                                                                Integer lessonId,
                                                                String fileType,
                                                                Pageable pageable) {
        return repository.searchLibraryByInstructorId(
                instructor.getId(),
                keyword != null ? keyword.trim() : "",
                courseId,
                sectionId,
                lessonId,
                fileType != null ? fileType.trim() : "",
                pageable
        ).map(this::toLibraryDto);
    }

    public List<Course> getLibraryCourses(User instructor) {
        return repository.findLibraryCoursesByInstructorId(instructor.getId());
    }

    public List<CourseSection> getLibrarySections(User instructor, Integer courseId) {
        return repository.findLibrarySectionsByInstructorId(instructor.getId(), courseId);
    }

    public List<Lesson> getLibraryLessons(User instructor, Integer courseId, Integer sectionId) {
        return repository.findLibraryLessonsByInstructorId(instructor.getId(), courseId, sectionId);
    }

    public List<String> getLibraryFileTypes(User instructor) {
        return repository.findLibraryFileTypesByInstructorId(instructor.getId());
    }

    private LessonMaterialDto toLibraryDto(LessonMaterial material) {
        Lesson lesson = material.getLesson();
        Course course = lesson != null && lesson.getCourseSection() != null
                ? lesson.getCourseSection().getCourse()
                : material.getCourse();
        CourseStatus status = course != null ? course.getStatus() : null;
        boolean deleteAllowed = canDeleteMaterial(course);

        return LessonMaterialDto.builder()
                .id(material.getId())
                .fileName(material.getFileName())
                .fileUrl(material.getFileUrl())
                .fileType(material.getFileType())
                .fileSize(material.getFileSize())
                .createdAt(material.getCreatedAt())
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : "Chua gan khoa hoc")
                .courseStatus(status != null ? status.name() : null)
                .courseStatusLabel(status != null ? status.getLabel() : "Chua co trang thai")
                .lessonId(lesson != null ? lesson.getId() : null)
                .lessonTitle(lesson != null ? lesson.getTitle() : "Chua gan bai hoc")
                .sectionTitle(lesson != null && lesson.getCourseSection() != null ? lesson.getCourseSection().getTitle() : "Chua gan chuong")
                .deleteAllowed(deleteAllowed)
                .deleteReason(getDeleteReason(course))
                .build();
    }

    private boolean canDeleteMaterial(Course course) {
        if (course == null || course.getStatus() == null) {
            return false;
        }
        if (course.getStatus() == CourseStatus.DRAFT || course.getStatus() == CourseStatus.REJECTED) {
            return true;
        }
        if (course.getStatus() == CourseStatus.HIDDEN) {
            return enrollmentRepository.countByCourseId(course.getId()) == 0;
        }
        return false;
    }

    private String getDeleteReason(Course course) {
        if (course == null || course.getStatus() == null) {
            return "Không xác định được trạng thái khóa học.";
        }
        if (course.getStatus() == CourseStatus.PENDING || course.getStatus() == CourseStatus.RESUBMIT) {
            return "Khóa học đang chờ duyệt hoặc duyệt lại, không được xóa tài liệu để tránh thay đổi nội dung duyệt.";
        }
        if (course.getStatus() == CourseStatus.PUBLISHED) {
            return "Khóa học đã xuất bản, học viên có thể đang sử dụng tài liệu.";
        }
        if (course.getStatus() == CourseStatus.HIDDEN && enrollmentRepository.countByCourseId(course.getId()) > 0) {
            return "Khóa học đã ẩn nhưng vẫn còn học viên có quyền truy cập.";
        }
        return "Có thể xóa tài liệu.";
    }

    public ResponseEntity<InputStreamResource> dowloadFile(Integer id) {
        User user = SecurityUtils.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        LessonMaterial lessonMaterial = findById(id).orElse(null);
        if (lessonMaterial == null) {
            return ResponseEntity.notFound().build();
        }
        if (!hasAccessToMaterial(user, lessonMaterial)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        BlobClient blobClient = azureBlobService.getBlobClient(
                AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS,
                lessonMaterial.getFileUrl()
        );
        return azureBlobService.dowloadFile(blobClient);
    }


    public void deleteMaterialById(Integer materialId) {
        if (materialId == null || materialId <= 0) {
            throw new RuntimeException("ID tài liệu không hợp lệ");
        }

        LessonMaterial material = repository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu không tìm thấy với id: " + materialId));
        Lesson lesson = material.getLesson();
        if (lesson != null) {
            lesson.removeMaterial(material);
        }
        if (material.getFileUrl() != null && !material.getFileUrl().isBlank()) {
            azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS, material.getFileUrl());
        }
        repository.delete(material);
    }

    public void deleteInstructorLibraryMaterial(Integer materialId, User instructor) {
        LessonMaterial material = repository.findOwnedMaterialForDelete(materialId, instructor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu không tồn tại hoặc không thuộc khóa học của bạn."));
        Course course = material.getLesson() != null && material.getLesson().getCourseSection() != null
                ? material.getLesson().getCourseSection().getCourse()
                : material.getCourse();
        if (!canDeleteMaterial(course)) {
            throw new RuntimeException(getDeleteReason(course));
        }
        deleteMaterialById(materialId);
    }

    public void updateMaterial(Integer materialId, List<MultipartFile> materialFile, User user) {
        LessonMaterial material = repository
                .findById(materialId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy tài liệu")
                );

        if (materialFile == null || materialFile.isEmpty()) {
            return;
        }

        for(MultipartFile file : materialFile){
            String fileName = file.getOriginalFilename();
            String fileType = getValidatedMaterialExtension(fileName);
            validateMaterialFileSize(file.getSize());
            String newFilePath = azureBlobService.saveFile(file, AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS);

            material.setFileName(fileName);
            material.setFileType(fileType);
            material.setFileSize(file.getSize());
            material.setFileUrl(newFilePath);
            material.setInstructor(user);
            material.setUpdatedAt(LocalDateTime.now());

            repository.save(material);
        }
    }

    private void validateMaterialFileSize(Long fileSize) {
        if (fileSize != null && fileSize > AppConstants.MAX_MATERIAL_FILE_SIZE_BYTES) {
            throw new RuntimeException("Dung lượng file tài liệu không được vượt quá 50MB (BR-35).");
        }
    }

    private String getValidatedMaterialExtension(String fileName) {
        if (fileName == null || fileName.isBlank() || !fileName.contains(".")) {
            throw new RuntimeException("File tai lieu phai co dinh dang: pdf, doc, docx, xls, xlsx, ppt, pptx.");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT)
                .trim();
        if (!ALLOWED_MATERIAL_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("Chi chap nhan material dang: pdf, doc, docx, xls, xlsx, ppt, pptx.");
        }
        return extension;
    }


    public String getViewmaterial(Integer id) {
        User user = SecurityUtils.getCurrentUser();
        if (user == null) {
            return null;
        }
        LessonMaterial lessonMaterial = findById(id).orElse(null);
        if (lessonMaterial == null) {
            return null;
        }
        if (!hasAccessToMaterial(user, lessonMaterial)) {
            return null;
        }
        String publicUrl = azureBlobService.generateSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS, lessonMaterial.getFileUrl());
        String fileType = lessonMaterial.getFileType() != null ? lessonMaterial.getFileType().toLowerCase().trim() : "";
        if ("pdf".equals(fileType)) {
            return publicUrl;
        } else if ("doc".equals(fileType) || "docx".equals(fileType) || "xls".equals(fileType) || "xlsx".equals(fileType) || "ppt".equals(fileType) || "pptx".equals(fileType)) {
            try {
                return AppConstants.OFFICE_VIEWER_BASE_URL + java.net.URLEncoder.encode(publicUrl, "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                return AppConstants.OFFICE_VIEWER_BASE_URL + publicUrl;
            }
        }
        return null;
    }

    public String viewMaterialRedirect(Integer id) {
        User user = SecurityUtils.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        LessonMaterial lessonMaterial = findById(id).orElse(null);
        if (lessonMaterial == null) {
            return "redirect:/";
        }
        if (!hasAccessToMaterial(user, lessonMaterial)) {
            return "redirect:/course/" + lessonMaterial.getLesson().getCourseSection().getCourse().getId();
        }
        String publicUrl = azureBlobService.generateSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS, lessonMaterial.getFileUrl());
        String fileType = lessonMaterial.getFileType() != null ? lessonMaterial.getFileType().toLowerCase().trim() : "";
        if ("pdf".equals(fileType)) {
            return "redirect:" + publicUrl;
        } else if ("doc".equals(fileType) || "docx".equals(fileType) || "xls".equals(fileType) || "xlsx".equals(fileType) || "ppt".equals(fileType) || "pptx".equals(fileType)) {
            try {
                return "redirect:" + AppConstants.OFFICE_VIEWER_BASE_URL + java.net.URLEncoder.encode(publicUrl, "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                return "redirect:" + AppConstants.OFFICE_VIEWER_BASE_URL + publicUrl;
            }
        }
        return "redirect:/material/" + id + "/download";
    }
}

