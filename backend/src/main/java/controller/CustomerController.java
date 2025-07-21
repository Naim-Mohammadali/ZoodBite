package controller;

import dto.order.CustomerOrderRequest;
import dto.order.OrderResponse;
import dto.user.request.UserRegisterRequest;
import dto.user.request.UserUpdateRequest;
import dto.user.response.UserProfileResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import model.FoodOrder;
import service.OrderService;
import service.UserService;
import util.mapper.OrderMapper;
import util.mapper.UserMapper;
import model.Customer;
import model.Role;
import service.CustomerService;

import java.util.Set;

public class CustomerController {

    private final CustomerService service;
    private final UserService     userService;
    private final OrderService    orderService;
    private final Validator       validator;

    public CustomerController() {
        this(new CustomerService(),
                new UserService(),
                new OrderService(),
                Validation.buildDefaultValidatorFactory().getValidator());
    }

    public CustomerController(CustomerService service,
                              UserService userService,
                              OrderService orderService,
                              Validator validator) {
        this.service      = service;
        this.userService  = userService;
        this.orderService = orderService;
        this.validator    = validator;
    }


    public UserProfileResponse register(UserRegisterRequest dto) {
        validate(dto);

        UserRegisterRequest fixed = new UserRegisterRequest(
                dto.name(), dto.phone(), dto.email(),
                dto.password(), dto.address(), Role.CUSTOMER);

        Customer saved = service.registerCustomer(fixed);
        return UserMapper.toDto(saved);
    }

    public UserProfileResponse view(long id) {
        return UserMapper.toDto(service.viewProfile(id));
    }

    public UserProfileResponse update(long id, UserUpdateRequest patch) {
        validate(patch);
        Customer updated = service.updateAddress(id, patch.address() == null ? "" : patch.address());
        return UserMapper.toDto(updated);
    }

    public UserProfileResponse changePhone(long id, String phone) {
        Customer c = service.changePhone(id, phone);
        return UserMapper.toDto(c);
    }

    public UserProfileResponse changePassword(long id, String newPwd) {
        Customer c = service.changePassword(id, newPwd);
        return UserMapper.toDto(c);
    }

    @POST
    @Path("/orders")
    @RolesAllowed("customer")
    public OrderResponse placeOrder(CustomerOrderRequest dto, @QueryParam("userId") long userId) throws Exception {
        Customer customer = (Customer) userService.findById(userId);
        FoodOrder saved = orderService.placeOrder(customer, dto);
        return OrderMapper.toDto(saved);
    }


    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> v = validator.validate(obj);
        if (!v.isEmpty()) throw new ConstraintViolationException(v);
    }
}
