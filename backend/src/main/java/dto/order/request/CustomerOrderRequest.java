package dto.order.request;

import java.util.List;

public record CustomerOrderRequest(
        Long restaurantId,
        List<Long> itemIds,
        String notes,
        String couponCode
) {}
