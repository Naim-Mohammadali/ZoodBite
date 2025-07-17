package service;

import dao.RestaurantDAO;
import dao.RestaurantDAOImpl;
import model.User;
import model.Restaurant;
import java.util.List;
public class RestaurantService {
    protected final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    public void registerRestaurant (Restaurant restaurant, User seller) throws Exception{
        if (seller.getRole() != User.Role.SELLER)
        {
            throw new Exception("Only sellers can register restaurants");
        }
        restaurant.setSeller(seller);
        restaurant.setStatus(Restaurant.Status.PENDING);
        restaurantDAO.save(restaurant);
    }
    public void approveRestaurant (Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.PENDING)
        {
            throw new Exception("Restaurant is not pending approval!");
        }
        restaurant.setStatus(Restaurant.Status.ACTIVE);
        restaurantDAO.update(restaurant);
    }
    public void blockRestaurant (Restaurant restaurant) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.BLOCKED) {
            throw new Exception("Only active restaurants can be blocked!");
        }
        restaurant.setStatus(Restaurant.Status.BLOCKED);
        restaurantDAO.update(restaurant);
    }
    public void unblockRestaurant (Restaurant restaurant) throws Exception{
        if (restaurant.getStatus() != Restaurant.Status.BLOCKED) {
            throw new Exception("Restaurant is not blocked!");
        }
        restaurant.setStatus(Restaurant.Status.ACTIVE);
        restaurantDAO.update(restaurant);
    }
    public List<Restaurant> getApprovedRestaurants() {
        return restaurantDAO.findByStatus(Restaurant.Status.ACTIVE);
    }
    public List<Restaurant> getRestaurantsBySeller (User seller) throws Exception{
        if (seller.getRole() != User.Role.SELLER) {
            throw new Exception("Not a seller!");
        }
        return restaurantDAO.findBySeller(seller);
    }



}
