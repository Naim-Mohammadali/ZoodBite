package dao;

import model.Restaurant;
import model.Seller;
import java.util.List;

public interface RestaurantDAO {

    void save(Restaurant restaurant);
    Restaurant findById(Long id);
    void update(Restaurant restaurant);
    void delete(Restaurant restaurant);

    List<Restaurant> findBySeller(Seller seller);
    List<Restaurant> findByStatus(Restaurant.Status status);
    List<Restaurant> findActive();

    Restaurant findByIdAndSeller(Long restaurantId, Long sellerId);
    List<Restaurant> search(String keyword, String category,
                            Double minPrice, Double maxPrice);
}
