package controller;

import dto.menuitem.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.MenuItem;
import model.Restaurant;
import model.Seller;
import model.TokenUtil;
import service.MenuItemService;
import service.RestaurantService;
import service.SellerService;
import util.mapper.MenuItemMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RolesAllowed("seller")
@Path("/menu-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuItemController {

    private final MenuItemService  service;
    private final RestaurantService restaurantService;
    private final SellerService     sellerService;
    private final Validator         validator;

    public MenuItemController() {
        this(new MenuItemService(), new RestaurantService(), new SellerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public MenuItemController(MenuItemService service,
                              RestaurantService restaurantService,
                              SellerService sellerService,
                              Validator validator) {
        this.service           = service;
        this.restaurantService = restaurantService;
        this.sellerService     = sellerService;
        this.validator         = validator;
    }

    @POST
    @Operation(summary = "Add a new menu item to a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Unauthorized action")
    })
    public MenuItemResponse add(
            @HeaderParam("Authorization") String token,
            @QueryParam("restaurantId") long restaurantId,
            @Valid MenuItemCreateRequest dto) throws Exception {

        validate(dto);
        Seller seller = extractSeller(token);
        Restaurant restaurant = restaurantService.findById(restaurantId);

        MenuItem entity = new MenuItem();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());
        entity.setImgURL(dto.imgURL());
        entity.setCategory(dto.categories());

        service.addMenuItem(seller, restaurant, entity);
        return MenuItemMapper.toDto(entity);
    }

    @PATCH
    @Path("/{itemId}")
    @Operation(summary = "Update menu item details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Unauthorized action"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public MenuItemResponse update(
            @HeaderParam("Authorization") String token,
            @PathParam("itemId") long itemId,
            @Valid MenuItemUpdateRequest dto) throws Exception {

        validate(dto);
        Seller seller = extractSeller(token);
        MenuItem item = service.getById(itemId);

        if (dto.name()        != null) item.setName(dto.name());
        if (dto.description() != null) item.setDescription(dto.description());
        if (dto.price()       != null) item.setPrice(dto.price());
        if (dto.quantity()    != null) item.setQuantity(dto.quantity());
        if (dto.imgURL()      != null) item.setImgURL(dto.imgURL());
        if (dto.categories()    != null) item.setCategory(dto.categories());

        service.updateMenuItem(seller, item);
        return MenuItemMapper.toDto(item);
    }

    @DELETE
    @Path("/{itemId}")
    @Operation(summary = "Delete a menu item")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized action"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public void delete(
            @HeaderParam("Authorization") String token,
            @PathParam("itemId") long itemId) throws Exception {

        Seller seller = extractSeller(token);
        MenuItem item = service.getById(itemId);
        service.deleteMenuItem(seller, item);
    }

    @GET
    @Path("/restaurant/{restaurantId}")
    @Operation(summary = "List menu items of a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items listed successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public List<MenuItemResponse> list(@PathParam("restaurantId") long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return service.getRestaurantMenu(restaurant)
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private Seller extractSeller(String token) {
        long userId = TokenUtil.decodeUserId(token); // Base64 parse
        return (Seller) sellerService.findById(userId);
    }


    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
