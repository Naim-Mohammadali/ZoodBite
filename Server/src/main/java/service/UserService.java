package service;
import dto.user.request.UserUpdateRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import dao.UserDAO;
import dao.UserDAOImpl;
import model.*;
import org.jetbrains.annotations.NotNull;
import util.EntityManagerFactorySingleton;

import java.time.Instant;
import java.util.Base64;
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
        if (u instanceof Courier c) {
            System.out.println("Direct find -> bank=" + c.getBankName() + ", account=" + c.getAccountNumber());
        }

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

    public User login(String phone, String rawPassword) {
        User u = userDAO.findByPhone(phone);
        if (u == null)  {
            new IllegalArgumentException("Phone not registered");
        }
        if (!BCrypt.checkpw(rawPassword, u.getPassword()))
            throw new IllegalArgumentException("Bad credentials");
        if (u.getRole() == Role.SELLER) {
            u = userDAO.findById(u.getId()); // will return Seller
        } else if (u.getRole() == Role.COURIER) {
            u = userDAO.findById(u.getId()); // will return Courier
        }
        if (u instanceof Courier c) {
            System.out.println("Courier bank=" + c.getBankName() + ", account=" + c.getAccountNumber());
        }

        return u;
    }
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public User changeRole(User user, Role newRole) {
        user.setRole(newRole);
        return userDAO.update(user);
    }


    public String issueToken(User user) {
        String raw = user.getId() + ":" + user.getRole() + ":" + Instant.now().toEpochMilli();
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }
    public User update(User user) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User merged = em.merge(user);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public User updatePartial(long id, UserUpdateRequest dto) {
        User user = userDAO.findById(id);
        if (user == null) throw new NotFoundException("User not found");

        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }
        if (dto.address() != null && !dto.address().isBlank()) {
            user.setAddress(dto.address());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));
        }
        if (dto.accountNumber() != null && !dto.accountNumber().isBlank()) {
            if (user.getRole() == Role.SELLER) {
                ((Seller) user).setAccountNumber(dto.accountNumber());
            }
            else {
                ((Courier) user).setAccountNumber(dto.accountNumber());
            }
        }
        if (dto.bankName() != null && !dto.bankName().isBlank()) {
            if (user.getRole() == Role.SELLER) {
                ((Seller) user).setBankName(dto.bankName());
            }
            else {
                ((Courier) user).setBankName(dto.bankName());
            }
        }

        return userDAO.update(user);
    }


}
