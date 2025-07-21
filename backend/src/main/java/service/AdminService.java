package service;

import dto.user.request.UserRegisterRequest;
import util.mapper.UserMapper;
import model.Admin;
import model.Role;
import model.User;
import model.User.Status;

import java.util.List;

public class AdminService extends UserService {


    public AdminService() {
        super();
    }

    public Admin registerAdmin(UserRegisterRequest dto) {
        if (dto == null) throw new IllegalArgumentException("DTO must not be null");

        // Enforce correct role (records are immutable ➜ build a new one if needed)
        if (dto.role() != Role.ADMIN) {
            dto = new UserRegisterRequest(
                    dto.name(), dto.phone(), dto.email(),
                    dto.password(), dto.address(), Role.ADMIN);
        }

        Admin admin = (Admin) UserMapper.toEntity(dto);
        return (Admin) register(admin);              // inherited helper
    }


    public Admin changePassword(long adminId, String newPassword) {
        Admin admin = (Admin) findById(adminId);     // inherited helper
        admin.setPassword(newPassword);
        return (Admin) update(admin);                // persists & returns
    }


    public List<User> listAllUsers() {
        return findAll();                            // inherited helper
    }

    public User blockUser(long userId) {
        User u = findById(userId);
        u.setStatus(Status.BLOCKED);
        return update(u);
    }

    public User unblockUser(long userId) {
        User u = findById(userId);
        u.setStatus(Status.ACTIVE);
        return update(u);
    }


    public User findUserById(long id) {
        return findById(id);                         // thin wrapper
    }

    public User changeRole(long userId, Role newRole) {
        User u = findById(userId);
        return changeRole(u, newRole);
    }
}
