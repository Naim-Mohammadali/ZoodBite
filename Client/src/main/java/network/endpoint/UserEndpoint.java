package network.endpoint;

import network.ApiClient;
import network.dto.user.*;

import java.net.http.HttpRequest;

public class UserEndpoint {
    public LoginResponseDto login(LoginRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("auth/login", dto);
        return ApiClient.getInstance().send(request, LoginResponseDto.class);
    }
    public LoginResponseDto register(RegisterRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("auth/register", dto);
        return ApiClient.getInstance().send(request, LoginResponseDto.class);
    }
    public UserDto getProfile() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("auth/profile");
        return ApiClient.getInstance().send(request, UserDto.class);
    }
    public UserDto updateProfile(UserUpdateRequestDto update) throws Exception {
        HttpRequest req = ApiClient.getInstance().buildPut("auth/profile", update);
        return ApiClient.getInstance().send(req, UserDto.class);
    }

}
