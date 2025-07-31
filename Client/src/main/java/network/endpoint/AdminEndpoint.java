package network.endpoint;

import network.ApiClient;
import network.dto.admin.CreateCouponRequestDto;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.user.UpdateUserStatusRequestDto;
import network.dto.user.UpdateUserStatusResponseDto;
import util.SessionManager;

import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;


public class AdminEndpoint {

    public UpdateUserStatusResponseDto approveUser(Long sellerId) throws Exception {
        UpdateUserStatusRequestDto dto = new UpdateUserStatusRequestDto("APPROVED");

        String path = "admin/users/" + sellerId + "/status";
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI("http://localhost:8080/" + path))
                .timeout(java.time.Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .method("PATCH", BodyPublishers.ofString(json))
                .build();

        return ApiClient.getInstance().send(request, UpdateUserStatusResponseDto.class);
    }
    public void createCoupon(CreateCouponRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("admin/coupons", dto);
        ApiClient.getInstance().send(request, Void.class);
        System.out.println("✅ Coupon created: " + dto.code);
    }
    public List<PlaceOrderResponseDto> getAllOrders() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("admin/orders");
        return ApiClient.getInstance().sendList(request, PlaceOrderResponseDto.class);
    }
}
