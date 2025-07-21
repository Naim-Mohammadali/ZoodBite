package service;

import dao.MenuItemDAO;
import dao.MenuItemDAOImpl;
import dao.OrderDAO;
import dao.OrderDAOImpl;
import dto.order.request.CustomerOrderRequest;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();


    public void placeOrder(Customer customer, @NotNull Restaurant restaurant, List<MenuItem> items) throws Exception {
        if (restaurant.getStatus() != Restaurant.Status.ACTIVE) {
            throw new Exception("Restaurant is not active.");
        }
        if (items == null || items.isEmpty()) {
            throw new Exception("No items selected.");
        }
        boolean mismatch = items.stream()
                .anyMatch(i -> !i.getRestaurant().getId()
                        .equals(restaurant.getId()));
        if (mismatch)
            throw new Exception("One or more items do not belong to the selected restaurant.");

        double total = items.stream().mapToDouble(MenuItem::getPrice).sum();
        total += restaurant.getAdditionalFee();
        total += restaurant.getTaxFee();


        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setItems(items);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(FoodOrder.Status.PLACED);
        order.setTotal(total);

        orderDAO.save(order);
    }
    public FoodOrder placeOrder(Customer customer, CustomerOrderRequest dto) throws Exception {
        Restaurant restaurant = restaurantService.findById(dto.restaurantId());
        List<MenuItem> items = dto.itemIds().stream()
                .map(menuItemDAO::findById)
                .filter(Objects::nonNull)
                .toList();

        if (items.isEmpty())
            throw new Exception("No valid items selected.");

        this.placeOrder(customer, restaurant, items);
        FoodOrder latest = new FoodOrder();
        latest.setCustomer(customer);
        latest.setItems(items);
        latest.setRestaurant(restaurant);
        latest.setTotal(items.stream().mapToDouble(MenuItem::getPrice).sum()
                + restaurant.getAdditionalFee() + restaurant.getTaxFee());
        latest.setStatus(FoodOrder.Status.PLACED);
        return latest;
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

        boolean ok = switch (order.getStatus()) {
            case PLACED     -> newStatus == FoodOrder.Status.ACCEPTED
                    || newStatus == FoodOrder.Status.REJECTED;
            case ACCEPTED   -> newStatus == FoodOrder.Status.PREPARING;
            case PREPARING  -> newStatus == FoodOrder.Status.READY_FOR_PICKUP;
            default         -> false;
        };
        if (!ok) throw new IllegalStateException("Illegal status transition");

        order.setStatus(newStatus);
        orderDAO.update(order);
        return order;
    }

    public List<FoodOrder> listByCourier(Courier courier,
                                         FoodOrder.Status status) {
        return status == null
                ? orderDAO.findByCourier(courier)
                : orderDAO.findByCourierAndStatus(courier, status);
    }

        public FoodOrder updateStatusByCourier(Courier courier,
                                           FoodOrder order,
                                           FoodOrder.Status newStatus) throws Exception {

        if (!courier.equals(order.getCourier()))
            throw new Exception("Order not assigned to this courier");

        boolean ok = switch (order.getStatus()) {
            case READY_FOR_PICKUP -> newStatus == FoodOrder.Status.IN_TRANSIT;
            case IN_TRANSIT       -> newStatus == FoodOrder.Status.DELIVERED;
            default               -> false;
        };
        if (!ok) throw new IllegalStateException("Illegal status transition");

        order.setStatus(newStatus);
        orderDAO.update(order);
        return order;
    }

    private void validateOwnership(@NotNull Seller s, @NotNull Restaurant r) throws Exception {
        if (!s.getId().equals(r.getSeller().getId()))
            throw new Exception("Seller does not own this restaurant");
    }

}
