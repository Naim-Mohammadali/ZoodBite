package controller;

import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.validation.*;
import util.mapper.UserMapper;
import model.Role;
import model.Seller;
import service.SellerService;

import java.util.Set;

public class SellerController {
    private final SellerService service;
    private final Validator     validator;

    public SellerController() {
        this(new SellerService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    /** Test-friendly ctor */
    public SellerController(SellerService service, Validator validator) {
        this.service   = service;
        this.validator = validator;
    }

    /** Register a new seller account */
    public UserProfileResponse registerSeller(UserRegisterRequest dto) {
        validate(dto);
        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.SELLER);

        Seller saved = service.registerSeller(fixed);
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse viewProfile(long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    public UserProfileResponse updatePhone(long id, String phone) {
        Seller s = service.updatePhone(id, phone);
        return UserMapper.toDto(s);
    }

    public UserProfileResponse changePassword(long id, String newPwd) {
        Seller s = service.changePassword(id, newPwd);
        return UserMapper.toDto(s);
    }

    public UserProfileResponse update(long id, UserUpdateRequest patch) {
        validate(patch);

        Seller s = (Seller) service.findById(id);
        if (patch.name()    != null) s.setName(patch.name());
        if (patch.email()   != null) s.setEmail(patch.email());
        if (patch.address() != null) s.setAddress(patch.address());

        Seller saved = (Seller) service.update(s);
        return UserMapper.toDto(saved);
    }
    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
