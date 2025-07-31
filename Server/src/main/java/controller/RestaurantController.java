package controller;

import dto.menuitem.MenuItemCreateRequest;
import dto.menuitem.MenuItemResponse;
import dto.restaurant.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;
import service.MenuItemService;
import service.RestaurantService;
import service.SellerService;
import util.mapper.MenuItemMapper;
import util.mapper.RestaurantMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final SellerService     sellerService;
    private final Validator         validator;

    public RestaurantController() {
        this(new RestaurantService(),
                new SellerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public RestaurantController(RestaurantService restaurantService,
                                SellerService sellerService,
                                Validator validator) {
        this.restaurantService = restaurantService;
        this.sellerService     = sellerService;
        this.validator         = validator;
    }

    @POST
    @RolesAllowed("seller")
    @Operation(summary = "Register a new restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public RestaurantResponseDto register(
            @HeaderParam("Authorization") String token,
            @Valid RestaurantCreateDto dto) throws Exception {

        validate(dto);
        Seller currentSeller = extractSeller(token);

        Restaurant saved = new Restaurant(
                dto.name(), dto.address(), dto.phone(),
                currentSeller, dto.logoBase64(),
                dto.taxFee(), dto.additionalFee());

        restaurantService.registerRestaurant(saved, currentSeller);
        return RestaurantMapper.toDto(saved);
    }

    @PATCH
    @RolesAllowed("seller")
    @Path("/{restaurantId}")
    @Operation(summary = "Update restaurant details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Seller does not own restaurant"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto update(
            @PathParam("restaurantId") long restaurantId,
            @HeaderParam("Authorization") String token,
            @Valid RestaurantUpdateDto dto)
            throws Exception {

        validate(dto);
        Seller currentSeller = extractSeller(token);
        Restaurant r = restaurantService.findByIdAndSeller(restaurantId, currentSeller.getId());
        if (r == null)
            throw new IllegalArgumentException("Seller does not own restaurant #" + restaurantId);

        if (dto.name()          != null) r.setName(dto.name());
        if (dto.address()       != null) r.setAddress(dto.address());
        if (dto.phone()         != null) r.setPhone(dto.phone());
        if (dto.logoBase64()    != null) r.setLogoBase64(dto.logoBase64());
        if (dto.taxFee()        != null) r.setTaxFee(dto.taxFee());
        if (dto.additionalFee() != null) r.setAdditionalFee(dto.additionalFee());

        restaurantService.updateRestaurant(r);
        return RestaurantMapper.toDto(r);
    }

    @GET
    @Path("/mine")
    @Operation(summary = "List restaurants of current seller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully")
    })
    public List<RestaurantResponseDto> listMine(@HeaderParam("Authorization") String token) {
        Seller currentSeller = extractSeller(token);
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

    @GET
    @Path("/{id}")
    @Operation(summary = "View details of a specific restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponseDto view(@PathParam("id") long id) {
        return RestaurantMapper.toDto(restaurantService.findById(id));
    }





    @GET
    @Path("/{restaurantId}/menu")
    @Operation(summary = "Get menu items for a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public List<MenuItemResponse> menu(@PathParam("restaurantId") long restaurantId) throws Exception {
        Restaurant r = restaurantService.findById(restaurantId);
        return r.getMenuItems()
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }
    @POST
    @Path("/{restaurantId}/menu")
    @RolesAllowed("seller")
    public Response createMenu(@PathParam("restaurantId") long restaurantId,
                               MenuCreateRequest request) {
        Restaurant r = restaurantService.findById(restaurantId);
        restaurantService.createEmptyMenu(r, request.title());
        return Response.ok(Map.of("created_menu", request.title())).build();
    }


    @PUT
    @Path("/{restaurantId}/menu/{menuTitle}")
    @RolesAllowed("seller")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemsToMenu(@PathParam("restaurantId") long restaurantId,
                                   @PathParam("menuTitle") String title,
                                   @Valid MenuItemPatchRequest request) throws Exception {

        Restaurant r = restaurantService.findById(restaurantId);
        MenuItemService menuItemService = new MenuItemService();

        for (Long itemId : request.item_ids()) {
            MenuItem item = menuItemService.getById(itemId);
            restaurantService.addItemToMenu(r, title, item);
        }

        return Response.ok(
                Map.of("menu", title, "added_items", request.item_ids())
        ).build();
    }


    @DELETE
    @Path("/{restaurantId}/menu/{menuTitle}")
    @RolesAllowed("seller")
    public Response removeItemFromMenu(@PathParam("restaurantId") long restaurantId,
                                       @PathParam("menuTitle") String title,
                                       @Valid ItemIdDto dto) throws Exception {
        MenuItemService menuItemService = new MenuItemService();
        Restaurant r = restaurantService.findById(restaurantId);
        MenuItem item = menuItemService.getById(Long.valueOf((dto.itemId())));
        restaurantService.removeItemFromMenu(r, title, item);
        return Response.ok(Map.of("menu", title, "removed_item", item.getId())).build();
    }

    @GET
    @Path("/{restaurantId}/menu/{menuTitle}")
    public List<MenuItemResponse> getMenuItems(@PathParam("restaurantId") long restaurantId,
                                                  @PathParam("menuTitle") String title) {
        Restaurant r = restaurantService.findById(restaurantId);
        return restaurantService.getMenuItems(r, title)
                .stream().map(MenuItemMapper::toDto)
                .toList();
    }




    private Seller extractSeller(String token) {
        long userId = TokenUtil.decodeUserId(token);
        return (Seller) sellerService.findById(userId);
    }
    @POST
    @Path("/{restaurantId}/item")
    @RolesAllowed("seller")
    @Operation(summary = "Add item to restaurant via nested path")
    public MenuItemResponse addItem(@PathParam("restaurantId") long restaurantId,
                                    @HeaderParam("Authorization") String token,
                                    @Valid MenuItemCreateRequest dto) throws Exception {
        Seller seller = (Seller) sellerService.findById(TokenUtil.decodeUserId(token));
        Restaurant restaurant = restaurantService.findById(restaurantId);

        if (!restaurant.getSeller().getId().equals(seller.getId())) {
            throw new ForbiddenException("You do not own this restaurant.");
        }

        return new MenuItemController().add(token, restaurantId, dto);

    }



    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
