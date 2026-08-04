package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.material.LessonMaterialService;
import vn.edu.fpt.util.SecurityUtils;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorMaterialLibraryController {
    private final LessonMaterialService lessonMaterialService;

    @GetMapping("/materials")
    public String materialLibrary(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "courseId", required = false) Integer courseId,
                                  @RequestParam(value = "sectionId", required = false) Integer sectionId,
                                  @RequestParam(value = "lessonId", required = false) Integer lessonId,
                                  @RequestParam(value = "fileType", required = false) String fileType,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "8") int size,
                                  Model model) {
        User instructor = SecurityUtils.getCurrentUser();
        int currentPage = Math.max(page, 0);
        int currentSize = size > 0 ? size : 8;
        model.addAttribute("materialPage", lessonMaterialService.getInstructorMaterialLibrary(
                instructor, keyword, courseId, sectionId, lessonId, fileType, PageRequest.of(currentPage, currentSize)));
        model.addAttribute("courses", lessonMaterialService.getLibraryCourses(instructor));
        model.addAttribute("sections", lessonMaterialService.getLibrarySections(instructor, courseId));
        model.addAttribute("lessons", lessonMaterialService.getLibraryLessons(instructor, courseId, sectionId));
        model.addAttribute("fileTypes", lessonMaterialService.getLibraryFileTypes(instructor));
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedSectionId", sectionId);
        model.addAttribute("selectedLessonId", lessonId);
        model.addAttribute("selectedFileType", fileType);
        model.addAttribute("pageSize", currentSize);
        return "instructor_course/material_library";
    }

    @PostMapping("/materials/library/{materialId}/delete")
    public String deleteMaterialFromLibrary(@PathVariable("materialId") Integer materialId,
                                            RedirectAttributes redirectAttributes) {
        User instructor = SecurityUtils.getCurrentUser();
        try {
            lessonMaterialService.deleteInstructorLibraryMaterial(materialId, instructor);
            redirectAttributes.addFlashAttribute("success", "Xóa tài liệu thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/instructor/materials";
    }
}
