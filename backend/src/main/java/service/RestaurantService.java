package service;

import dao.RestaurantDAO;
import dao.RestaurantDAOImpl;
import jakarta.ws.rs.NotFoundException;
import model.Menu;
import model.MenuItem;
import model.Restaurant;
import model.Seller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RestaurantService {

    protected final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    public void registerRestaurant(@NotNull Restaurant restaurant, Seller seller) {
        restaurant.setSeller(seller);
        restaurant.setStatus(Restaurant.Status.PENDING);
        restaurantDAO.save(restaurant);
    }

    public void approveRestaurant(@NotNull Restaurant restaurant) throws Exception {
        restaurant.setStatus(Restaurant.Status.ACTIVE);
        restaurantDAO.update(restaurant);
    }

    public void blockRestaurant(@NotNull Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE) {
            throw new Exception("Only active restaurants can be blocked!");
        }
        restaurant.setStatus(Restaurant.Status.BLOCKED);
        restaurantDAO.update(restaurant);
    }

    public void unblockRestaurant(@NotNull Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.BLOCKED) {
            throw new Exception("Restaurant is not blocked!");
        }
        restaurant.setStatus(Restaurant.Status.ACTIVE);
        restaurantDAO.update(restaurant);
    }

    public List<Restaurant> getApprovedRestaurants() {
        return restaurantDAO.findByStatus(Restaurant.Status.ACTIVE);
    }

    public List<Restaurant> getRestaurantsBySeller(Seller seller) {
        return restaurantDAO.findBySeller(seller);
    }

    public Restaurant findById(Long id) {
        return restaurantDAO.findById(id);
    }

    public void updateRestaurant(Restaurant restaurant) {
        restaurantDAO.update(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantDAO.delete(restaurant);
    }
    public List<Restaurant> listActive() {
        return getApprovedRestaurants();
    }

    public Restaurant findByIdAndSeller(Long restaurantId, Long sellerId) {
        Restaurant r = restaurantDAO.findById(restaurantId);
        if (r != null && r.getSeller() != null &&
                sellerId.equals(r.getSeller().getId())) {
            return r;
        }
        return null;
    }
    public List<Restaurant> search(String keyword,
                                   String category,
                                   Double minPrice,
                                   Double maxPrice) {
        return restaurantDAO.search(keyword, category, minPrice, maxPrice);
    }

    public List<Restaurant> getByStatus(Restaurant.Status status) {
        return restaurantDAO.findByStatus(status);
    }

    public void createEmptyMenu(Restaurant restaurant, String title) {
        for (Menu m : restaurant.getMenus()) {
            if (title.equals(m.getTitle())) {
                throw new IllegalArgumentException("Menu already exists");
            }
        }


        Menu menu = new Menu(title);
            menu.setRestaurant(restaurant);
            menu.setTitle(title);
            menu.setItems(new ArrayList<>());
            restaurant.getMenus().add(menu);
            restaurantDAO.update(restaurant);
    }
    public void addItemToMenu(Restaurant restaurant, String menuTitle, MenuItem item) {
            Menu menu = findMenu(restaurant, menuTitle);
            menu.getItems().add(item);
            restaurantDAO.update(restaurant);
    }
    public void removeItemFromMenu(Restaurant restaurant, String menuTitle, MenuItem item) {
            Menu menu = findMenu(restaurant, menuTitle);
            menu.getItems().remove(item);
            restaurantDAO.update(restaurant);
    }
    public List<MenuItem> getMenuItems(Restaurant restaurant, String menuTitle) {
        Menu menu = findMenu(restaurant, menuTitle);
        return menu.getItems();
    }

    private Menu findMenu(Restaurant restaurant, String title) {
        return restaurant.getMenus().stream()
                .filter(m -> m.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + title));
    }

    public List<Restaurant> searchByName(String search) {
        if (search == null || search.isBlank())
            return List.of();

        return restaurantDAO.findByNameContaining(search);
    }

}
