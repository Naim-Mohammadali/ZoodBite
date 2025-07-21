package dto.coupon;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CouponCreateDto(
        @NotBlank String code,
        @Min(1) @Max(100) int discountPercent,
        @NotNull LocalDate validFrom,
        @NotNull LocalDate validUntil,
        @Min(1) int usageLimit
) {}
