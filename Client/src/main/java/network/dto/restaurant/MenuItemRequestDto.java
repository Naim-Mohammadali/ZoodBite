package network.dto.restaurant;

import java.util.List;

public class MenuItemRequestDto {
    public String name;
    public String description;
    public double price;
    public int quantity;
    public List<String> categories;
    public long vendor_id;

    public MenuItemRequestDto() {}
    public MenuItemRequestDto(String name, String description, double price, int quantity, List<String> categories, long vendor_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categories = categories;
        this.vendor_id = vendor_id;
    }
}
