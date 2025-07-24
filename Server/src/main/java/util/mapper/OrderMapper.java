package util.mapper;

import dto.order.OrderResponse;
import model.FoodOrder;
import model.MenuItem;

import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponse toDto(FoodOrder o) {
        return new OrderResponse(
                o.getId(), o.getRestaurant().getName(),o.getCreatedAt(), o.getStatus(),o.getCouponCode(),
                o.getTotal(),
                o.getDeliveryAddress(),
                o.getItems().stream().map(MenuItem::getId).collect(Collectors.toList()));
    }
}
