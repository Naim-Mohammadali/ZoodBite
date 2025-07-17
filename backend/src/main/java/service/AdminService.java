package service;

import dao.UserDAO;
import model.Admin;
import model.User;

import java.util.List;
public class AdminService {

    private final UserDAO userDAO;

    public AdminService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Admin changePassword(Admin admin, String newPassword) {
        admin.setPassword(newPassword);
        return (Admin) userDAO.update(admin);
    }


    public List<User> listAllUsers() {
        return userDAO.findAll();
    }

    public void blockUser(User user) {
        user.setStatus(User.Status.BLOCKED);
        userDAO.update(user);
    }

    public void unblockUser(User user) {
        user.setStatus(User.Status.ACTIVE);
        userDAO.update(user);
    }

    public User findUserById(Long id) {
        return userDAO.findById(id);
    }
}
