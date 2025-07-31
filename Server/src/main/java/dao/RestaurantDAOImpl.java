package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Menu;
import model.Restaurant;
import model.Seller;
import util.EntityManagerFactorySingleton;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class RestaurantDAOImpl implements RestaurantDAO {
    private final EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance();

    /* ---------- CRUD ---------- */

    @Override
    public void save(Restaurant restaurant) {
        doInTx((Consumer<EntityManager>) em -> em.persist(restaurant));
    }

      @Override
    public Restaurant findById(Long id) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("""
        SELECT r FROM Restaurant r
        LEFT JOIN FETCH r.menus
        WHERE r.id = :id
        """, Restaurant.class)
                .setParameter("id", id)
                .getSingleResult();
    }


    @Override
    public void update(Restaurant restaurant) {
        doInTx((Consumer<EntityManager>) em -> em.merge(restaurant));
    }

    @Override
    public void delete(Restaurant restaurant) {
        doInTx(em -> {
            Restaurant managed = em.find(Restaurant.class, restaurant.getId());
            if (managed != null) em.remove(managed);
        });
    }

    @Override
    public List<Restaurant> findBySeller(Seller seller) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT r FROM Restaurant r " +
                                    "LEFT JOIN FETCH r.menus " +
                                    "WHERE r.seller = :seller",
                            Restaurant.class)
                    .setParameter("seller", seller)
                    .getResultList();
        }
    }


    @Override
    public List<Restaurant> findByStatus(Restaurant.Status status) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT r FROM Restaurant r " +
                                    "WHERE r.status = :status", Restaurant.class)
                    .setParameter("status", status)
                    .getResultList();
        }
    }



    /* --- NEW helpers you’ll need for controllers --- */

    /**
     * List only ACTIVE restaurants (buyer-facing browse endpoint).
     */
    public List<Restaurant> findActive() {
        return findByStatus(Restaurant.Status.ACTIVE);
    }

    /**
     * Authorization helper: ensure the seller really owns the restaurant.
     */
    public Restaurant findByIdAndSeller(Long restaurantId, Long sellerId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT r FROM Restaurant r " +
                                    "WHERE r.id = :rid AND r.seller.id = :sid",
                            Restaurant.class)
                    .setParameter("rid", restaurantId)
                    .setParameter("sid", sellerId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    /* ---------- internal helpers ---------- */

    private void doInTx(Consumer<EntityManager> work) {
        doInTx(em -> { work.accept(em); return null; });
    }

    private <T> T doInTx(Function<EntityManager,T> work) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Restaurant> search(String kw, String cat,
                                   Double min, Double max) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = """
            SELECT DISTINCT r FROM Restaurant r
            JOIN r.menuItems i
            WHERE r.status = :active
              AND (:kw  IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%',:kw,'%'))
                   OR LOWER(i.name) LIKE LOWER(CONCAT('%',:kw,'%')))
              AND (:cat IS NULL OR LOWER(i.category) = LOWER(:cat))
              AND (:min IS NULL OR i.price >= :min)
              AND (:max IS NULL OR i.price <= :max)
        """;
            return em.createQuery(jpql, Restaurant.class)
                    .setParameter("active", Restaurant.Status.ACTIVE)
                    .setParameter("kw",  kw)
                    .setParameter("cat", cat)
                    .setParameter("min", min)
                    .setParameter("max", max)
                    .getResultList();
        }
    }@Override
    public List<Restaurant> findByNameContaining(String keyword) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<Restaurant> result;

        try {
            em.getTransaction().begin();

            result = em.createQuery(
                            "SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE :kw", Restaurant.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .getResultList();

            // Manually initialize menus and items to avoid LazyInitializationException
            for (Restaurant r : result) {
                r.getMenus().size(); // trigger menu list
                for (Menu menu : r.getMenus()) {
                    menu.getItems().size(); // trigger items
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }

        return result;
    }






}
