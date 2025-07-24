package dao;

import model.Customer;
import model.Favorite;
import model.Restaurant;
import java.util.List;

public interface FavoriteDAO {
    void save(Favorite fav);
    void delete(Favorite fav);
    boolean exists(Customer customer, Restaurant restaurant);
    List<Favorite> findByCustomer(Customer customer);
}
