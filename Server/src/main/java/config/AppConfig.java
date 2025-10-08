package config;
import controller.*;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import util.AuthFilter;

@ApplicationPath("/")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        // Resources
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
        register(SellerController.class);
        register(RatingController.class);

        // JSON + Security
        register(JacksonFeature.class);
        register(RolesAllowedDynamicFeature.class);
        register(AuthFilter.class);
        register(util.GlobalValidationExceptionMapper.class);
        register(util.GlobalRuntimeExceptionMapper.class);

        packages("io.swagger.v3.jaxrs2.integration.resources");
    }
}
