package controller;

import dto.admin.AdminRestaurantStatusPatch;
import dto.admin.AdminUserRolePatch;
import dto.admin.ChangePasswordRequest;
import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AdminService;
import service.RestaurantService;
import service.OrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock private AdminService adminService;
    @Mock private RestaurantService restaurantService;
    @Mock private OrderService orderService;
    private Validator validator;
    private AdminController controller;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new AdminController(adminService, restaurantService, orderService, validator);
    }

    @Test
    void register_admin_success() {
        var req = new UserRegisterRequest("Admin", "+11111111", "a@b", "pwdasdfgh", "HQ", Role.ADMIN);
        when(adminService.registerAdmin(any())).thenReturn(new Admin("Admin", "+111", "pwdasdfgh"));

        var resp = controller.registerAdmin(req);

        assertEquals(Role.ADMIN, resp.role());
        verify(adminService).registerAdmin(any());
    }

    @Test
    void list_all_users_success() {
        var u1 = new Customer("Ali", "+96171", "p1", "Loc1");
        var u2 = new Seller("Bob", "+96172", "p2", "Loc2");

        when(adminService.listAllUsers()).thenReturn(List.of(u1, u2));

        var list = controller.listUsers(null);

        assertEquals(2, list.size());
        verify(adminService).listAllUsers();
    }

    @Test
    void block_user_success() {
        var user = new Customer("Ali", "+96171", "pass", "Loc");
        user.setStatus(User.Status.BLOCKED);
        when(adminService.blockUser(5L)).thenReturn(user);

        var resp = controller.blockUser(5L);

        assertEquals(User.Status.BLOCKED, resp.status());
        verify(adminService).blockUser(5L);
    }

    @Test
    void change_role_success() {
        var patch = new AdminUserRolePatch("SELLER");
        var user = new Customer("Ali", "+96171", "pass", "Loc");
        user.setRole(Role.SELLER);

        when(adminService.changeRole(eq(10L), eq(Role.SELLER))).thenReturn(user);

        var result = controller.changeRole(10L, patch);

        assertEquals(Role.SELLER, result.role());
        verify(adminService).changeRole(eq(10L), eq(Role.SELLER));
    }

    @Test
    void patch_restaurant_approve_success() throws Exception {
        var patch = new AdminRestaurantStatusPatch("ACTIVE");
        var rest = new Restaurant();
        rest.setStatus(Restaurant.Status.ACTIVE);

        when(restaurantService.findById(20L)).thenReturn(rest);

        var dto = controller.patchRestaurant(20L, patch);

        assertEquals(Restaurant.Status.ACTIVE, dto.status());
        verify(restaurantService).approveRestaurant(rest);
    }

    @Test
    void patch_restaurant_block_success() throws Exception {
        var patch = new AdminRestaurantStatusPatch("BLOCKED");
        var rest = new Restaurant();
        rest.setStatus(Restaurant.Status.ACTIVE);

        when(restaurantService.findById(20L)).thenReturn(rest);

        var dto = controller.patchRestaurant(20L, patch);

        assertEquals(Restaurant.Status.ACTIVE, dto.status()); // assuming block does not change immediately
        verify(restaurantService).blockRestaurant(rest);
    }

    @Test
    void change_password_success() {
        var admin = new Admin("A", "+961", "pwd");
        when(adminService.changePassword(5L, "newpass")).thenReturn(admin);

        var resp = controller.changePassword(5L, new ChangePasswordRequest("newpass"));

        assertEquals("+961", resp.phone());
        verify(adminService).changePassword(5L, "newpass");
    }

    @Test
    void update_profile_success() {
        var patch = new UserUpdateRequest("Ali", "ali@zood.com",null);
        var admin = new Admin("Old", "+961", "pwd");
        admin.setEmail("ali@zood.com");
        admin.setName("Ali");

        when(adminService.findById(1L)).thenReturn(admin);
        when(adminService.update(admin)).thenReturn(admin);

        var resp = controller.updateProfile(1L, patch);

        assertEquals("Ali", resp.name());
        verify(adminService).update(admin);
    }
}
