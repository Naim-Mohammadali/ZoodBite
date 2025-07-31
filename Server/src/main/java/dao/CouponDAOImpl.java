package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Coupon;
import util.EntityManagerFactorySingleton;

import java.time.LocalDate;
import java.util.List;

public class CouponDAOImpl implements CouponDAO {

    @Override
    public void save(Coupon c) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(c);
            tx.commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Coupon findByCode(String code) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            List<Coupon> result = em.createQuery(
                            "SELECT c FROM Coupon c WHERE c.code = :code", Coupon.class)
                    .setParameter("code", code)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void incrementUsage(Coupon c) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            c.setUsedCount(c.getUsedCount() + 1);
            em.merge(c);
            tx.commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Coupon> findAll() {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Coupon c", Coupon.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Coupon> findValid() {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery("""
                        SELECT c FROM Coupon c
                        WHERE c.validFrom <= :now AND c.validUntil >= :now
                          AND c.usedCount < c.usageLimit
                    """, Coupon.class)
                    .setParameter("now", LocalDate.now())
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
