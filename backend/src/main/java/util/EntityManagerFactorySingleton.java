package util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactorySingleton {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManagerFactorySingleton() {
    }

    public static EntityManagerFactory getInstance() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("foodappPU");
        }
        return entityManagerFactory;
    }

    public static void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
