package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.Role;
import model.Seller;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.SellerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerControllerTest {

    @Mock  private SellerService service;
    private Validator            validator;
    private SellerController     controller;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new SellerController(service, validator);
    }

    @Test
    void register_seller_success() {
        var req   = new UserRegisterRequest("Shop","+9617123000","shop@mail","asdfghjkl","Saida",Role.SELLER);
        var saved = new Seller("Shop","+9617123000","asdfghjkl","Saida");

        when(service.registerSeller(any(UserRegisterRequest.class))).thenReturn(saved);

        UserProfileResponse resp = controller.registerSeller(req);

        assertEquals(Role.SELLER, resp.role());
        verify(service).registerSeller(any(UserRegisterRequest.class));
    }

    @Test
    void update_phone_success() {
        var updated = new Seller("Shop","+96178345688","pdfdfghjw","Saida");

        when(service.updatePhone(2L, "+96178345688")).thenReturn(updated);

        UserProfileResponse resp = controller.updatePhone(2L, "+96178345688");

        assertEquals("+96178345688", resp.phone());
        verify(service).updatePhone(2L, "+96178345688");
    }

    @Test
    void change_password_success() {
        var updated = new Seller("Shop","+9617000","newPdfghW","Saida");

        when(service.changePassword(2L,"newPdfghW")).thenReturn(updated);

        UserProfileResponse resp = controller.changePassword(2L,"newPdfghW");

        assertEquals("newPdfghW", updated.getPassword()); // entity holds new pw
        verify(service).changePassword(2L,"newPdfghW");
    }
}
