package util;

import model.Admin;
import model.Role;
import service.AdminService;
import service.UserService;

import java.util.List;

public final class DatabaseSeeder {

    private DatabaseSeeder() {}

    public static void seedAdminIfMissing() {
        UserService userService = new UserService();
        List<model.User> admins = userService.findByRole(Role.ADMIN);
        if (admins == null || admins.isEmpty()) {
            System.out.println("⚙ Seeding default admin user...");
            AdminService adminService = new AdminService();
            Admin admin = new Admin("Administrator", "admin", "admin");
            adminService.register(admin);
            System.out.println("✅ Default admin seeded (phone=admin / password=admin)");
        } else {
            System.out.println("ℹ Admin user already present (" + admins.size() + ")");
        }
    }
}


