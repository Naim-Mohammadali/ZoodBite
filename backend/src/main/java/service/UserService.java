package service;
import org.mindrot.jbcrypt.BCrypt;
import dao.UserDAO;
import dao.UserDAOImpl;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class UserService {

    protected final UserDAO userDAO = new UserDAOImpl();


    public User register(User user) {
        if (user == null) throw new IllegalArgumentException("User must not be null");
        if (userDAO.findByPhone(user.getPhone()) != null)
            throw new IllegalArgumentException("Phone already exists");
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        userDAO.save(user);
        return user;
    }

    @Deprecated
    public User register(String name, String phone, String password,
                         @NotNull Role role, String address) {

        User u = switch (role) {
            case CUSTOMER -> new Customer(name, phone, password, address);
            case SELLER   -> new Seller  (name, phone, password, address);
            case COURIER  -> new Courier (name, phone, password, address);
            case ADMIN    -> new Admin   (name, phone, password);
        };
        return register(u);
    }


    public User findById(Long id) {
        User u = userDAO.findById(id);
        if (u == null) throw new IllegalArgumentException("No user found with ID " + id);
        return u;
    }

    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public List<User> findByRole(@NotNull Role role) {
        Class<? extends User> type = switch (role) {
            case CUSTOMER -> Customer.class;
            case SELLER   -> Seller.class;
            case COURIER  -> Courier.class;
            case ADMIN    -> Admin.class;
        };
        return userDAO.findByType(type);
    }


    public User update(User user) {
        return userDAO.update(user);
    }

    public User login(String phone, String rawPassword) {
        User u = userDAO.findByPhone(phone);
        if (u == null)  {
            new IllegalArgumentException("Phone not registered");
        }
        if (!BCrypt.checkpw(rawPassword, u.getPassword()))
            throw new IllegalArgumentException("Bad credentials");

        return u;
    }
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public User changeRole(User user, Role newRole) {
        user.setRole(newRole);
        return userDAO.update(user);
    }

}
