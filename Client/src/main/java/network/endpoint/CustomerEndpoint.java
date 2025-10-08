package network.endpoint;

import network.ApiClient;
import network.dto.order.PlaceOrderRequestDto;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.rating.CustomerRatingRequestDto;
import network.dto.user.VendorSearchRequestDto;
import network.dto.user.RestaurantResponseDto;
import java.net.http.HttpRequest;
import java.util.List;

public class CustomerEndpoint {
    public List<RestaurantResponseDto> searchVendors(String keyword) throws Exception {
        VendorSearchRequestDto requestDto = new VendorSearchRequestDto(keyword);
        HttpRequest request = ApiClient.getInstance().buildPost("vendors", requestDto);

        return ApiClient.getInstance().sendList(request, RestaurantResponseDto.class);
    }
    public PlaceOrderResponseDto placeOrder(PlaceOrderRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("orders", dto);
        return ApiClient.getInstance().send(request, PlaceOrderResponseDto.class);
    }
    public void rateOrder(Long orderId, int score, String comment) throws Exception {
        CustomerRatingRequestDto dto = new CustomerRatingRequestDto(orderId, score, comment);
        HttpRequest request = ApiClient.getInstance().buildPost("ratings", dto);
        ApiClient.getInstance().send(request, Void.class);
    }
}
