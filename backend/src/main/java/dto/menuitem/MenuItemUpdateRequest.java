package dto.menuitem;

import jakarta.validation.constraints.*;

public record MenuItemUpdateRequest(
        @Size(max = 255) String name,
        @Size(max = 500) String description,
        @PositiveOrZero Double price,
        @Min(0) Integer quantity,
        String imgURL,
        String category) { }
