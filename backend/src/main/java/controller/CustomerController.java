package controller;

import dto.admin.ChangePasswordRequest;
import dto.customer.ChangePhoneRequest;
import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantResponseDto;
import dto.restaurant.RestaurantSearchRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import model.*;
import service.*;
import util.mapper.RestaurantMapper;
import util.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
@Path("/")
@RolesAllowed("customer")
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
    private final RestaurantService restaurantService;

    public CustomerController(RatingService rating, FavoriteService favorite, CouponService couponService, RestaurantService restaurantService) {
        this(new CustomerService(),
                new UserService(),
                new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator(), rating, favorite, couponService, restaurantService);
    }

    public CustomerController(CustomerService service,
                              UserService userService,
                              OrderService orderService,
                              Validator validator,
                              RatingService rating,
                              FavoriteService favorite,
                              CouponService couponService, RestaurantService restaurantService) {
        this.service = service;
        this.userService = userService;
        this.orderService = orderService;
        this.validator = validator;
        this.rating = rating;
        this.favorite = favorite;
        this.couponService = couponService;
        this.restaurantService = restaurantService;
    }
    public CustomerController() {
        this.service =  new CustomerService();
        this.userService = new UserService();
        this.orderService = new OrderService();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.rating = new RatingService();
        this.favorite =new FavoriteService();
        this.couponService = new CouponService();
        this.restaurantService = new RestaurantService();
    }


    @GET
    @Path("/me")
    @Operation(summary = "View customer profile")
    public UserProfileResponse view(@HeaderParam("Authorization") String token) {
        return UserMapper.toDto(service.viewProfile(TokenUtil.decodeUserId(token)));
    }

    @PATCH
    @Path("/me")
    @Operation(summary = "Update customer profile")
    public UserProfileResponse update(@HeaderParam("Authorization") String token,
                                      @Valid UserUpdateRequest patch) {
        validate(patch);
        Customer updated = service.updateAddress(TokenUtil.decodeUserId(token), patch.address() == null ? "" : patch.address());
        return UserMapper.toDto(updated);
    }

    @PATCH
    @Path("/me/phone")
    @Operation(summary = "Change customer phone number")
    public UserProfileResponse changePhone(@HeaderParam("Authorization") String token,
                                           @Valid ChangePhoneRequest request) {
        Customer c = service.changePhone(TokenUtil.decodeUserId(token), request.phone());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/me/password")
    @Operation(summary = "Change customer password")
    public UserProfileResponse changePassword(@HeaderParam("Authorization") String token,
                                              @Valid ChangePasswordRequest request) {
        Customer c = service.changePassword(TokenUtil.decodeUserId(token), request.newPassword());
        return UserMapper.toDto(c);
    }
    private Customer extractCustomer(String token) {
        long userId = TokenUtil.decodeUserId(token);
        return (Customer) userService.findById(userId);
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search restaurants by criteria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants search results retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria")
    })
    public List<RestaurantBriefDto> search(
            @QueryParam("kw") String kw,
            @QueryParam("cat") String cat,
            @QueryParam("min") Double min,
            @QueryParam("max") Double max)
    {

        RestaurantService restaurantService = new RestaurantService();
        return restaurantService.search(kw, cat, min, max)
                .stream()
                .map(RestaurantMapper::toBriefDto)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/vendors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("customer")
    public List<RestaurantResponseDto> searchRestaurants(RestaurantSearchRequest request) {
        String search = request.search();
        List<Restaurant> matches = restaurantService.searchByName(search);
        return matches.stream().map(RestaurantMapper::toDto).toList();
    }



    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
