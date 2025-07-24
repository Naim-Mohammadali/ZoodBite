package service;

import dao.*;
import dto.order.CustomerOrderRequest;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static model.FoodOrder.Status.*;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
    private final CouponService couponService = new CouponService();

    public FoodOrder placeOrder(Customer customer, CustomerOrderRequest request) throws Exception {
        RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
        Restaurant restaurant = restaurantDAO.findById(request.vendor_id());
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE)
            throw new Exception("Restaurant is not active.");

        List<MenuItem> items = new ArrayList<>();
        double total = 0;

        for (CustomerOrderRequest.ItemQuantity iq : request.items()) {
            MenuItem item = menuItemDAO.findById(iq.id());
            if (item == null || !item.getRestaurant().getId().equals(restaurant.getId())) {
                throw new IllegalArgumentException("Invalid item in menu");
            }
            items.add(item);
            total += item.getPrice() * iq.quantity();
            if (request.coupon() != null) {
                total = couponService.applyDiscount(couponService.findByCode(request.coupon()), total);
                couponService.incrementUsage(couponService.findByCode(request.coupon()));
            }
        }

        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setItems(items);
        order.setTotal(total);
        order.setStatus(PLACED);
        System.out.println("Setting delivery address: " + request.address());
        order.setDeliveryAddress(request.address());
        order.setComment(request.comment());
        order.setCreatedAt(LocalDateTime.now());
        orderDAO.save(order);
        return order;
    }

    public void assignCourier(Seller seller, @NotNull FoodOrder order, Courier courier) throws Exception {
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
    public void updateOrderStatus(@NotNull FoodOrder order, FoodOrder.Status newStatus) {
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

    public List<FoodOrder> listByRestaurant(Seller seller,
                                            Restaurant restaurant,
                                            FoodOrder.Status status) throws Exception {
        validateOwnership(seller, restaurant);
        return status == null
                ? orderDAO.findByRestaurant(restaurant)
                : orderDAO.findByRestaurantAndStatus(restaurant, status);
    }

    public FoodOrder updateStatusBySeller(Seller seller,
                                          @NotNull FoodOrder order,
                                          FoodOrder.Status newStatus) throws Exception {
        validateOwnership(seller, order.getRestaurant());

        boolean ok = switch (order.getStatus().toString().toUpperCase()) {
            case "PLACED"     -> newStatus == ACCEPTED;
            case "ACCEPTED"   -> newStatus == PREPARING;
            case "PREPARING"  -> newStatus == FoodOrder.Status.READY_FOR_PICKUP;
            default         -> newStatus == FoodOrder.Status.REJECTED;
        };

        order.setStatus(newStatus);
        orderDAO.update(order);
        return orderDAO.findById(order.getId());
    }

    public List<FoodOrder> listByCourier(Courier courier,
                                         FoodOrder.Status status) {
        return status == null
                ? orderDAO.findByCourier(courier)
                : orderDAO.findByCourierAndStatus(courier, status);
    }

    public FoodOrder updateStatusByCourier(Courier courier, FoodOrder order, String statusRaw) throws Exception {
        String status = statusRaw.trim().toLowerCase();

        if (status.contains("accept")) {
            if (order.getCourier() != null)
                throw new IllegalStateException("Order already assigned");

            if (order.getStatus() != FoodOrder.Status.ACCEPTED)
                throw new IllegalStateException("Order must be accepted by seller first");

            order.setCourier(courier);
            order.setStatus(FoodOrder.Status.IN_TRANSIT);
        }
        else if (status.contains("deliver")) {
            if (!courier.getId().equals(order.getCourier().getId()))
                throw new IllegalAccessException("You are not assigned to this order");

            if (order.getStatus() != FoodOrder.Status.IN_TRANSIT &&
                    order.getStatus() != FoodOrder.Status.READY_FOR_PICKUP)
                throw new IllegalStateException("Order is not in a deliverable state");
            order.setStatus(FoodOrder.Status.DELIVERED);
        }
        else {
            throw new IllegalArgumentException("Invalid status action");
        }

        orderDAO.update(order);
        return order;
    }


    public List<FoodOrder> getOrderHistory(Customer customer) {
        return orderDAO.findByCustomer(customer);
    }


    private void validateOwnership(@NotNull Seller s, @NotNull Restaurant r) throws Exception {
        if (!s.getId().equals(r.getSeller().getId()))
            throw new Exception("Seller does not own this restaurant");
    }

    public void updateOrder(FoodOrder order) {
        orderDAO.update(order);
    }
    public List<FoodOrder> getAvailableOrdersForCourier() {
        return orderDAO.findUnassignedAcceptedOrders();
    }


    public Object findCourierById(long courierId) {
        return (new CourierService()).findById(courierId);
    }

    public List<FoodOrder> getAllOrders() {
        return orderDAO.findAll();
    }

}
