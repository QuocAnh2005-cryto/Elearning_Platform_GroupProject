package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import vn.edu.fpt.dto.course.CourseListDto;
import vn.edu.fpt.dto.instructor.InstructorPublicProfileDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.util.AppConstants;

import java.util.List;

@Service
public class InstructorPublicProfileService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final DtoMapper dtoMapper;

    public InstructorPublicProfileService(UserRepository userRepository, CourseRepository courseRepository, DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.dtoMapper = dtoMapper;
    }

    public InstructorPublicProfileDto getInstructorProfile(Integer id) {
        User instructor = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));

        // 1. Fetch courses with pre-calculated stats (1 query)
        List<CourseListDto> courseDtos = courseRepository.getInstructorCoursesWithStats(id);

        long totalStudents = 0;
        double totalRatingSum = 0;
        
        // Sum up total students
        for (CourseListDto dto : courseDtos) {
            totalStudents += dto.getEnrollmentsCount();
        }

        // 2. Fetch all ratings for instructor to calculate distribution (1 query)
        List<Integer> allRatings = courseRepository.findAllFeedbackRatingsForInstructor(id);
        int totalReviews = allRatings.size();
        
        int count5 = 0, count4 = 0, count3 = 0, count2 = 0, count1 = 0;
        
        for (Integer rating : allRatings) {
            totalRatingSum += rating;
            switch (rating) {
                case 5: count5++; break;
                case 4: count4++; break;
                case 3: count3++; break;
                case 2: count2++; break;
                case 1: count1++; break;
            }
        }

        double averageRating = totalReviews > 0 ? totalRatingSum / totalReviews : 0.0;
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        int percent5 = totalReviews > 0 ? (int) Math.round(count5 * 100.0 / totalReviews) : 0;
        int percent4 = totalReviews > 0 ? (int) Math.round(count4 * 100.0 / totalReviews) : 0;
        int percent3 = totalReviews > 0 ? (int) Math.round(count3 * 100.0 / totalReviews) : 0;
        int percent2 = totalReviews > 0 ? (int) Math.round(count2 * 100.0 / totalReviews) : 0;
        int percent1 = totalReviews > 0 ? (int) Math.round(count1 * 100.0 / totalReviews) : 0;

        return dtoMapper.toInstructorPublicProfileDto(
                instructor,
                averageRating,
                totalReviews,
                (int) totalStudents,
                courseDtos.size(),
                percent5,
                percent4,
                percent3,
                percent2,
                percent1,
                courseDtos
        );
    }

    private String resolveThumbnailPath(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.trim().isEmpty()) {
            return "/images/course_thumbnail.png";
        }
        
        if (thumbnailUrl.startsWith("http://") || thumbnailUrl.startsWith("https://")) {
            return thumbnailUrl;
        }

        if (thumbnailUrl.startsWith(AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/")) {
            return AppConstants.AZURE_STORAGE_BASE_URL + "/" + thumbnailUrl;
        }

        return AppConstants.AZURE_STORAGE_BASE_URL + "/" + 
               AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/" + 
               thumbnailUrl;
    }
}
