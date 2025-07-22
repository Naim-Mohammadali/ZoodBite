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
import service.RestaurantService;
import service.SellerService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/seller/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SellerOrderController {

    private final OrderService      orderService;
    private final RestaurantService restaurantService;
    private final SellerService     sellerService;
    private final Validator         validator;

    public SellerOrderController() {
        this(new OrderService(), new RestaurantService(), new SellerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public SellerOrderController(OrderService orderService,
                                 RestaurantService restaurantService,
                                 SellerService sellerService,
                                 Validator validator) {
        this.orderService      = orderService;
        this.restaurantService = restaurantService;
        this.sellerService     = sellerService;
        this.validator         = validator;
    }

    @GET
    @Operation(summary = "List all orders for a seller's restaurant, optionally filtered by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders listed successfully"),
            @ApiResponse(responseCode = "403", description = "Seller not authorized for this restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public List<OrderResponse> listOrders(
            @QueryParam("sellerId") long sellerId,
            @QueryParam("restaurantId") long restaurantId,
            @QueryParam("status") String statusOpt) throws Exception {

        Seller seller = (Seller) sellerService.findById(sellerId);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        FoodOrder.Status s = statusOpt == null ? null : FoodOrder.Status.valueOf(statusOpt);

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
            @QueryParam("sellerId") long sellerId,
            @PathParam("orderId") long orderId,
            @Valid OrderStatusPatchRequest dto) throws Exception {

        validate(dto);

        Seller seller = (Seller) sellerService.findById(sellerId);
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
