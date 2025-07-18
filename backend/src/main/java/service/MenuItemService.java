package service;

import dao.MenuItemDAO;
import dao.MenuItemDAOImpl;
import model.MenuItem;
import model.Restaurant;
import model.Seller;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuItemService {

    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    /**
     * Seller adds a new menu item to their restaurant.
     */
    public void addMenuItem(@NotNull Seller seller, @NotNull Restaurant restaurant, @NotNull MenuItem item) throws Exception {
        validateOwnership(seller, restaurant);

        if (item.getPrice() < 0)
            throw new Exception("Price cannot be negative.");
        if (item.getName() == null || item.getName().trim().isEmpty())
            throw new Exception("Item name is required.");

        item.setRestaurant(restaurant);
        menuItemDAO.save(item);
    }

    public void updateMenuItem(@NotNull Seller seller, @NotNull MenuItem item) throws Exception {
        if (item.getId() == 0)
            throw new Exception("MenuItem ID is required for update.");

        validateOwnership(seller, item.getRestaurant());
        menuItemDAO.update(item);
    }


    public void deleteMenuItem(@NotNull Seller seller, @NotNull MenuItem item) throws Exception {
        if (item.getId() == 0)
            throw new Exception("MenuItem ID is required for deletion.");

        validateOwnership(seller, item.getRestaurant());
        menuItemDAO.delete(item);
    }


    public MenuItem getById(Long id) throws Exception {
        MenuItem item = menuItemDAO.findById(id);
        if (item == null)
            throw new Exception("MenuItem not found with ID: " + id);
        return item;
    }

    public List<MenuItem> searchByName(String name) {
        return menuItemDAO.findByName(name);
    }

    public List<MenuItem> getRestaurantMenu(@NotNull Restaurant restaurant) throws Exception {
        if (restaurant.getId() == null)
            throw new Exception("Valid restaurant ID required.");
        return menuItemDAO.findByRestaurant(restaurant);
    }


    private void validateOwnership(@NotNull Seller seller, @NotNull Restaurant restaurant) throws Exception {
        if (restaurant.getSeller() == null || restaurant.getSeller().getId() != seller.getId()) {
            throw new Exception("You do not own this restaurant.");
        }
    }

}
