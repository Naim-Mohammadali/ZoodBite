package dto.restaurant;
import model.Restaurant;

import java.util.List;

public record RestaurantResponseDto(
        Long id,
        String name,
        String address,
        String phone,
        String logoBase64,
        Integer taxFee,
        Integer additionalFee,
        Restaurant.Status status,
        List<MenuResponse> menus
) { }
