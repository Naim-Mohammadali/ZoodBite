package network.endpoint;

import network.ApiClient;
import network.dto.LoginRequestDto;
import network.dto.LoginResponseDto;
import network.dto.RegisterRequestDto;

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

}
