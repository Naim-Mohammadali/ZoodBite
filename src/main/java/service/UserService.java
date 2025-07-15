package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

public class UserService {

    protected final UserDAO userDAO = new UserDAOImpl();

    public void registerUser(User user) throws Exception {
        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already registered");
        }
        if (userDAO.findByPhone(user.getPhone()) != null) {
            throw new Exception("Phone already registered");
        }
        userDAO.save(user);
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
}
