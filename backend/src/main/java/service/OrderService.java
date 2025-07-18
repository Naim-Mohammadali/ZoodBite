package service;

import dao.OrderDAO;
import dao.OrderDAOImpl;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();

    public void placeOrder(Customer customer, Restaurant restaurant, List<MenuItem> items) throws Exception {
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

    public void assignCourier(Seller seller, FoodOrder order, Courier courier) throws Exception {
        if (!order.getRestaurant().getSeller().equals(seller)) {
            throw new Exception("Only the restaurant owner can assign a courier.");
        }
        if (courier == null || !courier.isAvailable()) {
            throw new Exception("Courier is not available.");
        }

        order.setCourier(courier);
        order.setStatus(FoodOrder.Status.IN_TRANSIT);
        orderDAO.update(order);
    }


    public void updateOrderStatus(FoodOrder order, FoodOrder.Status newStatus) {
        order.setStatus(newStatus);
        orderDAO.update(order);
    }

    public List<FoodOrder> getOrdersByCustomer(Customer customer) {
        return orderDAO.findByCustomer(customer);
    }

    public List<FoodOrder> getOrdersByCourier(Courier courier) {
        return orderDAO.findByCourier(courier);
    }

    public List<FoodOrder> getOrdersByStatus(FoodOrder.Status status) {
        return orderDAO.findByStatus(status);
    }

    public FoodOrder getOrderById(Long id) {
        return orderDAO.findById(id);
    }
}
