package controller;

import dto.order.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import service.MenuItemService;
import service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit-tests for OrderController.
 */
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    /* ---- collaborators ---- */
    @Mock private OrderService    orderService;
    @Mock private MenuItemService menuItemService;
    private Validator             validator;
    private OrderController       controller;

    /* ---- fixtures ---- */
    private Customer    customer;
    private Restaurant  restaurant;
    private MenuItem    item;
    private FoodOrder   persisted;

    @BeforeEach
    void setUp() {
        validator  = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new OrderController(orderService, menuItemService, validator);

        customer   = new Customer("Ali","+9617111111","pwasdfgh","Beirut"); customer.setId(1L);
        restaurant = new Restaurant("Grill","Hamra","+9617000000",null, null,5,2);
        restaurant.setId(2L);

        item = new MenuItem(); item.setId(3L); item.setPrice(9.9); item.setRestaurant(restaurant);

        persisted = new FoodOrder();
        persisted.setId(10L);
        persisted.setCreatedAt(LocalDateTime.now());
        persisted.setStatus(FoodOrder.Status.PLACED);
        persisted.setRestaurant(restaurant);
        persisted.setItems(List.of(item));
        persisted.setTotal(9.9 + 5 + 2);  // price + taxFee + addFee
    }

    /* ------------ place order ------------ */

    @Test
    void place_order_success() throws Exception {
        var req = new OrderCreateRequest(restaurant.getId(), List.of(item.getId()));

        when(menuItemService.getById(item.getId())).thenReturn(item);
        doNothing().when(orderService)
                .placeOrder(eq(customer),
                        any(Restaurant.class),
                        eq(List.of(item)));
        when(orderService.getOrdersByCustomer(customer))
                .thenReturn(List.of(persisted));

        OrderResponse resp = controller.place(customer, req);

        assertEquals(10L, resp.id());
        assertEquals(restaurant.getId(), resp.id());
        verify(orderService).placeOrder(eq(customer),
                any(Restaurant.class),
                eq(List.of(item)));    }

    /* ------------ myOrders ------------ */

    @Test
    void list_my_orders() {
        when(orderService.getOrdersByCustomer(customer))
                .thenReturn(List.of(persisted));

        List<OrderResponse> list = controller.myOrders(customer);

        assertEquals(1, list.size());
        assertEquals("PLACED", list.getFirst().status());
        verify(orderService).getOrdersByCustomer(customer);
    }
}
