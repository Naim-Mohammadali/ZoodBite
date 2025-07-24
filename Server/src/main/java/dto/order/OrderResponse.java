package dto.order;

import jakarta.validation.constraints.NotBlank;
import model.FoodOrder;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String restaurantName,
        LocalDateTime createdAt,
        FoodOrder.Status status,
        String couponCode,
        double total,
        @NotBlank String address,
        List<Long> itemIds) {
}
