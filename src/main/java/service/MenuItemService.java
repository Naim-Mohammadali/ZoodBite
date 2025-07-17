package service;
import dao.MenuItemDAO;
import dao.MenuItemDAOImpl;
import model.MenuItem;
import model.Restaurant;
import java.util.List;
public class MenuItemService {
    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
    public void addMenuItem(MenuItem menuItem, Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE){
            throw new Exception("Cannot add menu to inactive restaurant!");
        }
        menuItem.setRestaurant(restaurant);
        menuItemDAO.save(menuItem);
    }
    public void updateMenuItem(MenuItem menuItem) {
        menuItemDAO.update(menuItem);
    }
    public void deleteMenuItem(MenuItem menuItem) {
        menuItemDAO.delete(menuItem);
    }
    public List<MenuItem> getMenuByRestaurant (Restaurant restaurant) {
        return menuItemDAO.findByRestaurant(restaurant);
    }
    public List<MenuItem> getAllItemsByName (String name) {
        return menuItemDAO.findByName(name);
    }
}
