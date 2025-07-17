package dao;

import model.MenuItem;
import model.Restaurant;

import java.util.List;

public interface MenuItemDAO {
    void save(MenuItem menuItem);
    void update(MenuItem menuItem);
    void delete(MenuItem menuItem);
    MenuItem findById(Long id);
    List<MenuItem> findByName(String menuItemName);
    List<MenuItem> findByRestaurant(Restaurant restaurant);
}
