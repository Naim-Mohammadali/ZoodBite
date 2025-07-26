package network.dto.restaurant;

import java.util.List;

public class MenuItemToMenuResponseDto {
    private List<Long> added_items;
    private String menu;

    public MenuItemToMenuResponseDto() {}

    public List<Long> getAdded_items() {
        return added_items;
    }

    public String getMenu() {
        return menu;
    }

    public void setAdded_items(List<Long> added_items) {
        this.added_items = added_items;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}

