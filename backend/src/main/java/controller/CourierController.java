package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.*;
import util.mapper.UserMapper;
import model.Courier;
import model.Role;
import service.CourierService;

import java.util.Set;

public class CourierController {

    private final CourierService service;
    private final Validator      validator;

    public CourierController() {
        this(new CourierService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public CourierController(CourierService service, Validator validator) {
        this.service   = service;
        this.validator = validator;
    }


    public UserProfileResponse registerCourier(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.COURIER);

        Courier saved = service.registerCourier(fixed);
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse viewProfile(long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    public UserProfileResponse setAvailability(long id, boolean available) {
        Courier c = service.setAvailability(id, available);
        return UserMapper.toDto(c);
    }

    public UserProfileResponse changePhone(long id, String phone) {
        Courier c = service.changePhone(id, phone);
        return UserMapper.toDto(c);
    }

    public UserProfileResponse changePassword(long id, String newPwd) {
        Courier c = service.changePassword(id, newPwd);
        return UserMapper.toDto(c);
    }

    public UserProfileResponse update(long id, UserUpdateRequest patch) {
        validate(patch);

        Courier c = (Courier) service.findById(id);
        if (patch.name()    != null) c.setName(patch.name());
        if (patch.email()   != null) c.setEmail(patch.email());
        if (patch.address() != null) c.setAddress(patch.address());

        Courier saved = (Courier) service.update(c);
        return UserMapper.toDto(saved);
    }

    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
