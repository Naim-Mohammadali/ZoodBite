package controller;

import dto.admin.AdminRestaurantStatusPatch;
import dto.admin.AdminUserRolePatch;
import dto.order.OrderResponse;
import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import dto.restaurant.RestaurantResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.*;
import service.*;
import util.mapper.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RolesAllowed("admin")
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminController {

    private final AdminService       adminService;
    private final RestaurantService  restaurantService;
    private final OrderService       orderService;
    private final Validator          validator;

    public AdminController() {
        this(new AdminService(),
                new RestaurantService(),
                new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public AdminController(AdminService adminService,
                           RestaurantService restaurantService,
                           OrderService orderService,
                           Validator validator) {
        this.adminService      = adminService;
        this.restaurantService = restaurantService;
        this.orderService      = orderService;
        this.validator         = validator;
    }

    @POST @Path("/accounts")
    public UserProfileResponse registerAdmin(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.ADMIN);

        Admin saved = adminService.registerAdmin(fixed);
        return UserMapper.toDto(saved);
    }

    @GET @Path("/accounts")
    public List<UserProfileResponse> listUsers(@QueryParam("role") String roleOpt) {
        List<User> list = roleOpt == null
                ? adminService.listAllUsers()
                : adminService.findByRole(Role.valueOf(roleOpt));

        return list.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @PATCH @Path("/accounts/{id}")
    public UserProfileResponse updateProfile(@PathParam("id") long id,
                                             UserUpdateRequest patch) {
        validate(patch);

        Admin admin = (Admin) adminService.findById(id);
        if (patch.name()  != null) admin.setName(patch.name());
        if (patch.email() != null) admin.setEmail(patch.email());

        Admin saved = (Admin) adminService.update(admin);
        return UserMapper.toDto(saved);
    }

    @PATCH @Path("/accounts/{id}/block")
    public UserProfileResponse blockUser(@PathParam("id") long userId) {
        return UserMapper.toDto(adminService.blockUser(userId));
    }

    @PATCH @Path("/accounts/{id}/unblock")
    public UserProfileResponse unblockUser(@PathParam("id") long userId) {
        return UserMapper.toDto(adminService.unblockUser(userId));
    }

    @PATCH @Path("/accounts/{id}/role")
    public UserProfileResponse changeRole(@PathParam("id") long userId,
                                          AdminUserRolePatch dto) {
        validate(dto);
        User updated = adminService.changeRole(userId, Role.valueOf(dto.role()));
        return UserMapper.toDto(updated);
    }


    @PATCH @Path("/accounts/{id}/password")
    public UserProfileResponse changePassword(@PathParam("id") long id,
                                              String newPwd) {
        return UserMapper.toDto(adminService.changePassword(id, newPwd));
    }

    @GET @Path("/restaurants")
    public List<RestaurantResponseDto> listRestaurants(
            @QueryParam("status") String statusOpt) {

        Restaurant.Status s = statusOpt == null ? null
                : Restaurant.Status.valueOf(statusOpt);

        List<Restaurant> list = s == null
                ? restaurantService.getByStatus(Restaurant.Status.ACTIVE)
                : restaurantService.getByStatus(s);

        return list.stream().map(RestaurantMapper::toDto).toList();
    }

    @PATCH @Path("/restaurants/{id}")
    public RestaurantResponseDto patchRestaurant(@PathParam("id") long restId,
                                              AdminRestaurantStatusPatch dto)
            throws Exception {

        validate(dto);
        Restaurant r = restaurantService.findById(restId);
        Restaurant.Status newS = Restaurant.Status.valueOf(dto.status());

        switch (newS) {
            case ACTIVE  -> restaurantService.approveRestaurant(r);  // pending → active
            case BLOCKED -> restaurantService.blockRestaurant(r);
            default      -> throw new IllegalArgumentException("Unsupported status change");
        }
        return RestaurantMapper.toDto(r);
    }

    @GET @Path("/orders")
    public List<OrderResponse> listOrders(@QueryParam("status") String statusOpt) {
        FoodOrder.Status s = statusOpt == null ? null
                : FoodOrder.Status.valueOf(statusOpt);
        List<FoodOrder> list = s == null
                ? orderService.getOrdersByStatus(null)     // implement findAll inside service
                : orderService.getOrdersByStatus(s);
        return list.stream().map(OrderMapper::toDto).toList();
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
