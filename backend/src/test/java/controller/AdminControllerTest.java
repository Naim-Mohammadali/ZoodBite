package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import util.mapper.UserMapper;
import model.*;
import model.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AdminService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock  private AdminService service;
    private Validator           validator;
    private AdminController     controller;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new AdminController(service, validator);
    }

    @Test
    void register_admin_success() {
        var req   = new UserRegisterRequest("Root","+1113456788","root@sys","psdfghjkw","HQ",Role.ADMIN);
        var saved = new Admin("Root","+111","pw");

        when(service.registerAdmin(any(UserRegisterRequest.class))).thenReturn(saved);

        UserProfileResponse resp = controller.registerAdmin(req);

        assertEquals(Role.ADMIN, resp.role());
        verify(service).registerAdmin(any(UserRegisterRequest.class));
    }

    @Test
    void list_all_users() {
        when(service.listAllUsers()).thenReturn(List.of(new Customer("Ali","+9617123567","psdfghjkl","B"), new Seller("Bob","+9627","pasdfghjkl","A")));

        List<UserProfileResponse> list = controller.listAllUsers();

        assertEquals(2, list.size());
        verify(service).listAllUsers();
    }

    @Test
    void block_user_success() {
        var user = new Customer("Ali","+96170000111","p123o567q","B");
        user.setStatus(User.Status.BLOCKED);

        when(service.blockUser(5L)).thenReturn(user);

        UserProfileResponse resp = controller.blockUser(5L);

        assertEquals(User.Status.BLOCKED, resp.status());
        verify(service).blockUser(5L);
    }
}
