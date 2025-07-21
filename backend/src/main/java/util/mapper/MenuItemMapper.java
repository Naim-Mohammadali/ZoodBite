package util.mapper;

import dto.menuitem.MenuItemResponse;
import model.MenuItem;

public final class MenuItemMapper {
    private MenuItemMapper() {}

    public static MenuItemResponse toDto(MenuItem m) {
        return new MenuItemResponse(
                m.getId(), m.getName(), m.getDescription(),
                m.getPrice(), m.getQuantity(), m.getImgURL(), m.getCategory());
    }
}
