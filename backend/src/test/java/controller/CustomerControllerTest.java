package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import service.OrderService;
import service.UserService;
import util.mapper.UserMapper;
import model.*;
import model.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CustomerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock   private CustomerService service;
    private Validator               validator;
    private UserService userService;
    private OrderService orderService;
    private CustomerController      controller;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new CustomerController(service, userService, orderService, validator );
    }

    @Test
    void register_success() {
        var req   = new UserRegisterRequest(
                "Sara", "+96171123456", "sara@mail.com",
                "secret88", "Byblos", Role.CUSTOMER);

        var saved = new Customer("Sara", "+96171123456", "secret88", "Byblos");

        when(service.registerCustomer(any(UserRegisterRequest.class))).thenReturn(saved);

        UserProfileResponse resp = controller.register(req);

        assertEquals("Sara", resp.name());
        assertEquals(Role.CUSTOMER, resp.role());
        verify(service).registerCustomer(any(UserRegisterRequest.class));
    }

    @Test
    void change_phone_success() {
        var updated = new Customer("Sara", "+96176667777", "secret88", "Byblos");

        when(service.changePhone(1L, "+96176667777")).thenReturn(updated);

        UserProfileResponse resp = controller.changePhone(1L, "+96176667777");

        assertEquals("+96176667777", resp.phone());
        verify(service).changePhone(1L, "+96176667777");
    }

    @Test
    void view_profile_success() {
        var cust = new Customer("Sara", "+96171123456", "secret88", "Byblos");
        cust.setId(1L);

        when(service.viewProfile(1L)).thenReturn(cust);

        UserProfileResponse dto = controller.view(1L);

        assertEquals(1L, dto.id());
        verify(service).viewProfile(1L);
    }
}
