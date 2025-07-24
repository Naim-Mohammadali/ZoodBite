package util.mapper;

import dao.MenuDAO;
import dao.MenuDAOImpl;
import dto.restaurant.MenuResponse;
import dto.restaurant.ItemIdDto;
import model.Menu;
import model.MenuItem;

import java.util.List;

public final class MenuMapper {
    private MenuMapper() {}
    public static MenuResponse toDto(Menu menu) {
        // Load menu with items using DAO
        MenuDAO menuDAO = new MenuDAOImpl();
        Menu fetched = menuDAO.findWithItems(menu.getId());

        List<ItemIdDto> items = fetched.getItems().stream()
                .map(item -> new ItemIdDto((int) item.getId()))
                .toList();

        return new MenuResponse(fetched.getId(), fetched.getTitle(), items);
    }

}
