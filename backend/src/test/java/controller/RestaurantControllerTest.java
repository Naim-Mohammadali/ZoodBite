package controller;

import dto.restaurant.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.RestaurantService;
import util.mapper.RestaurantMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit-tests for RestaurantController.
 * Uses Mockito to isolate the controller from the real service layer.
 */
@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    /* ---------- collaborators ---------- */
    @Mock  private RestaurantService restaurantService;
    private Validator                validator;
    private RestaurantController     controller;

    /* ---------- common fixtures ---------- */
    private Seller seller;           // “current” authenticated seller
    private Restaurant existing;     // persisted entity used in multiple tests

    /* ---------- setup ---------- */

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new RestaurantController(restaurantService, validator);

        seller   = new Seller("Bob","+96170000000","pw","Beirut");
        seller.setId(55L);

        existing = new Restaurant("Bob's Grill","Hamra","+96170000000",
                seller, null,5,2);
        existing.setId(1L);
        existing.setStatus(Restaurant.Status.ACTIVE);
    }

    /* ---------- register ---------- */

    @Test
    void register_success() {
        var req = new RestaurantCreateDto(
                "Bob's Grill","Hamra","+96170000000",null,5,2);

        // registerRestaurant() is void
        doNothing().when(restaurantService)
                .registerRestaurant(any(Restaurant.class), eq(seller));

        RestaurantResponseDto resp = controller.register(seller, req);

        assertEquals("Bob's Grill", resp.name());
        verify(restaurantService).registerRestaurant(any(Restaurant.class), eq(seller));
    }

    /* ---------- update ---------- */

    @Test
    void update_success() throws Exception {
        var patch = new RestaurantUpdateDto("Grill 2", null,
                null, null, null, null);

        when(restaurantService.findByIdAndSeller(1L, seller.getId()))
                .thenReturn(existing);
        doNothing().when(restaurantService).updateRestaurant(existing);

        RestaurantResponseDto resp = controller.update(1L, seller, patch);

        assertEquals("Grill 2", resp.name());
        verify(restaurantService).updateRestaurant(existing);
    }

    /* ---------- listMine ---------- */

    @Test
    void list_mine() {
        when(restaurantService.getRestaurantsBySeller(seller))
                .thenReturn(List.of(existing));

        List<RestaurantResponseDto> list = controller.listMine(seller);

        assertEquals(1, list.size());
        verify(restaurantService).getRestaurantsBySeller(seller);
    }

    /* ---------- browse (public) ---------- */

    @Test
    void browse_active_restaurants() {
        when(restaurantService.listActive()).thenReturn(List.of(existing));

        List<RestaurantResponseDto> list = controller.browse();

        assertEquals(1, list.size());
        assertEquals("Bob's Grill", list.getFirst().name());
        verify(restaurantService).listActive();
    }

    /* ---------- approve ---------- */

    @Test
    void approve_pending_restaurant() throws Exception {
        Restaurant pending = new Restaurant("New Café","Main St","+111",
                seller,null,5,2);
        pending.setId(77L);
        pending.setStatus(Restaurant.Status.ACTIVE);

        when(restaurantService.findById(77L)).thenReturn(pending);
        doNothing().when(restaurantService).approveRestaurant(pending);

        RestaurantResponseDto dto = controller.approve(77L);

        assertEquals(Restaurant.Status.ACTIVE, pending.getStatus());
        verify(restaurantService).approveRestaurant(pending);
    }
}
