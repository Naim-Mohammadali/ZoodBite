package network.endpoint;

import network.ApiClient;
import network.dto.order.PlaceOrderRequestDto;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.rating.CustomerRatingRequestDto;
import network.dto.user.VendorSearchRequestDto;
import network.dto.user.RestaurantResponseDto;
import util.SessionManager;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;

public class CustomerEndpoint {
    public List<RestaurantResponseDto> searchVendors(String keyword) throws Exception {
        VendorSearchRequestDto requestDto = new VendorSearchRequestDto(keyword);
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(requestDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/vendors"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .POST(BodyPublishers.ofString(json))
                .build();

        return ApiClient.getInstance().sendList(request, RestaurantResponseDto.class);
    }
    public PlaceOrderResponseDto placeOrder(PlaceOrderRequestDto dto) throws Exception {
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/orders"))
                .timeout(java.time.Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .POST(BodyPublishers.ofString(json))
                .build();
        return ApiClient.getInstance().send(request, PlaceOrderResponseDto.class);
    }
    public void rateOrder(Long orderId, int score, String comment) throws Exception {
        CustomerRatingRequestDto dto = new CustomerRatingRequestDto(orderId, score, comment);
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI("http://localhost:8080/ratings"))
                .timeout(java.time.Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .POST(BodyPublishers.ofString(json))
                .build();

        ApiClient.getInstance().send(request, Void.class);
    }
}
