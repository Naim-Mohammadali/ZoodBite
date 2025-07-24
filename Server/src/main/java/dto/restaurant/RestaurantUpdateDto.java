package dto.restaurant;

import jakarta.validation.constraints.*;

public record RestaurantUpdateDto(
        String name,
        String address,
        String phone,
        String logoBase64,
        @Min(0) Integer taxFee,
        @Min(0) Integer additionalFee) { }
