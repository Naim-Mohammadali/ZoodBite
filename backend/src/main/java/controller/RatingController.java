package controller;

import dao.MenuItemDAO;
import dto.rating.RatingRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import model.Customer;
import model.TokenUtil;
import service.RatingService;
import service.UserService;

@Path("/ratings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;

    public RatingController() {
        this.ratingService = new RatingService();
        this.userService = new UserService();
    }


    @POST
    @RolesAllowed("customer")
    @Operation(summary = "Rate an order")
    public void rate(@Valid RatingRequestDto dto,
                     @HeaderParam("Authorization") String token) {

        long userId = TokenUtil.decodeUserId(token);
        Customer customer = (Customer) userService.findById(userId);
        ratingService.addOrUpdate(customer, dto.orderId(), dto.score(), dto.comment());
    }

    @GET
    @Path("/{orderId}")
    @Operation(summary = "Get average rating for an order")
    public double getAverageRating(@PathParam("orderId") long id) {
        return ratingService.getAverageRating(id);
    }
}
