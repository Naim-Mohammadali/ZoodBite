package controller;

import dto.menuitem.MenuItemResponse;
import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantCreateDto;
import dto.restaurant.RestaurantUpdateDto;
import dto.restaurant.RestaurantResponseDto;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.Restaurant;
import model.Seller;
import service.RestaurantService;
import util.mapper.MenuItemMapper;
import util.mapper.RestaurantMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantController {


    private final RestaurantService restaurantService;
    private final Validator         validator;

    public RestaurantController() {
        this(new RestaurantService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public RestaurantController(RestaurantService restaurantService,
                                Validator validator) {
        this.restaurantService = restaurantService;
        this.validator         = validator;
    }

    @POST
    @Operation(summary = "Register a new restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public RestaurantResponseDto register(Seller currentSeller,
                                          RestaurantCreateDto dto) {
        validate(dto);

        Restaurant saved = new Restaurant(
                dto.name(), dto.address(), dto.phone(),
                currentSeller, dto.logoBase64(),
                dto.taxFee(), dto.additionalFee());

        restaurantService.registerRestaurant(saved, currentSeller);
        return RestaurantMapper.toDto(saved);
    }

    @PATCH @Path("/{restaurantId}")
    @Operation(summary = "Update restaurant details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Seller does not own restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto update(
            @PathParam("restaurantId") long restaurantId,
            Seller currentSeller,
            RestaurantUpdateDto dto)
    throws Exception {
        validate(dto);

        Restaurant r = restaurantService.findByIdAndSeller(restaurantId,
                currentSeller.getId());
        if (r == null)
            throw new IllegalArgumentException("Seller does not own restaurant #" + restaurantId);

        if (dto.name()    != null) r.setName(dto.name());
        if (dto.address() != null) r.setAddress(dto.address());
        if (dto.phone()   != null) r.setPhone(dto.phone());
        if (dto.logoBase64()    != null) r.setLogoBase64(dto.logoBase64());
        if (dto.taxFee()        != null) r.setTaxFee(dto.taxFee());
        if (dto.additionalFee() != null) r.setAdditionalFee(dto.additionalFee());

        restaurantService.updateRestaurant(r);
        return RestaurantMapper.toDto(r);
    }
    @GET @Path("/mine")
    @Operation(summary = "List restaurants of current seller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully")
    })
    public List<RestaurantResponseDto> listMine(Seller currentSeller) {
        return restaurantService.getRestaurantsBySeller(currentSeller)
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }
    @GET
    @Operation(summary = "List all active restaurants for customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active restaurants listed successfully")
    })
    public List<RestaurantResponseDto> browse() {
        return restaurantService.listActive()
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @GET @Path("/{id}")
    @Operation(summary = "View details of a specific restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto view(@PathParam("id") long id)
    {
        return RestaurantMapper.toDto(restaurantService.findById(id));
    }
    @PATCH @Path("/{id}/approve")
    @Operation(summary = "Approve a restaurant (Pending → Active)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant approved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto approve(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.approveRestaurant(r);      // PENDING → ACTIVE
        return RestaurantMapper.toDto(r);
    }
    @PATCH @Path("/{id}/block")
    @Operation(summary = "Block a restaurant (Active → Blocked)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant blocked successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })

    public RestaurantResponseDto block(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.blockRestaurant(r);
        return RestaurantMapper.toDto(r);
    }
    @PATCH @Path("/{id}/unblock")
    @Operation(summary = "Unblock a restaurant (Blocked → Active)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant unblocked successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto unblock(long id) throws Exception {
        Restaurant r = restaurantService.findById(id);
        restaurantService.unblockRestaurant(r);
        return RestaurantMapper.toDto(r);
    }

    @GET @Path("/search")
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
        return restaurantService.search(kw, cat, min, max)
                .stream()
                .map(RestaurantMapper::toBriefDto)
                .collect(Collectors.toList());
    }

    @GET @Path("/{restaurantId}/menu")
    @Operation(summary = "Get menu items for a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public List<MenuItemResponse> menu(@PathParam("restaurantId") long restaurantId)
    throws Exception {
        Restaurant r = restaurantService.findById(restaurantId);
        return r.getMenuItems()
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
