package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.MenuItem;
import model.Restaurant;
import org.jetbrains.annotations.NotNull;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {

    @Override
    public void save(MenuItem menuItem) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(menuItem);
            tx.commit();
        } catch (Exception e) { if (tx.isActive()) tx.rollback(); throw e; }
        finally {
            em.close();
        }
    }

    @Override
    public void update(MenuItem menuItem) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(menuItem);
            tx.commit();
        } catch (Exception e) { if (tx.isActive()) tx.rollback(); throw e; }
        finally {
            em.close();
        }
    }

    @Override
    public void delete(@NotNull MenuItem menuItem) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MenuItem managed = em.find(MenuItem.class, menuItem.getId());
            if (managed != null) {
                em.remove(managed);
            }
            tx.commit();
        } catch (Exception e) { if (tx.isActive()) tx.rollback(); throw e; }
        finally {
            em.close();
        }
    }

    @Override
    public MenuItem findById(Long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.find(MenuItem.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<MenuItem> findByName(String menuItemName) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery("SELECT m FROM MenuItem m WHERE m.name = :name", MenuItem.class)
                    .setParameter("name", menuItemName)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<MenuItem> findByRestaurant(Restaurant restaurant) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT i FROM MenuItem i " +
                                    "LEFT JOIN FETCH i.categories " +
                                    "WHERE i.restaurant = :restaurant", MenuItem.class)
                    .setParameter("restaurant", restaurant)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public MenuItem findByIdAndRestaurant(Long itemId, Long restaurantId) {
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM MenuItem m " +
                                    "WHERE m.id = :itemId AND m.restaurant.id = :restId",
                            MenuItem.class)
                    .setParameter("itemId", itemId)
                    .setParameter("restId", restaurantId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }


}
