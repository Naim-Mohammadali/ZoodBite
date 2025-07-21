package util.mapper;

import dto.menuitem.MenuItemCreateRequest;
import dto.menuitem.MenuItemResponse;
import model.MenuItem;
import model.Restaurant;

public final class MenuItemMapper {
    private MenuItemMapper() {}

    public static MenuItemResponse toDto(MenuItem m) {
        return new MenuItemResponse(
                m.getId(), m.getName(), m.getDescription(),
                m.getPrice(), m.getQuantity(), m.getImgURL(), m.getCategory());
    }

    public static MenuItem fromCreateRequest(MenuItemCreateRequest req, Restaurant r) {
        MenuItem m = new MenuItem();
        m.setName(req.name());
        m.setDescription(req.description());
        m.setPrice(req.price());
        m.setQuantity(req.quantity());
        m.setImgURL(req.imgURL());
        m.setCategory(req.category());
        m.setRestaurant(r);
        return m;
    }
}
