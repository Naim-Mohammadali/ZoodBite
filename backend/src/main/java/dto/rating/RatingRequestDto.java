package dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDto(
        @NotNull Long restaurantId,
        @Min(1) @Max(5) int score,
        String comment
) {}
