package vn.edu.fpt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.fpt.dto.*;
import vn.edu.fpt.dto.cart.CartDto;
import vn.edu.fpt.dto.cart.CartItemDto;
import vn.edu.fpt.dto.cart.OrderDto;
import vn.edu.fpt.dto.course.CategoryDto;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.dto.course.CourseListDto;
import vn.edu.fpt.dto.course.FeedbackDto;
import vn.edu.fpt.dto.course.OrderItemDto;
import vn.edu.fpt.dto.instructor.InstructorPublicProfileDto;
import vn.edu.fpt.dto.lesson.LessonNoteSiderbarDTO;
import vn.edu.fpt.dto.quizdto.QuizAnswerDTO;
import vn.edu.fpt.dto.quizdto.QuizAttemptAnswerDTO;
import vn.edu.fpt.dto.quizdto.QuizAttemptDTO;
import vn.edu.fpt.dto.quizdto.QuizDTO;
import vn.edu.fpt.dto.quizdto.QuizQuestionDTO;
import vn.edu.fpt.dto.user.LearnerInfomationGrantAccessDTO;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.entity.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(target = "avatarUrl", expression = "java(user.getFullAvatarUrl())")
    @Mapping(target = "courseCount", expression = "java(user.getCourses() != null ? user.getCourses().size() : 0)")
    @Mapping(target = "hasPassword", expression = "java(user.getPasswordHash() != null && !user.getPasswordHash().isEmpty())")
    UserDto toUserDto(User user);

    @Named("toInstructorListDto")
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "courseCount", ignore = true)
    @Mapping(target = "hasPassword", ignore = true)
    UserDto toInstructorListDto(User user);

    @Named("toSimpleUserDto")
    @Mapping(target = "avatarUrl", expression = "java(user.getFullAvatarUrl())")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "courseCount", expression = "java(user.getCourses() != null ? user.getCourses().size() : 0)")
    @Mapping(target = "hasPassword", expression = "java(user.getPasswordHash() != null && !user.getPasswordHash().isEmpty())")
    UserDto toSimpleUserDto(User user);

    @Named("toVerySimpleUserDto")
    @Mapping(target = "avatarUrl", expression = "java(user.getFullAvatarUrl())")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "courseCount", ignore = true)
    UserDto toVerySimpleUserDto(User user);

    @Mapping(target = "averageRating", expression = "java(course.getAverageRating())")
    @Mapping(target = "ratingCount", expression = "java(course.getRatingCount())")
    @Mapping(target = "totalLessonsCount", expression = "java(course.getTotalLessonsCount())")
    @Mapping(target = "firstLessonVideoUrl", expression = "java(course.getFirstLessonVideoUrl())")
    @Mapping(target = "firstLessonId", expression = "java(course.getFirstLessonId())")
    @Mapping(target = "thumbnailPath", expression = "java(course.getThumbnailPath())")
    @Mapping(target = "rejectionReason", source = "rejectionReason")
    @Mapping(target = "category", qualifiedByName = "toSimpleCategoryDto")
    @Mapping(target = "instructor", qualifiedByName = "toSimpleUserDto")
    CourseDto toCourseDto(Course course);

    @Named("toSimpleCourseDto")
    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "totalLessonsCount", ignore = true)
    @Mapping(target = "firstLessonVideoUrl", ignore = true)
    @Mapping(target = "firstLessonId", ignore = true)
    @Mapping(target = "enrollmentsCount", ignore = true)
    @Mapping(target = "category", qualifiedByName = "toSimpleCategoryDto")
    @Mapping(target = "instructor", qualifiedByName = "toVerySimpleUserDto")
    @Mapping(target = "thumbnailPath", expression = "java(course.getThumbnailPath())")
    @Mapping(target = "rejectionReason", source = "rejectionReason")
    CourseDto toSimpleCourseDto(Course course);

    @Mapping(target = "courseCount", expression = "java(category.getCourses() != null ? category.getCourses().size() : 0)")
    @Mapping(
            target = "parentId",
            expression = "java(category.getParent() != null ? category.getParent().getId() : null)"
    )
    CategoryDto toCategoryDto(Category category);

    @Named("toSimpleCategoryDto")
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "courseCount", ignore = true)
    @Mapping(target = "parentId", expression = "java(category.getParent() != null ? category.getParent().getId() : null)")
    CategoryDto toSimpleCategoryDto(Category category);

    @Mapping(source = "lesson.id", target = "lessonId")
    QuizDTO toQuizDto(Quiz quiz);

    List<QuizDTO> toQuizDtos(List<Quiz> quizzes);

    QuizQuestionDTO toQuizQuestionDto(QuizQuestion quizQuestion);

    QuizAnswerDTO toQuizAnswerDto(QuizAnswer quizAnswer);

    QuizAttemptDTO toQuizAttemptDto(QuizAttempt quizAttempt);

    @Mapping(source = "attempt.id", target = "attemptId")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "selectedAnswer.id", target = "selectedAnswerId")
    QuizAttemptAnswerDTO toQuizAttemptAnswerDto(QuizAttemptAnswer quizAttemptAnswer);

    List<QuizAttemptAnswerDTO> toQuizAttemptAnswerDtos(List<QuizAttemptAnswer> quizAttemptAnswers);

    CourseSectionDto toCourseSectionDto(CourseSection section);
    LessonDto toLessonDto(Lesson lesson);

    @Mapping(target = "user", qualifiedByName = "toSimpleUserDto")
    FeedbackDto toFeedbackDto(Feedback feedback);

    EnrollmentDto toEnrollmentDto(Enrollment enrollment);

    @Mapping(target = "user", qualifiedByName = "toSimpleUserDto")
    CartDto toCartDto(Cart cart);

    @Mapping(target = "course", qualifiedByName = "toSimpleCourseDto")
    CartItemDto toCartItemDto(CartItem cartItem);

    OrderDto toOrderDto(Order order);
    OrderItemDto toOrderItemDto(OrderItem orderItem);
    LessonMaterialDto toLessonMaterialDto(LessonMaterial lessonMaterial);

    @Mapping(target = "videoTimeSeconds", source = "videoTimeSeconds")
    @Mapping(target = "noteContent", source = "noteContent")
    @Mapping(target = "lessonTitle", source = "lesson.title")
    @Mapping(target = "lessonId", source = "lesson.id")
    LessonNoteSiderbarDTO toLessonNoteSiderbarDto(LessonNote lessonNote);


    @Mapping(expression = "java(user.getFirstName() + \" \" + user.getLastName())", target = "fullName")
    @Mapping(source = "customAvatarUrl", target = "avatarUrl")
    @Mapping(source = "lastName", target = "lastName")
    LearnerInfomationGrantAccessDTO toLearnerInfomationGrantAccessDto(User user);

    Quiz toQuiz(QuizDTO quizDTO);

    @Mapping(target = "fullName", expression = "java(instructor.getLastName() + \" \" + instructor.getFirstName())")
    @Mapping(target = "avatarUrl", expression = "java(instructor.getFullAvatarUrl())")
    InstructorPublicProfileDto toInstructorPublicProfileDto(
            User instructor,
            double averageRating,
            int totalReviews,
            int totalStudents,
            int totalCourses,
            int percent5Stars,
            int percent4Stars,
            int percent3Stars,
            int percent2Stars,
            int percent1Stars,
            List<CourseListDto> courses);
}
