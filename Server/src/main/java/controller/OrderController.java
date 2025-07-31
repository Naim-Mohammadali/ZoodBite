package controller;

import dto.order.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import service.CouponService;
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
    private final CustomerService  customerService;
    private final Validator        validator;

    public OrderController() {
        this(new OrderService(), new CustomerService(), new CouponService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           CouponService couponService,
                           Validator validator) {
        this.orderService    = orderService;
        this.customerService = customerService;
        this.validator       = validator;
    }






    @POST
    @RolesAllowed("customer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(@Valid CustomerOrderRequest request,
                               @HeaderParam("Authorization") String token) throws Exception {
        Customer customer = (Customer) customerService.findById(TokenUtil.decodeUserId(token));
        FoodOrder order = orderService.placeOrder(customer, request);
        return Response.ok(new OrderResponse(order.getId(),order.getRestaurant().getName(),
                        order.getCreatedAt(), order.getStatus(),order.getCouponCode(),order.getTotal(),order.getDeliveryAddress(), order.getItemIds ()))
                .build();
    }


    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
