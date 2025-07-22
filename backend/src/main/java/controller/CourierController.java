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
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import util.mapper.UserMapper;
import model.Courier;
import model.Role;
import service.CourierService;

import java.util.Set;

@Path("/couriers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourierController {

    private final CourierService service;
    private final Validator validator;

    public CourierController() {
        this(new CourierService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public CourierController(CourierService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @POST
    @Path("/register")
    @Operation(summary = "Register a new courier account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Courier account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public UserProfileResponse registerCourier(@Valid UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.COURIER);

        Courier saved = service.registerCourier(fixed);
        return UserMapper.toDto(saved);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "View courier profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse viewProfile(@PathParam("id") long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    @PATCH
    @Path("/{id}/availability")
    @Operation(summary = "Set courier availability")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse setAvailability(
            @PathParam("id") long id,
            @Valid CourierAvailabilityRequest request) {
        Courier c = service.setAvailability(id, request.available());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/{id}/phone")
    @Operation(summary = "Change courier phone number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse changePhone(
            @PathParam("id") long id,
            @Valid ChangePhoneRequest request) {
        Courier c = service.changePhone(id, request.phone());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/{id}/password")
    @Operation(summary = "Change courier password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse changePassword(
            @PathParam("id") long id,
            @Valid ChangePasswordRequest request) {
        Courier c = service.changePassword(id, request.newPassword());
        return UserMapper.toDto(c);
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Update courier profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    public UserProfileResponse update(
            @PathParam("id") long id,
            @Valid UserUpdateRequest patch) {
        validate(patch);

        Courier c = (Courier) service.findById(id);
        if (patch.name() != null)    c.setName(patch.name());
        if (patch.email() != null)   c.setEmail(patch.email());
        if (patch.address() != null) c.setAddress(patch.address());

        Courier saved = (Courier) service.update(c);
        return UserMapper.toDto(saved);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
