package service;

import dao.RatingDAO;
import dto.rating.RatingRequestDto;
import dto.restaurant.RestaurantResponseDto;
import dto.user.request.UserRegisterRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import model.Restaurant;
import util.mapper.UserMapper;
import model.Customer;
import model.Role;

import java.util.ArrayList;
import java.util.List;


public class CustomerService extends UserService {
    private final RatingDAO ratingDAO = new RatingDAO();

    public CustomerService() {
        super();                 // pulls in the shared UserDAO
    }

    public Customer registerCustomer(UserRegisterRequest dto) {
        if (dto == null) throw new IllegalArgumentException("DTO must not be null");

        // Enforce correct role (records are immutable, so create a new one)
        if (dto.role() != Role.CUSTOMER) {
            dto = new UserRegisterRequest(
                    dto.name(), dto.phone(), dto.email(),
                    dto.password(), dto.address(), Role.CUSTOMER);
        }

        Customer customer = (Customer) UserMapper.toEntity(dto);
        return (Customer) register(customer);   // inherited from UserService
    }


    public Customer viewProfile(long id) {
        return (Customer) findById(id);          // inherited helper
    }


    public Customer updateAddress(long id, String newAddress) {
        Customer c = (Customer) findById(id);
        c.setAddress(newAddress);
        return (Customer) update(c);             // persists & returns
    }

    public Customer changePhone(long id, String newPhone) {
        if (findByPhone(newPhone) != null)
            throw new IllegalArgumentException("Phone already in use!");

        Customer c = (Customer) findById(id);
        c.setPhone(newPhone);
        return (Customer) update(c);
    }

    public Customer changePassword(long id, String newPassword) {
        Customer c = (Customer) findById(id);
        c.setPassword(newPassword);
        return (Customer) update(c);
    }
    @POST
    @Path("/favorites")
    @RolesAllowed("customer")
    public void addFavorite(Customer customer, Restaurant restaurant) {
        customer.getFavorites().add(restaurant);
        userDAO.update(customer);
    }
    @DELETE
    @Path("/favorites")
    @RolesAllowed("customer")
    public List<Restaurant> getFavorites(Customer customer) {
        return new ArrayList<>(customer.getFavorites());
    }



}
