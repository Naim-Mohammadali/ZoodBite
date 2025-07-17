package dao;

import model.User;
import java.util.List;

public interface UserDAO {

    void save(User user);
    User update(User user);
    void delete(User user);
    User findById(Long id);
    List<User> findAll();
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByType(Class<? extends User> type);
}
