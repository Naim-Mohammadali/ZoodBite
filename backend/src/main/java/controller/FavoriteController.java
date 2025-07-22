package controller;

import dto.favorite.FavoriteActionDto;
import dto.restaurant.RestaurantBriefDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.*;
import service.FavoriteService;
import service.RestaurantService;
import util.mapper.RestaurantMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/favorites")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FavoriteController {

    private final FavoriteService   favService;
    private final RestaurantService restService;
    private final Validator         validator;

    public FavoriteController() {
        this(new FavoriteService(), new RestaurantService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }
    public FavoriteController(FavoriteService favService,
                              RestaurantService restService,
                              Validator validator) {
        this.favService  = favService;
        this.restService = restService;
        this.validator   = validator;
    }

    @POST
    @Operation(summary = "Add a restaurant to customer's favorites")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant added to favorites"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void add(Customer customer, FavoriteActionDto dto) throws Exception {
        validate(dto);
        Restaurant r = restService.findById(dto.restaurantId());
        favService.add(customer, r);
    }

    @DELETE
    @Path("/{restaurantId}")
    @Operation(summary = "Remove a restaurant from customer's favorites")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant removed from favorites"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void remove(Customer customer, @PathParam("restaurantId") long restaurantId) throws Exception
    {
        Restaurant r = restService.findById(restaurantId);
        favService.remove(customer, r);
    }
    @GET
    @Operation(summary = "List customer's favorite restaurants")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorite restaurants retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<RestaurantBriefDto> list(Customer customer) {
        return favService.list(customer).stream()
                .map(RestaurantMapper::toBriefDto)
                .collect(Collectors.toList());
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> v = validator.validate(dto);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
