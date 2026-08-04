package vn.edu.fpt.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstructorProfileViewDto {
    private final ProfileDto instructor;
    private final double averageRating;
    private final int ratingStars;
    private final long ratingCount;
    private final boolean editMode;
}
