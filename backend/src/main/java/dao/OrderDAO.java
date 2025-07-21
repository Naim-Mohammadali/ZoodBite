package dao;
import model.Restaurant;
import model.User;
import model.FoodOrder;

import java.util.List;

public interface OrderDAO {
    void save (FoodOrder order);
    void update (FoodOrder order);
    FoodOrder findById (long id);
    List<FoodOrder> findByCustomer (User customer);
    List<FoodOrder> findByCourier (User courier);
    List<FoodOrder> findByStatus (FoodOrder.Status status);
    List<FoodOrder> findByRestaurant(Restaurant restaurant);
    List<FoodOrder> findByRestaurantAndStatus(Restaurant restaurant,
                                              FoodOrder.Status status);

}
