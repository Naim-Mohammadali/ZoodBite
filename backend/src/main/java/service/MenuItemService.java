package service;

import dao.MenuItemDAO;
import dao.MenuItemDAOImpl;
import model.MenuItem;
import model.Restaurant;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuItemService {

    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    // Add new menu item
    public void addMenuItem(@NotNull MenuItem item) throws Exception {
        if (item.getRestaurant() == null) {
            throw new Exception("MenuItem must be linked to a restaurant.");
        }
        if (item.getPrice() < 0) {
            throw new Exception("Price cannot be negative.");
        }
        menuItemDAO.save(item);
    }

    // Update existing item
    public void updateMenuItem(@NotNull MenuItem item) throws Exception {
        if (item.getId() == 0) {
            throw new Exception("MenuItem ID is required for update.");
        }
        menuItemDAO.update(item);
    }

    // Delete item
    public void deleteMenuItem(@NotNull MenuItem item) throws Exception {
        if (item.getId() == 0) {
            throw new Exception("MenuItem ID is required for deletion.");
        }
        menuItemDAO.delete(item);
    }

    // View menu item by ID
    public MenuItem getById(Long id) throws Exception {
        MenuItem item = menuItemDAO.findById(id);
        if (item == null) {
            throw new Exception("MenuItem not found with ID: " + id);
        }
        return item;
    }

    // Get items by name (exact)
    public List<MenuItem> searchByName(String name) {
        return menuItemDAO.findByName(name);
    }

    // Get menu for a restaurant
    public List<MenuItem> getRestaurantMenu(Restaurant restaurant) throws Exception {
        if (restaurant == null || restaurant.getId() == null) {
            throw new Exception("Valid restaurant is required.");
        }
        return menuItemDAO.findByRestaurant(restaurant);
    }
}
