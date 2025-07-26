package network.endpoint;

import network.ApiClient;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.order.UpdateOrderStatusRequestDto;

import java.net.http.HttpRequest;
import java.util.List;

public class CourierEndpoint {
    public List<PlaceOrderResponseDto> getAvailableDeliveries() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("deliveries/available");
        return ApiClient.getInstance().sendList(request, PlaceOrderResponseDto.class);
    }
    public PlaceOrderResponseDto pickOrder(Long orderId) throws Exception {
        return updateOrderStatus(orderId, "accepted");
    }

    public PlaceOrderResponseDto deliverOrder(Long orderId) throws Exception {
        return updateOrderStatus(orderId, "delivered");
    }

    private PlaceOrderResponseDto updateOrderStatus(Long orderId, String status) throws Exception {
        UpdateOrderStatusRequestDto dto = new UpdateOrderStatusRequestDto(status);
        String path = "deliveries/" + orderId;

        HttpRequest request = ApiClient.getInstance().buildPatch(path, dto);
        return ApiClient.getInstance().send(request, PlaceOrderResponseDto.class);
    }

}
