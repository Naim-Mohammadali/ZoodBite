// service/AuthService.java
package service;

import dto.user.request.UserLoginRequest;
import dto.user.response.AuthResponse;
import util.JwtUtil;

public class AuthService {

    private final UserService users = new UserService();

    public AuthResponse login(UserLoginRequest dto) {
        var u   = users.login(dto.phone(), dto.password());
        var jwt = JwtUtil.issueToken(u.getId(), u.getRole());
        return new AuthResponse(jwt, u);
    }
}
