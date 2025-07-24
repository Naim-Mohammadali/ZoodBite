package dao;

import jakarta.persistence.*;
import model.*;
import org.jetbrains.annotations.NotNull;
import util.EntityManagerFactorySingleton;
import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {

    private EntityManager newEm() {
        return EntityManagerFactorySingleton.getInstance().createEntityManager();
    }

    @Override
    public void save(Favorite fav) {
        EntityManager em = newEm();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(fav);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public void delete(@NotNull Favorite fav) {
        EntityManager em = newEm();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Favorite m = em.find(Favorite.class, fav.getId());
            if (m != null) em.remove(m);
            tx.commit();
        } finally { em.close(); }
    }

    @Override
    public boolean exists(Customer c, Restaurant r) {
        EntityManager em = newEm();
        try {
            Long cnt = em.createQuery(
                            "SELECT COUNT(f) FROM Favorite f " +
                                    "WHERE f.customer = :c AND f.restaurant = :r", Long.class)
                    .setParameter("c", c)
                    .setParameter("r", r)
                    .getSingleResult();
            return cnt > 0;
        } finally { em.close(); }
    }

    @Override
    public List<Favorite> findByCustomer(Customer c) {
        EntityManager em = newEm();
        try {
            return em.createQuery(
                            "SELECT f FROM Favorite f WHERE f.customer = :c", Favorite.class)
                    .setParameter("c", c)
                    .getResultList();
        } finally { em.close(); }
    }
}
