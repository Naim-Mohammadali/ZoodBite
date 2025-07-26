package network.endpoint;
import network.ApiClient;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.order.UpdateOrderStatusRequestDto;
import network.dto.order.PlaceOrderResponseDto;

import java.net.http.HttpRequest;

public class SellerEndpoint {
    public PlaceOrderResponseDto updateOrderStatus(Long orderId, String newStatus) throws Exception {
        UpdateOrderStatusRequestDto dto = new UpdateOrderStatusRequestDto(newStatus);

        HttpRequest request = ApiClient.getInstance().buildPatch(
                "restaurants/orders/" + orderId, dto
        );

        return ApiClient.getInstance().send(request, PlaceOrderResponseDto.class);
    }
}
