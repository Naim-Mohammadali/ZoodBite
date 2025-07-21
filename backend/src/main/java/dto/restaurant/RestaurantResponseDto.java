package dto.restaurant;

public record RestaurantResponseDto(
        Long id,
        String name,
        String address,
        String phone,
        String logoBase64,
        Integer taxFee,
        Integer additionalFee) { }
