package dto.menuitem;

import java.util.List;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        double price,
        int quantity,
        String imgURL,
        List<String> categories) {}
