package dto.order;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderCreateRequest(
        @NotNull Long restaurantId,
        @NotEmpty List<@NotNull Long> itemIds) { }
