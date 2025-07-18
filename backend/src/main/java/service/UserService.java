package service;
import model.*;
import dao.UserDAO;
import dao.UserDAOImpl;

import java.util.List;

public class UserService {

    protected final UserDAO userDAO = new UserDAOImpl();

    public User register(String name, String phone, String password,
                         Role role, String address) {

        if (userDAO.findByPhone(phone) != null)
            throw new IllegalArgumentException("Phone already exists");

        User user;
        switch (role) {
            case CUSTOMER -> user = new Customer(name, phone, password, address);
            case SELLER   -> user = new Seller(name, phone, password, address);
            case COURIER  -> user = new Courier(name, phone, password, address);
            case ADMIN    -> user = new Admin(name, phone, password);
            default -> throw new IllegalStateException("Unexpected role: " + role);
        }

        userDAO.save(user);
        return user;
    }
    public void update(User user) {
        userDAO.update(user);
    }
    public List<User> listUsersByRole(Role role) {
        return switch (role) {
            case CUSTOMER -> userDAO.findByType(Customer.class);
            case SELLER   -> userDAO.findByType(Seller.class);
            case COURIER  -> userDAO.findByType(Courier.class);
            case ADMIN    -> userDAO.findByType(Admin.class);
        };
    }
    public List<User> listAll() {
        return userDAO.findAll();
    }



    public User getUser(Long id) {
        return userDAO.findById(id);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public User findByPhone(String number) {
        return userDAO.findByPhone(number);
    }

    public User getByEmail(String mail) {
        return userDAO.findByEmail(mail);
    }
}
