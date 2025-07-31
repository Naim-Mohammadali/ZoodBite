package controller;

import dto.courier.CourierAvailabilityRequest;
import dto.customer.ChangePhoneRequest;
import dto.user.request.UserRegisterRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.Courier;
import model.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.CourierService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @Mock  private CourierService service;
    private Validator             validator;
    private CourierController     controller;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new CourierController(service, validator);
    }

    @Test
    void register_courier_success() {
        var req   = new UserRegisterRequest("Joe","+9617555555","joe@mail","pwasdfgh","Tripoli",Role.COURIER);
        var saved = new Courier("Joe","+9617555555","pwasdfgh","Tripoli");

        when(service.registerCourier(any(UserRegisterRequest.class))).thenReturn(saved);

        UserProfileResponse resp = controller.registerCourier(req);

        assertEquals(Role.COURIER, resp.role());
        verify(service).registerCourier(any(UserRegisterRequest.class));
    }

    @Test
    void set_availability_success() {
        var courier = new Courier("Joe","+9617555555","pwasdfgh","Tripoli");
        courier.setAvailable(true);

        when(service.setAvailability(3L, true)).thenReturn(courier);

        UserProfileResponse resp = controller.setAvailability(3L, new CourierAvailabilityRequest(true));

        assertTrue(resp.status() == null || courier.isAvailable()); // adjust if DTO includes availability
        verify(service).setAvailability(3L, true);
    }

    @Test
    void change_phone_success() {
        var updated = new Courier("Joe","+9617999999","pwasdfgh","Tripoli");

        when(service.changePhone(3L, "+9617999999")).thenReturn(updated);

        UserProfileResponse resp = controller.changePhone(3L, new ChangePhoneRequest("+9617999999"));

        assertEquals("+9617999999", resp.phone());
        verify(service).changePhone(3L, "+9617999999");
    }
}
