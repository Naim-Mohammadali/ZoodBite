import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SessionManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Splash.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("ZoodBite");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.getIcons().add(
                new javafx.scene.image.Image(getClass().getResourceAsStream("/assets/logo/blueLogo.png"))
        );

    }
    @Override
    public void stop() {
        SessionManager.getInstance().deleteTempFile();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

//import network.dto.admin.CreateCouponRequestDto;
//import network.dto.order.PlaceOrderItemDto;
//import network.dto.order.PlaceOrderRequestDto;
//import network.dto.order.PlaceOrderResponseDto;
//import network.dto.user.LoginRequestDto;
//import network.dto.user.RestaurantResponseDto;
//import network.dto.user.UserDto;
//import network.dto.user.UserUpdateRequestDto;
//import network.endpoint.*;
//import util.SessionManager;
//import java.util.List;
//            ----- Login User -----
//            var dto0 = new LoginRequestDto("9890274654", "SecurePass123!");
//            var endpoint = new UserEndpoint();
//            var response = endpoint.login(dto0);
//            SessionManager.getInstance().setToken(response.token);
//            SessionManager.getInstance().setLoggedInUser(response.user);
//            System.out.println("Login success. Token: " + response.token);
//            System.out.println("User: " + response.user.name);


//            ----- Register User -----
//            RegisterRequestDto dto = new RegisterRequestDto(
//                    "Alexis Seller",
//                    "9890274654",
//                    "alexis@example.com",
//                    "SecurePass123!",
//                    "456 Seller St",
//                    "SELLER",
//                    new BankInfoDto("Test Bank", "0987654321")
//            );
//            LoginResponseDto response = new UserEndpoint().register(dto);
//            SessionManager.getInstance().setToken(response.token);
//            SessionManager.getInstance().setLoggedInUser(response.user);
//            System.out.println("Registered as: " + response.user.name + " (" + response.user.role + ")");


//            ----- Create Restaurant
//            System.out.println("Token: " + SessionManager.getInstance().getToken());
//            System.out.println("Role: " + SessionManager.getInstance().getLoggedInUser().role);
//            CreateRestaurantRequestDto dto = new CreateRestaurantRequestDto(
//                    "Tehran Burger 4",
//                    "123 Food St",
//                    "093456744435",
//                    9,
//                    2
//            );
//            RestaurantResponseDto restaurant = new RestaurantEndpoint().createRestaurant(dto);
//            System.out.println("Created restaurant: " + restaurant.name + " (ID: " + restaurant.id + ", Status: " + restaurant.status + ")");


//            ----- Admin Approves Seller
//            Long sellerId = 2L;
//            AdminEndpoint admin = new AdminEndpoint();
//            UpdateUserStatusResponseDto response1 = admin.approveSeller(sellerId);
//            System.out.println("User #" + response1.user_id + " status changed to: " + response1.new_status);


//            ----- Buyer View Profile
//            var dto0 = new LoginRequestDto("9890274654", "SecurePass123!");
//            var endpoint = new UserEndpoint();
//            var response = endpoint.login(dto0);
//            SessionManager.getInstance().setToken(response.token);
//            SessionManager.getInstance().setLoggedInUser(response.user);
//            UserDto profile = new UserEndpoint().getProfile();
//            System.out.println("User: " + profile.name + " (" + profile.role + ")");
//            System.out.println("Status: " + profile.status);
//            System.out.println("Phone: " + profile.phone);
//
//            ----- Add item to restaurant
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("9890274654", "SecurePass123!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//
//            System.out.println("✅ Logged in as: " + loginResponse.user.name);
//            long restaurantId = 3;
//            long sellerId = loginResponse.user.id;
//            MenuItemRequestDto itemDto = new MenuItemRequestDto(
//                    "Veg Pizza",
//                    "A veg pizza",
//                    40000,
//                    200,
//                    Arrays.asList("pizza", "veg"),
//                    sellerId
//            );
//            RestaurantEndpoint restaurantEndpoint = new RestaurantEndpoint();
//            MenuItemResponseDto itemResponse = restaurantEndpoint.addMenuItem(restaurantId, itemDto);
//            System.out.println("✅ Item added:");
//            System.out.println("Name: " + itemResponse.name);
//            System.out.println("Price: " + itemResponse.price);
//            System.out.println("Categories: " + itemResponse.categories);


//            Add Menu
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("9890274654", "SecurePass123!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            RestaurantEndpoint restaurantEndpoint = new RestaurantEndpoint();
//            Long restaurantId = 1L;
//            String menuTitle = "Special Courses";
//            MenuCreateResponseDto response = restaurantEndpoint.addMenuToRestaurant(restaurantId, menuTitle);
//            System.out.println("✅ Menu created: " + response.getCreated_menu());
//

//            Add items to menu
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("09120000032", "StrongPass456!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//
//            RestaurantEndpoint restaurantEndpoint = new RestaurantEndpoint();

//            ----- Replace with your real values
//            Long restaurantId = 2L;
//            String menuTitle = "Special Courses";
//            List<Long> itemIds = List.of(1L);
//
//            MenuItemToMenuResponseDto response = restaurantEndpoint.addItemsToMenu(restaurantId, menuTitle, itemIds);
//
//            System.out.println("✅ Items added to menu: " + response.getAdded_items());
//            System.out.println("📋 Menu title: " + response.getMenu());
//            ----- Customer Search
//            CustomerEndpoint customerEndpoint = new CustomerEndpoint();
//            List<RestaurantResponseDto> vendors = customerEndpoint.searchVendors("Burger");
//
//            for (RestaurantResponseDto r : vendors) {
//                System.out.println("🍔 Vendor: " + r.getName() + " | Status: " + r.getStatus());
//                for (RestaurantResponseDto.MenuDto m : r.getMenus()) {
//                    System.out.println("  📋 Menu: " + m.getTitle());
//                    for (RestaurantResponseDto.MenuDto.ItemDto item : m.getItems()) {
//                        System.out.println("    🧾 Item ID: " + item.getItemId());
//                    }
//                }
//            }
//
//
//           ---- Place Order By Customer
//
//
//            CustomerEndpoint ce = new CustomerEndpoint();
//
//            PlaceOrderRequestDto order = new PlaceOrderRequestDto(
//                    2L,
//                    "Tehran, Valiasr Street, No. 10",
//                    List.of(
//                            new PlaceOrderItemDto(2L, 1)
//                    ),
//                    "SUMMER2025" // can also be null
//            );
//
//            PlaceOrderResponseDto response = ce.placeOrder(order);
//
//            System.out.println("✅ Order placed! ID = " + response.id);
//            System.out.println("Vendor: " + response.restaurantName);
//            System.out.println("Total: " + response.total + " | Coupon: " + response.couponCode);
//            System.out.println("Status: " + response.status);
//            ------ Seller Approves order
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("091255555555", "SecurePass123!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            SellerEndpoint seller = new SellerEndpoint();
//            Long orderId = 6L;  // replace with your real test order ID
//            String newStatus = "ACCEPTED";
//
//            try {
//                PlaceOrderResponseDto response = seller.updateOrderStatus(orderId, newStatus);
//
//                System.out.println("✅ Order Status Updated!");
//                System.out.println("Order ID: " + response.id);
//                System.out.println("Restaurant: " + response.restaurantName);
//                System.out.println("Status: " + response.status);
//                System.out.println("Total: " + response.total);
//                System.out.println("Address: " + response.address);
//                System.out.println("Items: " + response.itemIds);
//            } catch (Exception e) {
//                System.err.println("❌ Failed to update order: " + e.getMessage());
//            }


//            Courier view available deliveries
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("09120300003", "CourierPass789!"));
//           SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            // Inside main(), after courier login
//            CourierEndpoint courier = new CourierEndpoint();
//
//            System.out.println("🔎 Available deliveries:");
//            List<PlaceOrderResponseDto> deliveries = courier.getAvailableDeliveries();
//            for (PlaceOrderResponseDto d : deliveries) {
//                System.out.println("🚚 Order ID: " + d.id + ", Total: " + d.total + ", Address: " + d.address);
//            }
//            🟡 Pick the order
//            PlaceOrderResponseDto picked = courier.pickOrder(6L);
//            System.out.println("✅ Picked: " + picked.id + " | Status: " + picked.status);
//            🟢 Deliver the order
//            PlaceOrderResponseDto delivered = courier.deliverOrder(6L);
//            System.out.println("✅ Delivered: " + delivered.id + " | Status: " + delivered.status);


//            Rate order
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("09120000032", "StrongPass456!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            CustomerEndpoint customer = new CustomerEndpoint();
//            customer.rateOrder(6L, 5, "Excellent burger!");
//            System.out.println("✅ Rating submitted!");


//            Update profile
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("09120000032", "StrongPass456!"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            UserUpdateRequestDto update = new UserUpdateRequestDto();
//            update.name = "Buyer Update";
//            update.address = "45 Buyer Ave";
//
//            UserDto updated = new UserEndpoint().updateProfile(update);
//            System.out.println("✅ Profile updated:");
//            System.out.println("Name: " + updated.name);
//            System.out.println("Address: " + updated.address);


//            Create coupon
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("admin", "admin"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            CreateCouponRequestDto coupon = new CreateCouponRequestDto();
//            coupon.code = "SUMMER1404";
//            coupon.value = 25;
//            coupon.min_price = 20000;
//            coupon.user_count = 100;
//            coupon.validFrom = "2025-06-01";
//            coupon.validUntil = "2025-09-01";
//            new AdminEndpoint().createCoupon(coupon);
//
//
//            View all orders by admin
//            var loginResponse = new UserEndpoint().login(new LoginRequestDto("admin", "admin"));
//            SessionManager.getInstance().setToken(loginResponse.token);
//            SessionManager.getInstance().setLoggedInUser(loginResponse.user);
//            System.out.println("✅ Logged in as: " + SessionManager.getInstance().getLoggedInUser().name);
//            AdminEndpoint admin = new AdminEndpoint();
//            List<PlaceOrderResponseDto> allOrders = admin.getAllOrders();
//            System.out.println("📦 Orders:");
//            for (PlaceOrderResponseDto order : allOrders) {
//                System.out.println("🆔 Order ID: " + order.id);
//                System.out.println("🍔 Restaurant: " + order.restaurantName);
//                System.out.println("💵 Total: " + order.total);
//                System.out.println("🚚 Status: " + order.status);
//                System.out.println("📍 Address: " + order.address);
//                System.out.println("🧾 Items: " + order.itemIds);
//                System.out.println("------");
//            }