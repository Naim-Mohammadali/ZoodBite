package controller;

import dto.courier.CourierStatusPatchRequest;
import dto.delivery.DeliveryStatusPatchRequest;
import dto.order.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import service.OrderService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/deliveries")
@RolesAllowed("courier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryController {

    private final OrderService orderService;
    private final Validator    validator;

    public DeliveryController() {
        this(new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public DeliveryController(OrderService orderService, Validator v) {
        this.orderService = orderService;
        this.validator    = v;
    }

    @GET
    @Operation(summary = "List all deliveries assigned to courier, filtered by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deliveries listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<OrderResponse> myDeliveries(
            @HeaderParam("Authorization") String token,
            @QueryParam("status") String statusOpt) {

        Courier courier = extractCourier(token);
        FoodOrder.Status s = statusOpt == null ? null : FoodOrder.Status.valueOf(statusOpt);

        return orderService.listByCourier(courier, s)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("courier")
    public List<OrderResponse> getAvailableOrders() {
        List<FoodOrder> orders = orderService.getAvailableOrdersForCourier();
        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();
    }


    @PATCH
    @Path("/{orderId}")
    @RolesAllowed("courier")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeliveryStatus(@HeaderParam("Authorization") String token,
                                         @PathParam("orderId") long orderId,
                                         @Valid CourierStatusPatchRequest dto) throws Exception {
        Courier courier = extractCourier(token);
        FoodOrder order = orderService.getOrderById(orderId);

        FoodOrder updated = orderService.updateStatusByCourier(courier, order, dto.status());

        return Response.ok(OrderMapper.toDto(updated)).build();
    }


    private Courier extractCourier(String token) {
        long id = TokenUtil.decodeUserId(token);
        return (Courier) orderService.findCourierById(id);
    }


    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
