package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.FoodOrder;
import model.User;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class OrderDAOImpl implements OrderDAO{
    public void save(FoodOrder order) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(order);
        tx.commit();
        em.close();
    }
    @Override
    public void update(FoodOrder order)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(order);
        tx.commit();
        em.close();
    };
    @Override
    public FoodOrder findById(long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        FoodOrder order = em.find(FoodOrder.class, id);
        em.close();
        return order;
    }
    @Override
    public List<FoodOrder> findByCustomer (User customer) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<FoodOrder> orders = em.createQuery("SELECT o FROM FoodOrder o WHERE o.customer = :customer", FoodOrder.class)
                .setParameter("customer", customer)
                .getResultList();
        em.close();
        return orders;
    }
    @Override
    public List<FoodOrder> findByCourier (User courier) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<FoodOrder> orders = em.createQuery("SELECT o FROM FoodOrder o WHERE o.courier = :courier", FoodOrder.class)
                .setParameter("courier", courier)
                .getResultList();
        em.close();
        return orders;
    }
    @Override
    public List<FoodOrder> findByStatus(FoodOrder.Status status) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<FoodOrder> orders = em.createQuery("SELECT o FROM FoodOrder o WHERE o.status = :status", FoodOrder.class)
                .setParameter("status", status)
                .getResultList();
        em.close();
        return orders;
    }

}
