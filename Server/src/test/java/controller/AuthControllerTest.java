package controller;

import dto.user.request.UserLoginRequest;
import dto.user.request.UserRegisterRequest;
import dto.user.response.AuthResponse;
import model.Customer;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private UserService mockUserService;
    private AuthController controller;

    @BeforeEach
    void setup() {
        mockUserService = mock(UserService.class);
        controller = new AuthController();
    }

    @Test
    void register_success() {
        var req = new UserRegisterRequest("Ali","+96171112233","ali@mail.com",
                "secret123","Beirut", Role.CUSTOMER);
        var saved = new Customer("Ali","+96171112233","secret123","Beirut");
        saved.setId(1L);

        when(mockUserService.register(any(User.class))).thenReturn(saved);
        when(mockUserService.issueToken(saved)).thenReturn("token-123");

        AuthResponse resp = controller.register(req);

        assertEquals(1L, resp.user_id());
        assertEquals("token-123", resp.token());
        verify(mockUserService).register(any(User.class));
        verify(mockUserService).issueToken(saved);
    }

    @Test
    void login_success() {
        var req = new UserLoginRequest("+96171112233", "secret123");
        var user = new Customer("Ali","+96171112233","secret123","Beirut");
        user.setId(1L);

        when(mockUserService.login(req.phone(), req.password())).thenReturn(user);
        when(mockUserService.issueToken(user)).thenReturn("token-456");

        AuthResponse resp = controller.login(req);

        assertEquals(1L, resp.user_id());
        assertEquals("token-456", resp.token());
        verify(mockUserService).login(req.phone(), req.password());
        verify(mockUserService).issueToken(user);
    }
}
