package controller;

import dto.admin.ChangePasswordRequest;
import dto.customer.ChangePhoneRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock private CustomerService service;
    @Mock private UserService userService;
    @Mock private OrderService orderService;
    @Mock private RatingService rating;
    @Mock private FavoriteService favorite;
    @Mock private CouponService coupon;

    private CustomerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new CustomerController(service, userService, orderService, validator, rating, favorite, coupon);
    }

    @Test
    void view_profile_success() {
        Customer cust = new Customer("Sara", "+96171123456", "secret88", "Byblos");
        cust.setId(1L);
        when(service.viewProfile(1L)).thenReturn(cust);

        UserProfileResponse dto = controller.view(String.valueOf(1L));

        assertEquals(1L, dto.id());
        verify(service).viewProfile(1L);
    }

    @Test
    void update_success() {
        Customer updated = new Customer("Sara", "+96171123456", "secret88", "Jounieh");

        when(service.updateAddress(1L, "Jounieh")).thenReturn(updated);

        UserUpdateRequest patch = new UserUpdateRequest(null, "Jounieh", null);

        UserProfileResponse resp = controller.update(String.valueOf(1L), patch);

        assertEquals("Jounieh", resp.address());
        verify(service).updateAddress(1L, "Jounieh");
    }

    @Test
    void change_phone_success() {
        Customer updated = new Customer("Sara", "+96176667777", "secret88", "Byblos");

        when(service.changePhone(1L, "+96176667777")).thenReturn(updated);

        UserProfileResponse resp = controller.changePhone(String.valueOf(1L), new ChangePhoneRequest("+96176667777"));

        assertEquals("+96176667777", resp.phone());
        verify(service).changePhone(1L, "+96176667777");
    }

    @Test
    void change_password_success() {
        Customer updated = new Customer("Sara", "+96171123456", "newSecurePwd", "Byblos");

        when(service.changePassword(1L, "newSecurePwd")).thenReturn(updated);

        UserProfileResponse resp = controller.changePassword(String.valueOf(1L), new ChangePasswordRequest("newSecurePwd"));

        assertEquals("Sara", resp.name());
        verify(service).changePassword(1L, "newSecurePwd");
    }
}
