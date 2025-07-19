package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Customer;
import service.UserService;
import util.EntityManagerFactorySingleton;

public class main {

    public static void main(String[] args) {
        System.out.println("🚀 Starting ZoodBite Backend...");

        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        UserService userService = new UserService();

        try {
            tx.begin();

            // Simple test — fetch or create a sample customer
            String phone = "09123456789";
            Customer existing = (Customer) userService.findByPhone(phone);
            if (existing == null) {
                Customer customer = (Customer) userService.register("Main Test", phone, "test123", model.Role.CUSTOMER, "Beirut");
                System.out.println("✅ Sample user created: " + customer);
            } else {
                System.out.println("👤 Sample user already exists: " + existing);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            EntityManagerFactorySingleton.getInstance().close();
        }

        System.out.println("✅ Server initialized successfully.");
    }
}
