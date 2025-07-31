package network.endpoint;
import network.ApiClient;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.order.UpdateOrderStatusRequestDto;
import network.dto.order.PlaceOrderResponseDto;

import java.net.http.HttpRequest;
import java.util.List;

public class SellerEndpoint {
    public PlaceOrderResponseDto updateOrderStatus(Long orderId, String newStatus) throws Exception {
        UpdateOrderStatusRequestDto dto = new UpdateOrderStatusRequestDto(newStatus);

        HttpRequest request = ApiClient.getInstance().buildPatch(
                "restaurants/orders/" + orderId, dto
        );

        return ApiClient.getInstance().send(request, PlaceOrderResponseDto.class);
    }
    public List<PlaceOrderResponseDto> getOrders(Long restaurantId, String statusOpt) throws Exception {
        String path = "restaurants/orders?restaurantId=" + restaurantId;
        if (statusOpt != null && !statusOpt.isBlank()) {
            path += "&status=" + statusOpt;
        }

        HttpRequest request = ApiClient.getInstance().buildGet(path);

        return ApiClient.getInstance().sendList(request, PlaceOrderResponseDto.class);
    }
}
