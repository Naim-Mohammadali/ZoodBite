package controller;

import dto.menuitem.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MenuItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit-tests for MenuItemController.
 */
@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    /* ---- collaborators ---- */
    @Mock  private MenuItemService menuItemService;
    private Validator              validator;
    private MenuItemController     controller;

    /* ---- fixtures ---- */
    private Seller     seller;
    private Restaurant restaurant;
    private MenuItem   item;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new MenuItemController(menuItemService, validator);

        seller      = new Seller("Bob","+9617000000","pwasdfgh","Beirut");
        seller.setId(11L);

        restaurant  = new Restaurant("Bob's Grill","Hamra","+9617000000",
                seller,null,5,2);
        restaurant.setId(21L);

        item = new MenuItem();
        item.setId(31L);
        item.setName("Burger");
        item.setDescription("Delicious");
        item.setPrice(9.9);
        item.setQuantity(10);
        item.setRestaurant(restaurant);
    }

    /* ------------ add ------------ */

    @Test
    void add_menu_item_success() throws Exception {
        var req = new MenuItemCreateRequest("Burger","Delicious",9.9,10,
                null,"Fast-Food");

        doNothing().when(menuItemService)
                .addMenuItem(eq(seller), eq(restaurant), any(MenuItem.class));

        MenuItemResponse resp = controller.add(seller, restaurant, req);

        assertEquals("Burger", resp.name());
        assertEquals(10, resp.quantity());
        verify(menuItemService)
                .addMenuItem(eq(seller), eq(restaurant), any(MenuItem.class));
    }

    /* ------------ update ------------ */

    @Test
    void update_menu_item_success() throws Exception {
        var patch = new MenuItemUpdateRequest("Cheese Burger", null,
                10.9, 12, null, null);

        when(menuItemService.getById(31L)).thenReturn(item);
        doNothing().when(menuItemService).updateMenuItem(seller, item);

        MenuItemResponse resp = controller.update(seller, 31L, patch);

        assertEquals("Cheese Burger", resp.name());
        assertEquals(12, resp.quantity());
        verify(menuItemService).updateMenuItem(seller, item);
    }

    /* ------------ delete ------------ */

    @Test
    void delete_menu_item_success() throws Exception {
        when(menuItemService.getById(31L)).thenReturn(item);
        doNothing().when(menuItemService).deleteMenuItem(seller, item);

        controller.delete(seller, 31L);

        verify(menuItemService).deleteMenuItem(seller, item);
    }

    /* ------------ list ------------ */

    @Test
    void list_menu_items() throws Exception {
        when(menuItemService.getRestaurantMenu(restaurant))
                .thenReturn(List.of(item));

        List<MenuItemResponse> list = controller.list(restaurant.getId());

        assertEquals(1, list.size());
        assertEquals("Burger", list.getFirst().name());
        verify(menuItemService).getRestaurantMenu(restaurant);
    }
}
