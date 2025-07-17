package dao;

import model.User;

import java.util.List;

public interface UserDAO {
    void save(User user);
    User findById(Long id);
    void update(User user);
    void delete(User user);
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findAll();
    List<User> findByStatus(User.Status status);
    List<User> findByRole(User.Role role);
}
