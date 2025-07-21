package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Customer;
import model.Rating;
import model.Restaurant;
import util.EntityManagerFactorySingleton;

import java.util.List;
import java.util.Optional;

public class RatingDAO {

    private final EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance();
    private final EntityManager em = emf.createEntityManager();

    public void save(Rating rating) {
        em.getTransaction().begin();
        em.persist(rating);
        em.getTransaction().commit();
    }

    public Optional<Rating> findById(Long id) {
        return Optional.ofNullable(em.find(Rating.class, id));
    }

    public List<Rating> findByCustomer(Customer customer) {
        String jpql = "SELECT r FROM Rating r WHERE r.customer = :customer";
        TypedQuery<Rating> q = em.createQuery(jpql, Rating.class);
        q.setParameter("customer", customer);
        return q.getResultList();
    }

    public List<Rating> findByRestaurant(Restaurant restaurant) {
        String jpql = "SELECT r FROM Rating r WHERE r.restaurant = :restaurant";
        TypedQuery<Rating> q = em.createQuery(jpql, Rating.class);
        q.setParameter("restaurant", restaurant);
        return q.getResultList();
    }


    public Rating findByCustomerAndRestaurant(Customer customer, Restaurant restaurant) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            String jpql = "SELECT r FROM Rating r WHERE r.customer = :customer AND r.restaurant = :restaurant";
            TypedQuery<Rating> q = em.createQuery(jpql, Rating.class);
            q.setParameter("customer", customer);
            q.setParameter("restaurant", restaurant);
            List<Rating> result = q.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }


    public void delete(Rating rating) {
        em.getTransaction().begin();
        em.remove(em.contains(rating) ? rating : em.merge(rating));
        em.getTransaction().commit();
    }

    public List<Rating> findAll() {
        return em.createQuery("SELECT r FROM Rating r", Rating.class).getResultList();
    }

    public List<Rating> findByRestaurantId(Long restaurantId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("""
            SELECT r FROM Rating r
            WHERE r.restaurant.id = :restaurantId
        """, Rating.class)
                    .setParameter("restaurantId", restaurantId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public void update(Rating rating) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rating);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


}
