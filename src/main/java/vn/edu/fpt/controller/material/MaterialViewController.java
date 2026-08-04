package vn.edu.fpt.controller.material;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonMaterial;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.service.material.LessonMaterialService;
import vn.edu.fpt.service.lesson.LessonService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;

@Controller
@RequiredArgsConstructor
public class MaterialViewController {

    private final LessonMaterialService lessonMaterialService;

    private final AzureBlobService azureBlobService;

    private final LessonService lessonService;

    private final UserRepository userRepository;

    private User getSessionUser() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            return userRepository.findById(currentUser.getId()).orElse(currentUser);
        }
        return null;
    }

    @GetMapping("/material/{id}")
    @ResponseBody
    public String viewMaterial(@PathVariable Integer id, Model model) {
        return lessonMaterialService.getViewmaterial(id);
    }

    @GetMapping("/material/{id}/view")
    public String viewMaterialRedirect(@PathVariable Integer id) {
        return lessonMaterialService.viewMaterialRedirect(id);
    }

    @GetMapping("/lesson/{id}/video")
    public String viewLessonVideo(@PathVariable Integer id) {
        User user = getSessionUser();
        Lesson lesson = lessonService.findById(id).orElse(null);
        if (lesson == null) {
            return "redirect:/";
        }
        if (!lessonService.hasAccessToLesson(user, lesson)) {
            return "redirect:/course/" + lesson.getCourseSection().getCourse().getId();
        }
        String videoUrl = lessonService.findLessonUrl(lesson);
        return "redirect:" + videoUrl;
    }

    @GetMapping("/material/{id}/download")
    public ResponseEntity<InputStreamResource> downloadMaterial(@PathVariable Integer id) {
        return lessonMaterialService.dowloadFile(id);
    }

    @Transactional
    public void deleteMaterialById(Integer materialId) {
        if (materialId == null || materialId <= 0) {
            throw new RuntimeException("ID tài liệu không hợp lệ");
        }

        LessonMaterial material = lessonMaterialService.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu không tìm thấy với id: " + materialId));

        if (material.getFileUrl() != null && !material.getFileUrl().isEmpty()) {
            try {
                azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_MATERIALS, material.getFileUrl());
                System.out.println("✓ Xóa file material thành công: " + material.getFileUrl());
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Không thể xóa file: " + e.getMessage());
            }
        }

        // Xóa record từ DB
        lessonMaterialService.deleteById(materialId);
        System.out.println("Xóa material record thành công: " + materialId);
    }
}
