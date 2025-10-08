package network.endpoint;

import network.ApiClient;
import network.dto.admin.CreateCouponRequestDto;
import network.dto.admin.GetUsersResponseDto;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.user.UpdateUserStatusRequestDto;
import network.dto.user.UpdateUserStatusResponseDto;
import network.dto.user.UserDto;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.stream.Collectors;


public class AdminEndpoint {

    public UpdateUserStatusResponseDto approveUser(Long id) throws Exception {
        UpdateUserStatusRequestDto dto = new UpdateUserStatusRequestDto("APPROVED");

        String path = "admin/users/" + id + "/status";
        HttpRequest request = ApiClient.getInstance().buildPatch(path, dto);

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
    public GetUsersResponseDto getAllUsers() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("admin/users");
        return ApiClient.getInstance().send(request, GetUsersResponseDto.class);
    }
    public List<UserDto> getUsersByStatus(String status) throws Exception {
        GetUsersResponseDto response = getAllUsers();
        return response.users.stream()
                .filter(u -> status.equalsIgnoreCase(u.status))
                .collect(Collectors.toList());
    }

}
