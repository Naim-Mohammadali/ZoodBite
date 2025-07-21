package dto.restaurant;

import jakarta.validation.constraints.*;

public record RestaurantCreateDto(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String phone,
        String logoBase64,
        @Min(0) Integer taxFee,
        @Min(0) Integer additionalFee) { }
