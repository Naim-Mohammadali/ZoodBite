package dto.rating;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDto(
        @NotNull Long restaurantId,
        @Min(1) @Max(5) int score,
        @Nullable String comment
) {}
