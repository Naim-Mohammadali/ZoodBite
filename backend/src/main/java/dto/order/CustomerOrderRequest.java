package dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CustomerOrderRequest(
        @Min(1) Long vendor_id,
        @NotBlank String address,
        @Nullable String comment,
        @Nullable String coupon,
        @NotEmpty List<ItemQuantity> items
) {    public record ItemQuantity(
        @Min(1) Long id,
        @Min(1) Integer quantity
) {}
}
