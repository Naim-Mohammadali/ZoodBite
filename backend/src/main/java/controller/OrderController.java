package controller;

import dto.order.*;
import jakarta.validation.*;
import model.*;
import service.MenuItemService;
import service.OrderService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderController {

    private final OrderService    orderService;
    private final MenuItemService itemService;
    private final Validator       validator;

    public OrderController() {
        this(new OrderService(), new MenuItemService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public OrderController(OrderService orderService,
                           MenuItemService itemService,
                           Validator validator) {
        this.orderService = orderService;
        this.itemService  = itemService;
        this.validator    = validator;
    }

    public OrderResponse place(Customer customer,
                               OrderCreateRequest dto) throws Exception {
        validate(dto);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(dto.restaurantId());

        List<MenuItem> items = dto.itemIds().stream()
                .map(id -> {
                    try { return itemService.getById(id); }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .collect(Collectors.toList());

        orderService.placeOrder(customer, restaurant, items, null);

        FoodOrder persisted = orderService.getOrdersByCustomer(customer).getLast();
        return OrderMapper.toDto(persisted);
    }
    public List<OrderResponse> myOrders(Customer customer) {
        return orderService.getOrdersByCustomer(customer)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
