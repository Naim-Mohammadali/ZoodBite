package controller;

import dto.admin.ChangePasswordRequest;
import dto.customer.ChangePhoneRequest;
import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import util.mapper.UserMapper;
import model.Role;
import model.Seller;
import service.SellerService;

import java.util.Set;

@Path("/sellers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SellerController {
    private final SellerService service;
    private final Validator validator;

    public SellerController() {
        this(new SellerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public SellerController(SellerService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "View seller profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    public UserProfileResponse viewProfile(@PathParam("id") long id) {
        return UserMapper.toProfileDto(service.viewProfile(id));
    }

    @PATCH
    @Path("/{id}/phone")
    @Operation(summary = "Update seller phone number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    public UserProfileResponse updatePhone(@PathParam("id") long id,
                                           @Valid ChangePhoneRequest request) {
        Seller s = service.updatePhone(id, request.phone());
        return UserMapper.toProfileDto(s);
    }

    @PATCH
    @Path("/{id}/password")
    @Operation(summary = "Change seller password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    public UserProfileResponse changePassword(@PathParam("id") long id,
                                              @Valid ChangePasswordRequest request) {
        Seller s = service.changePassword(id, request.newPassword());
        return UserMapper.toProfileDto(s);
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Update seller profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seller updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Seller not found")
    })
    public UserProfileResponse update(@PathParam("id") long id,
                                      @Valid UserUpdateRequest patch) {
        validate(patch);

        Seller s = (Seller) service.findById(id);
        if (patch.name()    != null) s.setName(patch.name());
        if (patch.email()   != null) s.setEmail(patch.email());
        if (patch.address() != null) s.setAddress(patch.address());

        Seller saved = (Seller) service.update(s);
        return UserMapper.toProfileDto(saved);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
