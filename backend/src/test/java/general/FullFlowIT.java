package general;

import controller.*;
import dto.favorite.FavoriteActionDto;
import dto.menuitem.MenuItemCreateRequest;
import dto.order.OrderCreateRequest;
import dto.order.OrderResponse;
import dto.restaurant.RestaurantCreateDto;
import dto.user.request.UserRegisterRequest;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import model.*;
import org.junit.jupiter.api.*;
import service.*;
import util.mapper.MenuItemMapper;
import util.mapper.RestaurantMapper;
import util.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FullFlowIT {

    /* ---------- boot real persistence with H2 ---------- */
    private static EntityManagerFactory emf;

    /* ---------- shared controllers ---------- */
    private static UserController       userCtl;
    private static RestaurantController restCtl;
    private static MenuItemController   itemCtl;
    private static FavoriteController   favCtl;
    private static OrderController      orderCtl;

    /* ---------- domain refs ---------- */
    private static Customer customer;
    private static Seller   seller;
    private static Restaurant restaurant;
    private static MenuItem  burger;

    /* ---------- one-time init ---------- */
    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("test-h2");

        Validator v = Validation.buildDefaultValidatorFactory().getValidator();
        userCtl  = new UserController(new UserService(), v);
        restCtl  = new RestaurantController(new RestaurantService(), v);
        itemCtl  = new MenuItemController(new MenuItemService(), v);
        favCtl   = new FavoriteController(new FavoriteService(), new RestaurantService(), v);
        orderCtl = new OrderController(new OrderService(), new MenuItemService(), v);
    }

    /* ---------- 1. register users ---------- */
    @Test @Order(1)
    void register_users() {
        var regDtoCustomer = new UserRegisterRequest("Ali","+96171","a@mail","pw","Beirut", Role.CUSTOMER);
        customer   = (Customer) UserMapper.toEntity(regDtoCustomer);
        var regDtoSeller = new UserRegisterRequest("Bob","+96170","b@mail","pw","Beirut", Role.SELLER);
        seller = (Seller) UserMapper.toEntity(regDtoSeller);
        userCtl.register(UserMapper.toRequest(customer));
        userCtl.register(UserMapper.toRequest(seller));

        assertNotNull(customer.getId());
        assertNotNull(seller.getId());
    }

    /* ---------- 2. seller creates restaurant & menu item ---------- */
    @Test @Order(2)
    void seller_creates_restaurant() throws Exception {
        RestaurantCreateDto dto = new RestaurantCreateDto("Bob Grill","Hamra","+96170",null,5,2);
        restaurant = RestaurantMapper.fromCreateDto(dto, seller);

        MenuItemCreateRequest itemReq = new MenuItemCreateRequest("Burger","Delicious",9.9,10,null,"Fast");
        burger = MenuItemMapper.fromCreateRequest(itemReq, restaurant);

        assertEquals(9.9, burger.getPrice());
    }

    /* ---------- 3. customer favourites restaurant ---------- */
    @Test @Order(3)
    void favourite() throws Exception {
        favCtl.add(customer, new FavoriteActionDto(restaurant.getId()));
        List<?> list = favCtl.list(customer);
        assertEquals(1, list.size());
    }

    /* ---------- 4. browse ---------- */
    @Test @Order(4)
    void browse() {
        List<?> list = restCtl.browse();   // returns brief DTOs
        assertFalse(list.isEmpty());
    }

    /* ---------- 5. place order ---------- */
    @Test @Order(5)
    void place_order() throws Exception {
        OrderResponse resp = orderCtl.place(customer,
                new OrderCreateRequest(restaurant.getId(), List.of(burger.getId())));

        assertEquals("PLACED", resp.status());
        assertEquals(restaurant.getId(), resp.restaurantId());
    }

    /* ---------- clean-up ---------- */
    @AfterAll
    static void tearDown() {
        emf.close();
    }
}
