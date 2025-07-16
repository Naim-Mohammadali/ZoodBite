package service;

import model.User;
import java.util.List;

public class AdminService extends UserService {

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void approveUser(User user) throws Exception {
        if (user.getStatus() != User.Status.PENDING) {
            throw new Exception("Cannot approve: User is not pending approval.");
        }
        user.setStatus(User.Status.ACTIVE);
        userDAO.update(user);
    }

    public void blockUser(User user) {
        user.setStatus(User.Status.BLOCKED);
        userDAO.update(user);
    }

    public void unblockUser(User user) {
        user.setStatus(User.Status.PENDING);
        userDAO.update(user);
    }
}
