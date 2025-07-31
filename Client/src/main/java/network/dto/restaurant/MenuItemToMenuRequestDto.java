package network.dto.restaurant;

import java.util.List;

public class MenuItemToMenuRequestDto {
    private List<Long> item_ids;

    public MenuItemToMenuRequestDto() {}

    public MenuItemToMenuRequestDto(List<Long> item_ids) {
        this.item_ids = item_ids;
    }

    public List<Long> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<Long> item_ids) {
        this.item_ids = item_ids;
    }
}
