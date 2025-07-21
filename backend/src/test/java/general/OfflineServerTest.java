package general;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;
import service.*;

public class OfflineServerTest {
    public static void main(String[] args) {

        /* ---------- bootstrap JPA ---------- */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("foodappPU");
        EntityManager em  = emf.createEntityManager();

        /* ---------- quick transaction ---------- */
        em.getTransaction().begin();
        Customer ali = new Customer("Ali", "090000000", "abc123", "Beirut");
        em.persist(ali);
        em.getTransaction().commit();

        /* ---------- verify read ---------- */
        Customer found = em.find(Customer.class, ali.getId());
        System.out.println("Found: " + found);

        /* ---------- service layer sanity ---------- */
        UserService us  = new UserService();      // assumes userDAO injected inside
        CustomerService cs  = new CustomerService();  // inherits DAO
        System.out.println("DAO call, total users = " + us.findAll().size());

        em.close();
        emf.close();
    }
}
