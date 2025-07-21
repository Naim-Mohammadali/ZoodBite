package controller;

import dto.order.CustomerOrderRequest;
import dto.order.OrderResponse;
import dto.rating.RatingRequestDto;
import dto.restaurant.RestaurantResponseDto;
import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import model.*;
import service.*;
import util.mapper.OrderMapper;
import util.mapper.RestaurantMapper;
import util.mapper.UserMapper;

import java.util.List;
import java.util.Set;

public class CustomerController {

    private final CustomerService service;
    private final UserService     userService;
    private final OrderService    orderService;
    private final Validator       validator;
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
                              Validator validator, RatingService rating, FavoriteService favorite, CouponService couponService) {
        this.service      = service;
        this.userService  = userService;
        this.orderService = orderService;
        this.validator    = validator;
        this.rating = rating;
        this.favorite = favorite;
        this.couponService = couponService;
    }


    public UserProfileResponse register(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.CUSTOMER);

        Customer saved = service.registerCustomer(fixed);
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse view(long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    public UserProfileResponse update(long id, UserUpdateRequest patch) {
        validate(patch);
        Customer updated = service.updateAddress(id, patch.address() == null ? "" : patch.address());
        return UserMapper.toDto(updated);
    }

    public UserProfileResponse changePhone(long id, String phone) {
        Customer c = service.changePhone(id, phone);
        return UserMapper.toDto(c);
    }

    public UserProfileResponse changePassword(long id, String newPwd) {
        Customer c = service.changePassword(id, newPwd);
        return UserMapper.toDto(c);
    }

    @POST
    @Path("/orders")
    @RolesAllowed("customer")
    public OrderResponse placeOrder(CustomerOrderRequest dto,
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
        orderService.updateOrder(saved); // this must call DAO update

        return OrderMapper.toDto(saved);
    }

    @GET
    @Path("/orders")
    @RolesAllowed("customer")
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
    public void rate(RatingRequestDto dto, @QueryParam("userId") long userId) {
        Customer customer = (Customer) userService.findById(userId);
        rating.addOrUpdate(customer, dto.restaurantId(), dto.score());
    }

    @GET
    @Path("/ratings/{restaurantId}")
    public double getAverageRating(@PathParam("restaurantId") long id) {
        return rating.getAverageRating(id);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
