package service;

import dao.OrderDAO;
import dao.OrderDAOImpl;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();

    // 📦 Place order (Customer type — no role check needed)
    public FoodOrder placeOrder(Customer customer, Restaurant restaurant, List<MenuItem> items) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE)
            throw new Exception("Restaurant is not active.");

        if (items == null || items.isEmpty())
            throw new Exception("No items selected.");

        double total = items.stream().mapToDouble(MenuItem::getPrice).sum();

        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setItems(items);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(FoodOrder.Status.PLACED);

        orderDAO.save(order);
        return order;
    }

    // 🚚 Assign a courier (Courier type — no casting)
    public void assignCourier(FoodOrder order, Courier courier) {
        order.setCourier(courier);
        order.setStatus(FoodOrder.Status.IN_TRANSIT);
        orderDAO.update(order);
    }

    // 🔄 Change order status (e.g., PREPARING → DELIVERED)
    public void updateOrderStatus(FoodOrder order, FoodOrder.Status newStatus) {
        order.setStatus(newStatus);
        orderDAO.update(order);
    }

    // 🧾 Customer views their orders
    public List<FoodOrder> getOrdersByCustomer(Customer customer) {
        return orderDAO.findByCustomer(customer);
    }

    // 🚚 Courier views assigned orders
    public List<FoodOrder> getOrdersByCourier(Courier courier) {
        return orderDAO.findByCourier(courier);
    }

    // 🧾 Admin views orders by status
    public List<FoodOrder> getOrdersByStatus(FoodOrder.Status status) {
        return orderDAO.findByStatus(status);
    }
}
