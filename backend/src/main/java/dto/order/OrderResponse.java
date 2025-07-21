package dto.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime createdAt,
        String status,
        double total,
        Long restaurantId,
        List<Long> itemIds) { }
