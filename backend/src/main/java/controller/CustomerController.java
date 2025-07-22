package controller;

import dto.admin.ChangePasswordRequest;
import dto.customer.ChangePhoneRequest;
import dto.order.CustomerOrderRequest;
import dto.order.OrderResponse;
import dto.rating.RatingRequestDto;
import dto.restaurant.RestaurantResponseDto;
import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import model.*;
import service.*;
import util.mapper.OrderMapper;
import util.mapper.RestaurantMapper;
import util.mapper.UserMapper;

import java.util.List;
import java.util.Set;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    private final CustomerService service;
    private final UserService userService;
    private final OrderService orderService;
    private final Validator validator;
    private final RatingService rating;
    private final FavoriteService favorite;
    private final CouponService couponService;

    public CustomerController(RatingService rating, FavoriteService favorite, CouponService couponService) {
        this(new CustomerService(),
                new UserService(),
                new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator(), rating, favorite, couponService);
    }

    public CustomerController(CustomerService service,
                              UserService userService,
                              OrderService orderService,
                              Validator validator,
                              RatingService rating,
                              FavoriteService favorite,
                              CouponService couponService) {
        this.service = service;
        this.userService = userService;
        this.orderService = orderService;
        this.validator = validator;
        this.rating = rating;
        this.favorite = favorite;
        this.couponService = couponService;
    }

    @POST
    @Path("/register")
    @Operation(summary = "Register a new customer account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer account created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public UserProfileResponse register(@Valid UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.CUSTOMER);

        Customer saved = service.registerCustomer(fixed);
        return UserMapper.toDto(saved);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "View customer profile")
    public UserProfileResponse view(@PathParam("id") long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Update customer profile")
    public UserProfileResponse update(@PathParam("id") long id,
                                      @Valid UserUpdateRequest patch) {
        validate(patch);
        Customer updated = service.updateAddress(id, patch.address() == null ? "" : patch.address());
        return UserMapper.toDto(updated);
    }

    @PATCH
    @Path("/{id}/phone")
    @Operation(summary = "Change customer phone number")
    public UserProfileResponse changePhone(@PathParam("id") long id,
                                           @Valid ChangePhoneRequest request) {
        Customer c = service.changePhone(id, request.phone());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/{id}/password")
    @Operation(summary = "Change customer password")
    public UserProfileResponse changePassword(@PathParam("id") long id,
                                              @Valid ChangePasswordRequest request) {
        Customer c = service.changePassword(id, request.newPassword());
        return UserMapper.toDto(c);
    }

    @POST
    @Path("/orders")
    @RolesAllowed("customer")
    @Operation(summary = "Place a new order")
    public OrderResponse placeOrder(@Valid CustomerOrderRequest dto,
                                    @QueryParam("userId") long userId) throws Exception {
        Customer customer = (Customer) userService.findById(userId);

        if (dto.couponCode() == null || dto.couponCode().isBlank()) {
            FoodOrder saved = orderService.placeOrder(customer, dto);
            return OrderMapper.toDto(saved);
        }

        Coupon coupon = couponService.findValidCoupon(dto.couponCode());
        FoodOrder saved = orderService.placeOrder(customer, dto);

        double discounted = saved.getTotal() * (100 - coupon.getDiscountPercent()) / 100.0;
        saved.setTotal(discounted);
        saved.setCouponCode(coupon.getCode());

        couponService.incrementUsage(coupon);
        orderService.updateOrder(saved);

        return OrderMapper.toDto(saved);
    }

    @GET
    @Path("/orders")
    @RolesAllowed("customer")
    @Operation(summary = "Get order history for a customer")
    public List<OrderResponse> getOrderHistory(@QueryParam("userId") long userId) {
        Customer customer = (Customer) userService.findById(userId);
        return orderService.getOrderHistory(customer)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @GET
    @Path("/favorites")
    @RolesAllowed("customer")
    @Operation(summary = "List favorite restaurants")
    public List<RestaurantResponseDto> listFavorites(@QueryParam("userId") long userId) {
        Customer customer = (Customer) userService.findById(userId);
        return favorite.list(customer)
                .stream()
                .map(RestaurantMapper::toDto)
                .toList();
    }

    @POST
    @Path("/ratings")
    @RolesAllowed("customer")
    @Operation(summary = "Rate a restaurant")
    public void rate(@Valid RatingRequestDto dto,
                     @QueryParam("userId") long userId) {
        Customer customer = (Customer) userService.findById(userId);
        rating.addOrUpdate(customer, dto.restaurantId(), dto.score());
    }

    @GET
    @Path("/ratings/{restaurantId}")
    @Operation(summary = "Get average rating for a restaurant")
    public double getAverageRating(@PathParam("restaurantId") long id) {
        return rating.getAverageRating(id);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
