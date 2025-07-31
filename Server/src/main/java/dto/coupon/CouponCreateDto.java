package dto.coupon;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CouponCreateDto(
        @NotBlank String code,
        @Min(1) @Max(100) int value,
        @Min(0) double min_price,
        @Min(1) double user_count,
        @NotNull LocalDate validFrom,
        @NotNull LocalDate validUntil
) {}
