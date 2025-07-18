package service;

import model.User;
import model.Admin;

import java.util.List;

public class AdminService extends UserService {

    public Admin changePassword(Long adminId, String newPassword) {
        Admin admin = (Admin) userDAO.findById(adminId);
        if (admin == null) throw new IllegalArgumentException("Admin not found");

        admin.setPassword(newPassword);
        userDAO.update(admin);
        return admin;
    }

    public List<User> listAllUsers() {
        return userDAO.findAll();
    }

    public void blockUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        user.setStatus(User.Status.BLOCKED);
        userDAO.update(user);
    }

    public void unblockUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        user.setStatus(User.Status.ACTIVE);
        userDAO.update(user);
    }

    public User findUserById(Long id) {
        return userDAO.findById(id);
    }
}
