package dto.order;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CustomerOrderRequest(
        @NotNull Long restaurantId,
        @NotNull List<Long> itemIds,
        String comment,
        String couponCode
) {}
