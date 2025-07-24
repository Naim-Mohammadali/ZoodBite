package service;

import dao.*;
import model.Customer;
import model.Rating;
import model.FoodOrder;
import java.time.LocalDateTime;
import java.util.List;

public class RatingService {

    private final RatingDAO ratingDAO;
    private final OrderDAO foodOrderDAO;


    public RatingService() {
        this.ratingDAO = new RatingDAO();
        this.foodOrderDAO = new OrderDAOImpl();
    }

    public Rating rate(Customer customer, Long orderId, int score, String comment) throws Exception {
        FoodOrder order = foodOrderDAO.findById(orderId);
        if (order == null) {
            throw new Exception("Order not found");
        }

        Rating rating = ratingDAO.findByCustomerAndOrder(customer, order);
        if (rating == null)
            rating = new Rating();

        rating.setCustomer(customer);
        rating.setOrder(order);
        rating.setScore(score);
        rating.setComment(comment);

        ratingDAO.save(rating);
        return rating;
    }


    public List<Rating> getRestaurantRatings(Long orderId) throws Exception {
        FoodOrder order = foodOrderDAO.findById(orderId);
        if (order == null) {
            throw new Exception("Restaurant not found");
        }

        return ratingDAO.findByOrder(order);
    }

    public List<Rating> getMyRatings(Customer customer) {
        return ratingDAO.findByCustomer(customer);
    }
    public double getAverageRating(Long orderId) {
        List<Rating> ratings = ratingDAO.findByOrder(foodOrderDAO.findById(orderId));
        if (ratings.isEmpty()) return 0;

        double sum = ratings.stream().mapToInt(Rating::getScore).sum();
        return sum / (double) ratings.size();
    }

    public void addOrUpdate(Customer customer, Long orderId, int score, String comment) {
        if (score < 1 || score > 5)
            throw new IllegalArgumentException("Rating score must be between 1 and 5");

        FoodOrder order = foodOrderDAO.findById(orderId);
        if (!order.getCustomer().getId().equals(customer.getId()))
            throw new SecurityException("You do not own this order.");

        if (order.getStatus() != FoodOrder.Status.DELIVERED)
            throw new IllegalStateException("You can only rate delivered orders.");

        Rating existing = ratingDAO.findByCustomerAndOrder(customer, order);
        if (existing != null) {
            existing.setScore(score);
            existing.setComment(comment);
            ratingDAO.update(existing);
        } else {
            Rating newRating = new Rating();
            newRating.setCustomer(customer);
            newRating.setOrder(order);
            newRating.setScore(score);
            newRating.setComment(null); // or allow it as optional
            newRating.setCreatedAt(LocalDateTime.now());
            ratingDAO.save(newRating);
        }
    }


}
