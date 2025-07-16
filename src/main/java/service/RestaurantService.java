package service;

import dao.RestaurantDAO;
import dao.RestaurantDAOImpl;

public class RestaurantService {
    protected final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

}
