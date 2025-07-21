package general;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.*;
import service.*;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class IntegrationTest {

    public static void main(String[] args) {
        System.out.println("🚀 Starting FULL INTEGRATION TEST...");

        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Services
        UserService userService = new UserService();
        RestaurantService restaurantService = new RestaurantService();
        MenuItemService menuItemService = new MenuItemService();
        OrderService orderService = new OrderService();

        try {
            tx.begin();

            // 1️⃣ Register users
            Customer customer = (Customer) userService.register("Ali Customer", "0911111111", "pass", Role.CUSTOMER, "Beirut");
            Seller seller = (Seller) userService.register("Sara Seller", "0922222222", "pass", Role.SELLER, "Tripoli");
            Courier courier = (Courier) userService.register("Karim Courier", "0933333333", "pass", Role.COURIER, "Saida");
            System.out.println("✅ Users registered");

            // 2️⃣ Seller creates a restaurant
            Restaurant r = new Restaurant();
            r.setName("Fatima's Kitchen");
            r.setAddress("Tyre");
            r.setPhone("071234567");

            restaurantService.registerRestaurant(r, seller);
            System.out.println("🏪 Restaurant registered");

            // 3️⃣ Approve restaurant
            restaurantService.approveRestaurant(r);
            System.out.println("✅ Restaurant approved");

            // 4️⃣ Add Menu Items
            MenuItem item1 = new MenuItem();
            item1.setName("Mansaf");
            item1.setPrice(8.5);
            item1.setQuantity(20);
            item1.setDescription("Delicious Jordanian lamb dish");
            item1.setRestaurant(r);
            menuItemService.addMenuItem(seller, r,item1);

            MenuItem item2 = new MenuItem();
            item2.setName("Shawarma Plate");
            item2.setPrice(5.0);
            item2.setQuantity(15);
            item2.setRestaurant(r);
            menuItemService.addMenuItem(seller, r, item2);
            System.out.println("🍕 Menu items added");

            // 5️⃣ Place an order
            List<MenuItem> items = List.of(item1, item2);
            orderService.placeOrder(customer, r, items);
            System.out.println("🛒 Order placed");

            // 6️⃣ Fetch order and assign courier
            FoodOrder placedOrder = orderService.getOrdersByCustomer(customer).get(0);
            orderService.assignCourier(seller, placedOrder, courier);
            System.out.println("🚚 Courier assigned: " + courier.getName());

            // 7️⃣ Update order status
            orderService.updateOrderStatus(placedOrder, FoodOrder.Status.PREPARING);
            orderService.updateOrderStatus(placedOrder, FoodOrder.Status.READY_FOR_PICKUP);
            orderService.updateOrderStatus(placedOrder, FoodOrder.Status.IN_TRANSIT);
            orderService.updateOrderStatus(placedOrder, FoodOrder.Status.DELIVERED);
            System.out.println("✅ Order delivered");

            // 8️⃣ List all orders for customer and courier
            System.out.println("📦 Orders for Customer:");
            orderService.getOrdersByCustomer(customer).forEach(System.out::println);

            System.out.println("📦 Orders for Courier:");
            orderService.getOrdersByCourier(courier).forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            EntityManagerFactorySingleton.getInstance().close();
        }

        System.out.println("🏁 Test completed.");
    }
}
