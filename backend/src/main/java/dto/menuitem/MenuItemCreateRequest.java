package dto.menuitem;

import jakarta.validation.constraints.*;

public record MenuItemCreateRequest(
        @NotBlank String name,
        @Size(max = 500) String description,
        @Positive double price,
        @Min(0) int quantity,
        String imgURL,
        String category) { }
