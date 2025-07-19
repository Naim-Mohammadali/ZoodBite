package controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class SQLConnectionTest {
    public static void main(String[] args) {
        System.out.println("⏳ Connecting to MySQL...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("foodappPU");
        EntityManager em = emf.createEntityManager();
        System.out.println("✅ Connected to MySQL via Hibernate!");
        em.close();
        emf.close();
    }
}
