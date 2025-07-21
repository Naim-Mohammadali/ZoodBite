package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.FoodOrder;
import model.Restaurant;
import model.User;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public void save(FoodOrder order) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(FoodOrder order) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public FoodOrder findById(long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.find(FoodOrder.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<FoodOrder> findByCustomer(User customer) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM FoodOrder o WHERE o.customer = :customer",
                            FoodOrder.class
                    ).setParameter("customer", customer)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<FoodOrder> findByCourier(User courier) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM FoodOrder o WHERE o.courier = :courier",
                            FoodOrder.class
                    ).setParameter("courier", courier)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<FoodOrder> findByStatus(FoodOrder.Status status) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM FoodOrder o WHERE o.status = :status",
                            FoodOrder.class
                    ).setParameter("status", status)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<FoodOrder> findByRestaurant(Restaurant r) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM FoodOrder o WHERE o.restaurant = :r",
                            FoodOrder.class)
                    .setParameter("r", r)
                    .getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<FoodOrder> findByRestaurantAndStatus(Restaurant r,
                                                     FoodOrder.Status s) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM FoodOrder o WHERE o.restaurant = :r AND o.status = :s",
                            FoodOrder.class)
                    .setParameter("r", r)
                    .setParameter("s", s)
                    .getResultList();
        } finally { em.close(); }
    }

}
