package controller;

import dto.order.OrderStatusPatchRequest;
import dto.order.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.*;
import service.OrderService;
import service.RatingService;
import service.RestaurantService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/seller/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SellerOrderController {

    private final OrderService orderService;
    private final Validator    validator;
    private final RestaurantService restaurantService;
    public SellerOrderController(RestaurantService restaurantService) {
        this(new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator(), restaurantService);
    }
    public SellerOrderController(OrderService service, Validator validator, RestaurantService restaurantService) {
        this.orderService = service;
        this.validator    = validator;
        this.restaurantService = restaurantService;
    }


    @GET
    @Operation(summary = "List all orders for a seller's restaurant, optionally filtered by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders listed successfully"),
            @ApiResponse(responseCode = "403", description = "Seller not authorized for this restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public List<OrderResponse> listOrders(
            Seller seller,
            @QueryParam("restaurantId") long restaurantId,
            @QueryParam("status") String statusOpt) throws Exception
    {
        FoodOrder.Status s = statusOpt == null ? null
                : FoodOrder.Status.valueOf(statusOpt);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return orderService.listByRestaurant(seller, restaurant, s).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @PATCH
    @Path("/orders/{orderId}/status")
    @Operation(summary = "Update order status by seller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Seller not authorized for this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponse updateStatus(
            Seller seller,
            @PathParam("orderId") long orderId,
            OrderStatusPatchRequest dto) throws Exception
    {
        validate(dto);
        FoodOrder order = orderService.getOrderById(orderId);
        FoodOrder.Status ns = FoodOrder.Status.valueOf(dto.status());
        FoodOrder updated = orderService.updateStatusBySeller(seller, order, ns);
        return OrderMapper.toDto(updated);
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
