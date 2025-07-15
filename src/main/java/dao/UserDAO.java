package dao;

import model.User;

public interface UserDAO {
    void save(User user);
    User findById(Long id);
    void update(User user);
    void delete(User user);
}
