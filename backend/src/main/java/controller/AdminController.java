package controller;

import dao.MenuItemDAO;
import dto.admin.AdminRestaurantStatusPatch;
import dto.admin.AdminUserRolePatch;
import dto.admin.ChangePasswordRequest;
import dto.order.OrderResponse;
import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import dto.restaurant.RestaurantResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import service.*;
import util.mapper.*;

import java.util.*;
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
    @Operation(summary = "Register a new admin account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public UserProfileResponse registerAdmin(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.ADMIN, null);

        Admin saved = adminService.registerAdmin(fixed);
        return UserMapper.toDto(saved);
    }
    @GET @Path("/accounts")
    @Operation(summary = "List users by role (or all users if role is omitted)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role specified")
    })
    public List<UserProfileResponse> listUsers(@QueryParam("role") String roleOpt) {
        List<User> list = roleOpt == null
                ? adminService.listAllUsers()
                : adminService.findByRole(Role.valueOf(roleOpt));

        return list.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @PATCH @Path("/accounts/{id}")
    @Operation(summary = "Update admin user profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Admin user not found")
    })
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
    @Operation(summary = "Block a user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User account blocked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserProfileResponse blockUser(@PathParam("id") long userId) {
        return UserMapper.toDto(adminService.blockUser(userId));
    }

    @PATCH @Path("/accounts/{id}/unblock")
    @Operation(summary = "Unblock a user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User account unblocked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserProfileResponse unblockUser(@PathParam("id") long userId) {
        return UserMapper.toDto(adminService.unblockUser(userId));
    }

    @PATCH @Path("/accounts/{id}/role")
    @Operation(summary = "Change the role of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserProfileResponse changeRole(@PathParam("id") long userId,
                                          AdminUserRolePatch dto) {
        validate(dto);
        User updated = adminService.changeRole(userId, Role.valueOf(dto.role()));
        return UserMapper.toDto(updated);
    }


    @PATCH @Path("/accounts/{id}/password")
    @Operation(summary = "Change user password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserProfileResponse changePassword(@PathParam("id") long id,
                                              ChangePasswordRequest request) {
        validate(request);
        return UserMapper.toDto(adminService.changePassword(id, request.newPassword()));
    }


    @GET @Path("/restaurants")
    @Operation(summary = "List restaurants by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants listed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
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
    @Operation(summary = "Update restaurant status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or input"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
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
    @PATCH
    @Operation(summary = "Approve a restaurant (Pending → Active)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant approved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto approve(@PathParam("id") long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.approveRestaurant(r);
        return RestaurantMapper.toDto(r);
    }

    @PATCH
    @Path("restaurants/{id}/block")
    @Operation(summary = "Block a restaurant (Active → Blocked)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant blocked successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto block(@PathParam("id") long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.blockRestaurant(r);
        return RestaurantMapper.toDto(r);
    }

    @PATCH
    @Path("restaurants/{id}/unblock")
    @Operation(summary = "Unblock a restaurant (Blocked → Active)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant unblocked successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto unblock(@PathParam("id") long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.unblockRestaurant(r);
        return RestaurantMapper.toDto(r);
    }



    @GET
    @Path("/orders")
    @Operation(summary = "List all orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders listed successfully")
    })
    public List<OrderResponse> listOrders() {
        List<FoodOrder> list = orderService.getAllOrders();  // New method
        return list.stream().map(OrderMapper::toDto).toList();
    }

    @GET
    @Path("/orders/{id}")
    @RolesAllowed("customer")
    @Operation(summary = "Get order history for a customer")
    public List<OrderResponse> getOrderHistory(@PathParam("id") String token) {
        Customer customer = extractCustomer(token);
        return orderService.getOrderHistory(customer).stream()
                .map(OrderMapper::toDto)
                .toList();
    }
    @PATCH
    @Path("/users/{seller_id}/status")
    @Operation(summary = "Approve or block user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response patchUserStatus(@PathParam("seller_id") long userId,
                                    AdminRestaurantStatusPatch dto) throws Exception {
        validate(dto);

        User u = adminService.findById(userId);
        if (dto.status().toLowerCase().contains("approve")) {
            if (u.getRole() == Role.SELLER) {
                for (Restaurant r : restaurantService.getRestaurantsBySeller((Seller) u))
                    restaurantService.approveRestaurant(r);
            }
            u.setStatus(User.Status.ACTIVE);
        }
        else
            u.setStatus(User.Status.PENDING);
        adminService.update(u);

        return Response.ok()
                .entity(Map.of("user_id", u.getId(), "new_status", u.getStatus().name()))
                .build();
    }
    private Customer extractCustomer(String token) {
        long userId = TokenUtil.decodeUserId(token);
        CustomerService customerService = new CustomerService();
        return (Customer) customerService.findById(userId);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
