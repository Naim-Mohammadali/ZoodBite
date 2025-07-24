package config;
import com.fasterxml.jackson.core.util.JacksonFeature;
import controller.*;
import jakarta.ws.rs.ApplicationPath;
import model.Menu;
import org.glassfish.jersey.server.ResourceConfig;
@ApplicationPath("/")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        register(AdminController.class);
        register(CourierController.class);
        register(CouponController.class);
        register(CustomerController.class);
        register(DeliveryController.class);
        register(FavoriteController.class);
        register(MenuItemController.class);
        register(OrderController.class);
        register(RestaurantController.class);
        register(SellerOrderController.class);
        register(SwaggerController.class);
        register(UserController.class);
        register(RootController.class);
        register(JacksonFeature.class);
        register(RootController.class);
        register(AdminController.class);
        register(AuthController.class);
        register(DeliveryController.class);
        register(FavoriteController.class);
        register(RootController.class);
        register(SellerController.class);
        register(Menu.class);
        register(RatingController.class);

        System.out.println("✅ AppConfig initialized");

        // Optional: Exception handling
        // register(GenericExceptionMapper.class);
        packages("org.glassfish.jersey.media.json.binding");
        packages("io.swagger.v3.jaxrs2.integration.resources"); // Swagger
    }
}
