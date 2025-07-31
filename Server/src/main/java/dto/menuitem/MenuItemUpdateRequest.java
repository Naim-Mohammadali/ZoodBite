package dto.menuitem;

import jakarta.validation.constraints.*;

import java.util.List;

public record MenuItemUpdateRequest(
        @Size(max = 255) String name,
        @Size(max = 500) String description,
        @PositiveOrZero Double price,
        @Min(0) Integer quantity,
        String imgURL,
        List<String> categories) { }
