package controller;

import dto.order.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.*;
import service.CustomerService;
import service.MenuItemService;
import service.OrderService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    private final OrderService     orderService;
    private final MenuItemService  itemService;
    private final CustomerService  customerService;
    private final Validator        validator;

    public OrderController() {
        this(new OrderService(), new MenuItemService(), new CustomerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public OrderController(OrderService orderService,
                           MenuItemService itemService,
                           CustomerService customerService,
                           Validator validator) {
        this.orderService     = orderService;
        this.itemService      = itemService;
        this.customerService  = customerService;
        this.validator        = validator;
    }

    @POST
    @Operation(summary = "Place a new order")
    public OrderResponse place(
            @QueryParam("customerId") long customerId,
            @Valid OrderCreateRequest dto) throws Exception {

        validate(dto);

        Customer customer = (Customer) customerService.findById(customerId);

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

    @GET
    @Operation(summary = "Get customer's order history")
    public List<OrderResponse> myOrders(@QueryParam("customerId") long customerId) {
        Customer customer = (Customer) customerService.findById(customerId);
        return orderService.getOrdersByCustomer(customer).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
