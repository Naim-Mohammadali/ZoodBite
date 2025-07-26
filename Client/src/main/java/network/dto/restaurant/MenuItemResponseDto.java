package network.dto.restaurant;

import java.util.List;

public class MenuItemResponseDto {
    public long id;
    public String name;
    public String description;
    public double price;
    public int quantity;
    public String imgURL;
    public List<String> categories;
}
