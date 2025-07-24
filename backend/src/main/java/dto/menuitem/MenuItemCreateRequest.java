package dto.menuitem;

import jakarta.validation.constraints.*;

import java.util.List;

public record MenuItemCreateRequest(
        @NotBlank String name,
        @Size(max = 500) String description,
        @Positive double price,
        @Min(0) int quantity,
        String imgURL,
        List<String> categories,
        String vendor_id) { }
