package controller;

import dto.order.CustomerOrderRequest;
import dto.order.OrderResponse;
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
import service.*;
import util.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock private CustomerService service;
    @Mock private UserService userService;
    @Mock private OrderService orderService;
    @Mock private Validator validator;
    @Mock private RatingService rating;
    @Mock private FavoriteService favorite;

    private CustomerController controller;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new CustomerController(service, userService, orderService, validator, rating, favorite);
    }

    @Test
    void register_success() {
        var req = new UserRegisterRequest("Sara", "+96171123456", "sara@mail.com", "secret88", "Byblos", Role.CUSTOMER);
        var saved = new Customer("Sara", "+96171123456", "secret88", "Byblos");

        when(service.registerCustomer(any(UserRegisterRequest.class))).thenReturn(saved);

        UserProfileResponse resp = controller.register(req);

        assertEquals("Sara", resp.name());
        assertEquals(Role.CUSTOMER, resp.role());
        verify(service).registerCustomer(any(UserRegisterRequest.class));
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

    @Test
    void change_phone_success() {
        var updated = new Customer("Sara", "+96176667777", "secret88", "Byblos");

        when(service.changePhone(1L, "+96176667777")).thenReturn(updated);

        UserProfileResponse resp = controller.changePhone(1L, "+96176667777");

        assertEquals("+96176667777", resp.phone());
        verify(service).changePhone(1L, "+96176667777");
    }

    @Test
    void update_success() {
        var updated = new Customer("Sara", "+96171123456", "secret88", "Jounieh");

        when(service.updateAddress(1L, "Jounieh")).thenReturn(updated);

        var patch = new UserUpdateRequest(null, "Jounieh", null);

        UserProfileResponse resp = controller.update(1L, patch);

        assertEquals("Jounieh", resp.address());
        verify(service).updateAddress(1L, "Jounieh");
    }

    @Test
    void change_password_success() {
        var updated = new Customer("Sara", "+96171123456", "newSecurePwd", "Byblos");

        when(service.changePassword(1L, "newSecurePwd")).thenReturn(updated);

        UserProfileResponse resp = controller.changePassword(1L, "newSecurePwd");

        assertEquals("Sara", resp.name());
        verify(service).changePassword(1L, "newSecurePwd");
    }

    @Test
    void place_order_success() throws Exception {
        var dto = new CustomerOrderRequest(1L, List.of(1L, 2L), null, null);
        var customer = new Customer("Sara", "+96171123456", "pass", "Byblos");
        customer.setId(1L);

        var restaurant = new Restaurant("PizzaHut", "Hamra", "+961", null, "logo", 10, 2);
        restaurant.setId(1L);

        var item1 = new MenuItem("Pizza", 12.5); item1.setRestaurant(restaurant);
        var item2 = new MenuItem("Pepsi", 3.0);  item2.setRestaurant(restaurant);

        var order = new FoodOrder();
        order.setId(77L);
        order.setCustomer(customer);
        order.setRestaurant(restaurant); // required
        order.setItems(List.of(item1, item2));   // required
        order.setStatus(FoodOrder.Status.PLACED);

        when(userService.findById(1L)).thenReturn(customer);
        when(orderService.placeOrder(customer, dto)).thenReturn(order);

        OrderResponse resp = controller.placeOrder(dto, 1L);

        assertEquals(77L, resp.id());
        assertEquals(FoodOrder.Status.PLACED.toString(), resp.status());
        verify(orderService).placeOrder(customer, dto);
    }


    @Test
    void get_order_history_success() {
        var customer = new Customer("Sara", "+96171123456", "pass", "Byblos");
        customer.setId(1L);

        var restaurant = new Restaurant("Burger King", "Furn el-Chebbak", "+961", null, "logo", 10, 2);
        restaurant.setId(1L);

        var item1 = new MenuItem("Burger", 10); item1.setRestaurant(restaurant);
        var item2 = new MenuItem("Fries", 5);   item2.setRestaurant(restaurant);

        var o1 = new FoodOrder(); o1.setId(1L); o1.setCustomer(customer); o1.setRestaurant(restaurant); o1.setItems(List.of(item1));
        var o2 = new FoodOrder(); o2.setId(2L); o2.setCustomer(customer); o2.setRestaurant(restaurant); o2.setItems(List.of(item2));

        when(userService.findById(1L)).thenReturn(customer);
        when(orderService.getOrderHistory(customer)).thenReturn(List.of(o1, o2));

        List<OrderResponse> result = controller.getOrderHistory(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
    }

}
