package util.mapper;

import dto.order.OrderResponse;
import model.FoodOrder;

import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponse toDto(FoodOrder o) {
        return new OrderResponse(
                o.getId(), o.getCreatedAt(), o.getStatus().name(),
                o.getTotal(), o.getRestaurant().getId(),
                o.getItems().stream().map(i -> i.getId()).collect(Collectors.toList()));
    }
}
