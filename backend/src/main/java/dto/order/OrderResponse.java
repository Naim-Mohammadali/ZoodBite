package dto.order;

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
        List<Long> itemIds) {
}
