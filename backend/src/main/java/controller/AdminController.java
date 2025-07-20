package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.*;
import util.mapper.UserMapper;
import model.*;
import service.AdminService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class AdminController {

    private final AdminService service;
    private final Validator    validator;

    public AdminController() {
        this(new AdminService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public AdminController(AdminService service, Validator validator) {
        this.service   = service;
        this.validator = validator;
    }

    public UserProfileResponse registerAdmin(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.ADMIN);

        Admin saved = service.registerAdmin(fixed);
        return UserMapper.toDto(saved);
    }

    public List<UserProfileResponse> listAllUsers() {
        return service.listAllUsers()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserProfileResponse update(long id, UserUpdateRequest patch) {
        validate(patch);

        Admin admin = (Admin) service.findById(id);  // inherited helper
        if (patch.name()  != null) admin.setName(patch.name());
        if (patch.email() != null) admin.setEmail(patch.email());

        Admin saved = (Admin) service.update(admin); // inherited helper
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse blockUser(long userId) {
        User updated = service.blockUser(userId);
        return UserMapper.toDto(updated);
    }

    public UserProfileResponse unblockUser(long userId) {
        User updated = service.unblockUser(userId);
        return UserMapper.toDto(updated);
    }

    public UserProfileResponse changePassword(long id, String newPwd) {
        Admin a = service.changePassword(id, newPwd);
        return UserMapper.toDto(a);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
