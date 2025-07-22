package controller;

import dto.delivery.DeliveryStatusPatchRequest;
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
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Path("/deliveries")
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
    public List<OrderResponse> myDeliveries(Courier courier, @QueryParam("status") String statusOpt)
    {
        FoodOrder.Status s = statusOpt == null ? null
                : FoodOrder.Status.valueOf(statusOpt);
        return orderService.listByCourier(courier, s).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @PATCH
    @Path("/orders/{orderId}/status")
    @Operation(summary = "Update delivery status by courier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "403", description = "Not allowed to update this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponse patchStatus(
            Courier courier,
            @PathParam("orderId") long orderId,
            DeliveryStatusPatchRequest dto) throws Exception
    {
        validate(dto);
        FoodOrder order = orderService.getOrderById(orderId);
        FoodOrder.Status ns = FoodOrder.Status.valueOf(dto.status());
        FoodOrder updated = orderService.updateStatusByCourier(courier, order, ns);
        return OrderMapper.toDto(updated);
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
