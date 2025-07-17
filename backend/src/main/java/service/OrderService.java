package service;
import dao.OrderDAO;
import dao.OrderDAOImpl;
import model.FoodOrder;
import model.MenuItem;
import model.Restaurant;
import model.User;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();

    public void placeOrder(User customer, Restaurant restaurant, List<MenuItem> items) throws Exception {
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Only customers can place orders.");
        }
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE) {
            throw new Exception("Restaurant is not active.");
        }
        if (items == null || items.isEmpty()) {
            throw new Exception("No items selected.");
        }

        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setItems(items);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(FoodOrder.Status.PLACED);

        double total = items.stream().mapToDouble(MenuItem::getPrice).sum();
        order.setTotal(total);

        orderDAO.save(order);
    }

    public void assignCourier(FoodOrder order, User courier) throws Exception {
        if (courier.getRole() != User.Role.COURIER) {
            throw new Exception("Assigned user is not a courier.");
        }
        order.setCourier(courier);
        order.setStatus(FoodOrder.Status.IN_TRANSIT);
        orderDAO.update(order);
    }

    public void updateOrderStatus(FoodOrder order, FoodOrder.Status newStatus) {
        order.setStatus(newStatus);
        orderDAO.update(order);
    }

    public List<FoodOrder> getOrdersByCustomer(User customer) throws Exception {
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new Exception("Not a customer.");
        }
        return orderDAO.findByCustomer(customer);
    }

    public List<FoodOrder> getOrdersByCourier(User courier) throws Exception {
        if (courier.getRole() != User.Role.COURIER) {
            throw new Exception("Not a courier.");
        }
        return orderDAO.findByCourier(courier);
    }

    public List<FoodOrder> getOrdersByStatus(FoodOrder.Status status) {
        return orderDAO.findByStatus(status);
    }
}
