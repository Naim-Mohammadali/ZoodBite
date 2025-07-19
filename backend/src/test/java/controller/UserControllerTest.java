package controller;

import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import model.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-tests for UserController.
 * Uses Mockito to isolate the controller from the real service layer.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock  private UserService userService;
    private Validator         validator;
    private UserController    controller;

    /* ---------- setup ---------- */

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new UserController(userService, validator);
    }

    /* ---------- register ---------- */

    @Test
    void register_success() {
        var req   = new UserRegisterRequest("Ali","+96171112233","ali@mail.com",
                "secret123","Beirut", Role.CUSTOMER);
        var saved = new Customer("Ali","+96171112233","secret123","Beirut");

        when(userService.register(any(User.class))).thenReturn(saved);

        UserProfileResponse resp = controller.register(req);

        assertEquals("Ali", resp.name());
        assertEquals(Role.CUSTOMER, resp.role());
        verify(userService).register(any(User.class));
    }

    /* ---------- login ---------- */

    @Test
    void login_success() {
        var req   = new UserLoginRequest("+96171112233", "secret123");
        var user  = new Customer("Ali","+96171112233","secret123","Beirut");

        when(userService.login(req.phone(), req.password())).thenReturn(user);

        UserProfileResponse resp = controller.login(req);

        assertEquals("Ali", resp.name());
        verify(userService).login(req.phone(), req.password());
    }

    /* ---------- update ---------- */

    @Test
    void update_profile_success() {
        var patch = new UserUpdateRequest("Ali M.","Hamra Street 12", null);
        var user  = new Customer("Ali","+96171112233","secret123","Beirut");
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(userService.update(user)).thenReturn(user);

        UserProfileResponse resp = controller.update(1L, patch);

        assertEquals("Hamra Street 12", resp.address());
        verify(userService).update(user);
    }

    /* ---------- listAll & listByRole ---------- */

    @Test
    void list_all_users() {
        when(userService.findAll()).thenReturn(List.of(new Admin("root","+111","pw")));

        List<UserProfileResponse> list = controller.listAll();

        assertEquals(1, list.size());
        verify(userService).findAll();
    }

    @Test
    void list_by_role() {
        when(userService.findByRole(Role.ADMIN))
                .thenReturn(List.of(new Admin("root","+111","pw")));

        List<UserProfileResponse> list = controller.listByRole(Role.ADMIN);

        assertEquals(Role.ADMIN, list.getFirst().role());
        verify(userService).findByRole(Role.ADMIN);
    }

    /* ---------- view single ---------- */

    @Test
    void view_profile_success() {
        var admin = new Admin("root","+111","pw");
        admin.setId(99L);

        when(userService.findById(99L)).thenReturn(admin);

        UserProfileResponse dto = controller.view(99L);

        assertEquals(99L, dto.id());
        verify(userService).findById(99L);
    }
}
