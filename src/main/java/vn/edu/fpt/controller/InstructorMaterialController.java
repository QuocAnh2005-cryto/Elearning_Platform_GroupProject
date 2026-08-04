
package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.material.LessonMaterialService;
import vn.edu.fpt.service.cloud.VideoUploadService;
import vn.edu.fpt.util.SecurityUtils;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor/materials")
public class InstructorMaterialController {
    private final LessonMaterialService lessonMaterialService;
    private final VideoUploadService videoUploadService;

    @PostMapping("/lesson/{lessonId}")
    public String createMaterial(@RequestParam(value = "source", defaultValue = "create") String source,
                                 @RequestParam("courseId") Integer courseId,
                                 @PathVariable("lessonId") Integer lessonId,
                                 @RequestParam(value = "materialBlobNames", required = false) List<String> blobNames,
                                 @RequestParam(value = "materialFileNames", required = false) List<String> fileNames,
                                 @RequestParam(value = "materialFileSizes", required = false) List<Long> fileSizes,
                                 RedirectAttributes redirectAttributes) {
        User user = SecurityUtils.getCurrentUser();
        try {
            lessonMaterialService.saveAllMaterialDirect(blobNames, fileNames, fileSizes, lessonId, user);
            redirectAttributes.addFlashAttribute("success", "Thêm tài liệu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm tài liệu: " + e.getMessage());
        }
        return redirectAfterCurriculumAction(source, courseId);
    }

    @PostMapping("/upload-url")
    @ResponseBody
    public Map<String, String> getMaterialUploadUrl(@RequestParam("fileName") String fileName) {
        User user = SecurityUtils.getCurrentUser();
        return videoUploadService.generateMaterialUploadUrl(fileName, user);
    }

    @PostMapping("/{materialId}/delete")
    public String deleteMaterial(
                                  @RequestParam(value = "source", defaultValue = "create") String source,
                                  @PathVariable("materialId") Integer materialId,
                                  @RequestParam("courseId") Integer courseId,
                                  RedirectAttributes redirectAttributes) {
        try {
            lessonMaterialService.deleteMaterialById(materialId);
            redirectAttributes.addFlashAttribute("success", "Xoá tài liệu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xoá tài liệu: " + e.getMessage());
        }
        return redirectAfterCurriculumAction(source, courseId);
    }

    private String redirectAfterCurriculumAction(String source, Integer courseId) {
        return "edit".equals(source)
                ? "redirect:/instructor/" + courseId + "/edit"
                : "redirect:/instructor/" + courseId + "/curriculum";
    }
}
