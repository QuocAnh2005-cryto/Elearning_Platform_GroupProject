package vn.edu.fpt.service.cloud;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.service.section.CourseSectionService;
import vn.edu.fpt.util.AppConstants;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoUploadService {

    private final CourseSectionService courseSectionService;
    private final CourseRepository courseRepository;
    private final AzureBlobService azureBlobService;

    public Map<String, String> generateDirectUploadUrl(String fileName, Integer sectionId, User user) {
        validateInstructorOwnsSection(sectionId, user);
        String extension = ".mp4";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        String blobName = UUID.randomUUID().toString() + extension;
        String uploadUrl = azureBlobService.generateUploadSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, blobName);
        return Map.of(
                "uploadUrl", uploadUrl,
                "blobName", blobName
        );
    }

    public Map<String, String> generateCourseIntroUploadUrl(String fileName, Integer courseId, User user) {
        validateCourseIntroUploadPermission(courseId, user);
        String extension = ".mp4";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        String blobName = UUID.randomUUID().toString() + extension;
        String uploadUrl = azureBlobService.generateUploadSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, blobName);
        return Map.of(
                "uploadUrl", uploadUrl,
                "blobName", blobName
        );
    }

    private String normalizeOriginalFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "lesson-video.mp4";
        }
        return fileName.trim();
    }

    private void validateInstructorOwnsSection(Integer sectionId, User user) {
        if (user == null) {
            throw new AccessDeniedException("Ban can dang nhap de tai video len.");
        }

        CourseSection section = courseSectionService.findBySectionId(sectionId);
        if (section == null
                || section.getCourse() == null
                || section.getCourse().getInstructor() == null
                || !section.getCourse().getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Ban khong co quyen tai video len chuong nay.");
        }
    }

    private void validateCourseIntroUploadPermission(Integer courseId, User user) {
        if (user == null) {
            throw new AccessDeniedException("Ban can dang nhap de tai video gioi thieu.");
        }
        
        if (courseId == null) {
            return;
        }

        boolean ownsCourse = courseRepository.findById(courseId)
                .map(course -> course.getInstructor() != null
                        && course.getInstructor().getId().equals(user.getId()))
                .orElse(false);
        if (!ownsCourse) {
            throw new AccessDeniedException("Ban khong co quyen tai video cho khoa hoc nay.");
        }
    }

    public Map<String, String> generateMaterialUploadUrl(String fileName, User user) {
        if (user == null) {
            throw new AccessDeniedException("Ban can dang nhap de tai tai lieu.");
        }
        String extension = ".pdf";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        String blobName = UUID.randomUUID().toString() + extension;
        String uploadUrl = azureBlobService.generateUploadSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS, blobName);
        return Map.of(
                "uploadUrl", uploadUrl,
                "blobName", blobName
        );
    }
}
