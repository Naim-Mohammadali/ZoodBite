package service;

import dao.RestaurantDAO;
import dao.RestaurantDAOImpl;
import model.Restaurant;
import model.Seller;

import java.util.List;

public class RestaurantService {

    protected final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    public void registerRestaurant(Restaurant restaurant, Seller seller) {
        restaurant.setSeller(seller);
        restaurant.setStatus(Restaurant.Status.PENDING);
        restaurantDAO.save(restaurant);
    }

    public void approveRestaurant(Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.PENDING) {
            throw new Exception("Restaurant is not pending approval!");
        }
        restaurant.setStatus(Restaurant.Status.ACTIVE);
        restaurantDAO.update(restaurant);
    }

    public void blockRestaurant(Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE) {
            throw new Exception("Only active restaurants can be blocked!");
        }
        restaurant.setStatus(Restaurant.Status.BLOCKED);
        restaurantDAO.update(restaurant);
    }

    public void unblockRestaurant(Restaurant restaurant) throws Exception {
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

    // ➕ Optional useful methods
    public Restaurant findById(Long id) {
        return restaurantDAO.findById(id);
    }

    public void updateRestaurant(Restaurant restaurant) {
        restaurantDAO.update(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantDAO.delete(restaurant);
    }
}
