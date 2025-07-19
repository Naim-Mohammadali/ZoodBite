package controller;

import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import jakarta.validation.*;
import util.mapper.UserMapper;
import model.Role;
import model.User;
import service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UserController {


    private final UserService userService;
    private final Validator   validator;

    public UserController() {
        this(new UserService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator   = validator;
    }


    public UserProfileResponse register(UserRegisterRequest dto) {
        validate(dto);
        User saved = userService.register(UserMapper.toEntity(dto));
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse login(UserLoginRequest dto) {
        validate(dto);
        User logged = userService.login(dto.phone(), dto.password());
        return UserMapper.toDto(logged);
    }


    public UserProfileResponse update(long id, UserUpdateRequest dto) {
        validate(dto);

        User user = userService.findById(id);

        if (dto.name()    != null) user.setName(dto.name());
        if (dto.address() != null) user.setAddress(dto.address());
        if (dto.email()   != null) user.setEmail(dto.email());

        User saved = userService.update(user);
        return UserMapper.toDto(saved);
    }


    public List<UserProfileResponse> listAll() {
        return userService.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserProfileResponse> listByRole(Role role) {
        return userService.findByRole(role)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserProfileResponse view(long id) {
        return UserMapper.toDto(userService.findById(id));
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
