package test;

import model.Customer;
import model.Role;
import service.CustomerService;
import service.UserService;
import util.EntityManagerFactorySingleton;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import static model.Role.CUSTOMER;

public class IntegrationTest {

    public static void main(String[] args) {
        System.out.println("🚀 Starting CustomerService integration test...");

        // Setup EntityManager
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        UserService userService = new CustomerService();

        try {
            tx.begin();

            // 1. Register new customer
            Customer customer = (Customer) userService.register("John Doe", "09123456789",  "secret123", CUSTOMER, "Beirut");
            System.out.println("✅ Registered customer: " + customer);

            // 2. Fetch by phone
            Customer fetchedByPhone = (Customer) userService.findByPhone("09123456789");
            System.out.println("🔍 Fetched by phone: " + fetchedByPhone);

            // 3. Fetch by email
            Customer fetchedByEmail = (Customer) userService.getByEmail("john@example.com");
            System.out.println("🔍 Fetched by email: " + fetchedByEmail);

            // 4. Update customer info
            fetchedByPhone.setName("John Smith");
            userService.update(fetchedByPhone);
            System.out.println("✏️ Updated name to: " + fetchedByPhone.getName());

            // 5. List all users
            System.out.println("📋 All users:");
            userService.listAll().forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            EntityManagerFactorySingleton.getInstance().close();
        }

        System.out.println("🏁 Test finished.");
    }
}
