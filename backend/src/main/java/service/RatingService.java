package service;

import dao.RatingDAO;
import dao.RestaurantDAO;
import dao.RestaurantDAOImpl;
import model.Customer;
import model.Rating;
import model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RatingService {

    private final RatingDAO ratingDAO;
    private final RestaurantDAO restaurantDAO;

    public RatingService() {
        this.ratingDAO = new RatingDAO();
        this.restaurantDAO = new RestaurantDAOImpl();
    }

    public Rating rate(Customer customer, Long restaurantId, int score, String comment) throws Exception {
        Restaurant restaurant = restaurantDAO.findById(restaurantId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        Rating rating = ratingDAO.findByCustomerAndRestaurant(customer, restaurant);
        if (rating == null)
            rating = new Rating();

        rating.setCustomer(customer);
        rating.setRestaurant(restaurant);
        rating.setScore(score);
        rating.setComment(comment);

        ratingDAO.save(rating);
        return rating;
    }


    public List<Rating> getRestaurantRatings(Long restId) throws Exception {
        Restaurant restaurant = restaurantDAO.findById(restId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        return ratingDAO.findByRestaurant(restaurant);
    }

    public List<Rating> getMyRatings(Customer customer) {
        return ratingDAO.findByCustomer(customer);
    }
    public double getAverageRating(Long restaurantId) {
        List<Rating> ratings = ratingDAO.findByRestaurantId(restaurantId);
        if (ratings.isEmpty()) return 0;

        double sum = ratings.stream().mapToInt(Rating::getScore).sum();
        return sum / (double) ratings.size();
    }

    public void addOrUpdate(Customer customer, Long restaurantId, int score) {
        if (score < 1 || score > 5)
            throw new IllegalArgumentException("Rating score must be between 1 and 5");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Rating existing = ratingDAO.findByCustomerAndRestaurant(customer, restaurant);

        if (existing != null) {
            existing.setScore(score);
            ratingDAO.update(existing);
        } else {
            Rating newRating = new Rating(customer, restaurant, score, null);
            ratingDAO.save(newRating);
        }
    }

}
