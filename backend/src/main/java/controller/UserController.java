package controller;

import dto.user.*;
import model.Role;
import model.User;
import service.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    private final UserService userService = new UserService();

    // Register a new user
    public UserProfileDTO register(RegisterRequestDTO dto) {
        try {
            User user = userService.register(
                    dto.name,
                    dto.phone,
                    dto.password,
                    dto.role,
                    dto.address
            );
            return mapToDTO(user);
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    // Login with phone + password
    public UserProfileDTO login(LoginRequestDTO dto) {
        User user = userService.findByPhone(dto.phone);
        if (user == null) {
            System.out.println("User not found.");
            return null;
        }
        if (!user.getPassword().equals(dto.password)) {
            System.out.println("Incorrect password.");
            return null;
        }
        return mapToDTO(user);
    }

    // View profile
    public UserProfileDTO viewProfile(User user) {
        return mapToDTO(user);
    }

    // Update user info
    public UserProfileDTO update(UpdateUserDTO dto, User user) {
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        userService.update(user);
        return mapToDTO(user);
    }

    // List all users
    public List<UserProfileDTO> listAllUsers() {
        return userService.listAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // List by role
    public List<UserProfileDTO> listByRole(Role role) {
        return userService.listUsersByRole(role).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper to convert User → DTO
    private UserProfileDTO mapToDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getStatus(),
                user.getRole()
        );
    }
}
