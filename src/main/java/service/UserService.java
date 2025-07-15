package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    public void registerUser(User user) {
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
