package dao;

import model.Restaurant;
import model.Seller;
import model.User;

import java.util.List;

public interface RestaurantDAO {
    void save(Restaurant restaurant);
    Restaurant findById(Long id);
    void update(Restaurant restaurant);
    void delete(Restaurant restaurant);
    List<Restaurant> findBySeller(Seller seller);
    List<Restaurant> findByStatus(Restaurant.Status status);
}


