package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.EnrollmentDto;
import vn.edu.fpt.dto.user.StudentLearningDto;
import vn.edu.fpt.entity.Enrollment;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.EnrollmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyCourseService {

    private final EnrollmentRepository enrollmentRepository;
    private final DtoMapper dtoMapper;

    public StudentLearningDto getLearningData(User user, String filter, int page) {
        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            EnrollmentDto dto = dtoMapper.toEnrollmentDto(enrollment);
            if ("incomplete".equalsIgnoreCase(filter)) {
                if (dto.getProgressPercent() != null && dto.getProgressPercent().doubleValue() >= 100.0) {
                    continue;
                }
            }
            enrollmentDtos.add(dto);
        }

        int pageSize = 6;
        int totalItems = enrollmentDtos.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }

        int currentPage = Math.max(1, Math.min(page, totalPages));
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        List<EnrollmentDto> pagedEnrollments = new ArrayList<>();
        if (fromIndex < totalItems) {
            pagedEnrollments = enrollmentDtos.subList(fromIndex, toIndex);
        }

        return StudentLearningDto.builder()
                .currentUser(dtoMapper.toUserDto(user))
                .enrollments(pagedEnrollments)
                .enrollmentsCount(totalItems)
                .filter(filter)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .build();
    }
}
