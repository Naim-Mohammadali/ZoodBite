package controller;

import dto.order.OrderStatusPatchRequest;
import dto.order.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
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

import static model.FoodOrder.Status.ACCEPTED;
import static model.FoodOrder.Status.REJECTED;

@Path("/restaurants/orders")
@RolesAllowed("seller")
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
            @HeaderParam("Authorization") String token,
            @QueryParam("restaurantId") long restaurantId,
            @QueryParam("status") String statusOpt) throws Exception {

        Seller seller = extractSeller(token);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        FoodOrder.Status s = statusOpt == null ? null : FoodOrder.Status.valueOf(statusOpt);

        return orderService.listByRestaurant(seller, restaurant, s).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @PATCH
    @Path("/{orderId}")
    @Operation(summary = "Update order status by seller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Seller not authorized for this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponse updateStatus(
            @HeaderParam("Authorization") String token,
            @PathParam("orderId") long orderId,
            @Valid OrderStatusPatchRequest dto) throws Exception {

        validate(dto);

        Seller seller = extractSeller(token);
        FoodOrder order = orderService.getOrderById(orderId);
        FoodOrder.Status ns;
        if (dto.status().toLowerCase().contains("approve") || dto.status().toLowerCase().contains("accept")
                || dto.status().toLowerCase().contains("yes") || dto.status().toLowerCase().contains("valid"))
        {
            ns = ACCEPTED;

        } else { ns = REJECTED;}
        FoodOrder updated = orderService.updateStatusBySeller(seller, order, ns);
        return OrderMapper.toDto(updated);
    }
    private Seller extractSeller(String token) {
        long userId = TokenUtil.decodeUserId(token);  // Fake Base64 parser
        return (Seller) sellerService.findById(userId);
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
