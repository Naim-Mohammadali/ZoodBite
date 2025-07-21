package dto.menuitem;

import jakarta.validation.constraints.*;

public record MenuItemUpdateRequest(
        String name,
        @Size(max = 500) String description,
        @PositiveOrZero Double price,
        @Min(0) Integer quantity,
        String imgURL,
        String category) { }
