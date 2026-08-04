package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Feedback;
import vn.edu.fpt.repository.FeedbackRepository;

import vn.edu.fpt.entity.User;
import vn.edu.fpt.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {
    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.repository = feedbackRepository;
    }

    public List<Feedback> findAll() { return repository.findAll(); }
    public Optional<Feedback> findById(Integer id) { return repository.findById(id); }
    public Feedback save(Feedback entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
    public boolean hasUserReviewedCourse(Integer userId, Integer courseId) {
        return repository.existsByUserIdAndCourseId(userId, courseId);
    }
    public Optional<Feedback> findByUserIdAndCourseId(Integer userId, Integer courseId) {
        return repository.findByUserIdAndCourseId(userId, courseId);
    }

    public void updateReview(Integer feedbackId, Integer rating, String comment, User user) {
        if (comment != null && comment.trim().length() > 500) {
            throw new IllegalArgumentException("Nội dung nhận xét không được vượt quá 500 ký tự.");
        }

        Feedback feedback = repository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Đánh giá không tồn tại"));

        if (user == null || !feedback.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền sửa đánh giá này!");
        }

        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(java.time.LocalDateTime.now());
        repository.save(feedback);
    }

    public void deleteReview(Integer feedbackId, User user) {
        Feedback feedback = repository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Đánh giá không tồn tại"));

        if (user == null || !feedback.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa đánh giá này!");
        }

        repository.delete(feedback);
    }
}

