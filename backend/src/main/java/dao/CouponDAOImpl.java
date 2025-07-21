package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Coupon;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class CouponDAOImpl implements CouponDAO {

    private final EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();

    @Override
    public void save(Coupon c) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(c);
        tx.commit();
    }

    @Override
    public Coupon findByCode(String code) {
        List<Coupon> result = em.createQuery("SELECT c FROM Coupon c WHERE c.code = :code", Coupon.class)
                .setParameter("code", code)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void incrementUsage(Coupon c) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        c.setUsedCount(c.getUsedCount() + 1);
        em.merge(c);
        tx.commit();
    }
}
