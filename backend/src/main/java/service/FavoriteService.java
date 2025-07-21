package service;

import dao.FavoriteDAO;
import dao.FavoriteDAOImpl;
import model.*;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteService {

    private final FavoriteDAO dao = new FavoriteDAOImpl();

    public void add(Customer customer, Restaurant restaurant) {
        if (dao.exists(customer, restaurant)) return;            // idempotent
        dao.save(new Favorite(customer, restaurant));
    }

    public void remove(Customer customer, Restaurant restaurant) {
        dao.findByCustomer(customer).stream()
                .filter(f -> f.getRestaurant().getId().equals(restaurant.getId()))
                .findFirst()
                .ifPresent(dao::delete);
    }

    public List<Restaurant> list(Customer customer) {
        return dao.findByCustomer(customer).stream()
                .map(Favorite::getRestaurant)
                .collect(Collectors.toList());
    }
}
