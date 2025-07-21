package controller;

import dto.order.OrderStatusPatchRequest;
import dto.order.OrderResponse;
import jakarta.validation.*;
import model.*;
import service.OrderService;
import util.mapper.OrderMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SellerOrderController {

    private final OrderService orderService;
    private final Validator    validator;

    public SellerOrderController() {
        this(new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public SellerOrderController(OrderService service, Validator validator) {
        this.orderService = service;
        this.validator    = validator;
    }

    public List<OrderResponse> listOrders(Seller seller,
                                          Restaurant restaurant,
                                          String statusOpt) throws Exception {
        FoodOrder.Status s = statusOpt == null ? null
                : FoodOrder.Status.valueOf(statusOpt);
        return orderService.listByRestaurant(seller, restaurant, s).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderResponse updateStatus(Seller seller,
                                      FoodOrder order,
                                      OrderStatusPatchRequest dto) throws Exception {
        validate(dto);
        FoodOrder.Status ns = FoodOrder.Status.valueOf(dto.status());
        FoodOrder updated = orderService.updateStatusBySeller(seller, order, ns);
        return OrderMapper.toDto(updated);
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
