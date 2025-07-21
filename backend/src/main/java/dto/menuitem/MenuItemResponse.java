package dto.menuitem;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        double price,
        int quantity,
        String imgURL,
        String category) { }
