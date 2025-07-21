package controller;

import dto.delivery.DeliveryStatusPatchRequest;
import dto.order.OrderResponse;
import jakarta.validation.*;
import model.*;
import service.OrderService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<OrderResponse> myDeliveries(Courier courier, String statusOpt) {
        FoodOrder.Status s = statusOpt == null ? null
                : FoodOrder.Status.valueOf(statusOpt);
        return orderService.listByCourier(courier, s).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderResponse patchStatus(Courier courier,
                                     FoodOrder order,
                                     DeliveryStatusPatchRequest dto) throws Exception {
        validate(dto);
        FoodOrder.Status ns = FoodOrder.Status.valueOf(dto.status());
        FoodOrder updated = orderService.updateStatusByCourier(courier, order, ns);
        return OrderMapper.toDto(updated);
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
