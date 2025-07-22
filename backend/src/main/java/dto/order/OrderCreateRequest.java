package dto.order;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderCreateRequest(
        @NotNull Long restaurantId,
        @NotEmpty(message = "Order must contain at least one item")
        List<@NotNull Long> itemIds) { }
