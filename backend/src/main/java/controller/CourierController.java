package controller;

import dto.admin.ChangePasswordRequest;
import dto.courier.CourierAvailabilityRequest;
import dto.customer.ChangePhoneRequest;
import dto.user.request.UserRegisterRequest;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import model.TokenUtil;
import util.mapper.UserMapper;
import model.Courier;
import model.Role;
import service.CourierService;

import java.util.Set;

@Path("/couriers")
@RolesAllowed("courier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourierController {

    private final CourierService service;
    private final Validator validator;

    public CourierController() {
        System.out.println("🟢 CourierController initialized");
        this.service = new CourierService();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    public CourierController(CourierService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }



    @GET
    @Path("/me")
    @Operation(summary = "View courier profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse viewProfile(@HeaderParam("Authorization") String token) {
        Courier c = extractCourier(token);
        return UserMapper.toDto(service.viewProfile(c.getId()));
    }

    @PATCH
    @Path("/me/availability")
    @Operation(summary = "Set courier availability")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse setAvailability(
            @HeaderParam("Authorization") String token,
            @Valid CourierAvailabilityRequest request) {
        Courier c = extractCourier(token);
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/me/phone")
    @Operation(summary = "Change courier phone number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse changePhone(
            @HeaderParam("Authorization") String token,
            @Valid ChangePhoneRequest request) {
        Courier c = service.changePhone(extractCourier(token).getId(), request.phone());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/me/password")
    @Operation(summary = "Change courier password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse changePassword(
            @HeaderParam("Authorization") String token,
            @Valid ChangePasswordRequest request) {
        Courier c = service.changePassword(extractCourier(token).getId(), request.newPassword());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/me")
    @Operation(summary = "Update courier profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse update(
            @HeaderParam("Authorization") String token,
            @Valid UserUpdateRequest patch) {
        validate(patch);

        Courier c = extractCourier(token);
        if (patch.name() != null)    c.setName(patch.name());
        if (patch.email() != null)   c.setEmail(patch.email());
        if (patch.address() != null) c.setAddress(patch.address());

        Courier saved = (Courier) service.update(c);
        return UserMapper.toDto(saved);
    }

    private Courier extractCourier(String token) {
        long id = TokenUtil.decodeUserId(token);
        return (Courier) service.findById(id);
    }


    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
