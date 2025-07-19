package controller;

import model.*;
import org.jetbrains.annotations.NotNull;
import service.*;

import java.util.List;

public class UserController {

    private final UserService userService = new UserService();

    public User register(String name, String phone, String password, Role role, String address) {
        try {
            User user = userService.register(name, phone, password, role, address);
            System.out.println("Registered " + role + ": " + user.getName());
            return user;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    public User login(String phone, String password) {
        User user = userService.findByPhone(phone);
        if (user == null) {
            System.out.println("User not found.");
            return null;
        }
        if (!user.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            return null;
        }
        System.out.println("Welcome back, " + user.getName() + "!");
        return user;
    }

    public void viewProfile(@NotNull User user) {
        System.out.println("Profile of " + user.getName() + ":");
        System.out.println(user);
    }

    public void updateBasicInfo(@NotNull User user, String newName, String newAddress) {
        user.setName(newName);
        user.setAddress(newAddress);
        userService.update(user);
        System.out.println("Info updated for " + user.getName());
    }

    public void listAllUsers() {
        List<User> users = userService.listAll();
        System.out.println("All users:");
        users.forEach(System.out::println);
    }

    public void listByRole(Role role) {
        List<User> users = userService.listUsersByRole(role);
        System.out.println("Users with role " + role + ":");
        users.forEach(System.out::println);
    }
    public void deleteUser(Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.deleteUser(user);
            System.out.println("Deleted user: " + user.getName());
        } else {
            System.out.println("No user found with ID: " + id);
        }
    }

    public User findUserById(Long id) {
        return userService.findById(id);
    }

    public void changePassword(@NotNull User user, String newPassword) {
        user.setPassword(newPassword);
        userService.update(user);
        System.out.println("Password updated.");
    }

}
