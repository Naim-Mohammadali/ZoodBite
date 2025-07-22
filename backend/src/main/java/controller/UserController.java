package controller;

import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;

import util.mapper.UserMapper;
import model.Role;
import model.User;
import service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;
    private final Validator validator;

    public UserController() {
        this(new UserService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @POST
    @Path("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public UserProfileResponse register(@Valid UserRegisterRequest dto) {
        validate(dto);
        User saved = userService.register(UserMapper.toEntity(dto));
        return UserMapper.toDto(saved);
    }

    @POST
    @Path("/login")
    @Operation(summary = "Login a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public UserProfileResponse login(@Valid UserLoginRequest dto) {
        validate(dto);
        User logged = userService.login(dto.phone(), dto.password());
        return UserMapper.toDto(logged);
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Update user information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public UserProfileResponse update(@PathParam("id") long id,
                                      @Valid UserUpdateRequest dto) {
        validate(dto);

        User user = userService.findById(id);

        if (dto.name() != null) user.setName(dto.name());
        if (dto.address() != null) user.setAddress(dto.address());
        if (dto.email() != null) user.setEmail(dto.email());

        User saved = userService.update(user);
        return UserMapper.toDto(saved);
    }

    @GET
    @Operation(summary = "List all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    public List<UserProfileResponse> listAll() {
        return userService.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/role/{role}")
    @Operation(summary = "List users by role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully by role"),
            @ApiResponse(responseCode = "400", description = "Invalid role specified")
    })
    public List<UserProfileResponse> listByRole(@PathParam("role") Role role) {
        return userService.findByRole(role)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "View user profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserProfileResponse view(@PathParam("id") long id) {
        return UserMapper.toDto(userService.findById(id));
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
