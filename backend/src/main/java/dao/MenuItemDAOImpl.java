package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.MenuItem;
import model.Restaurant;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO{
    @Override
    public void save(MenuItem menuItem) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(menuItem);
        tx.commit();
        em.close();
    }
    @Override
    public void update(MenuItem menuItem)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(menuItem);
        tx.commit();
        em.close();
    };
    @Override
    public void delete(MenuItem menuItem)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        MenuItem onList = em.find(MenuItem.class, menuItem.getId());
        if (onList != null)
        {
            em.remove(onList);
        }
        tx.commit();
        em.close();
    }

    @Override
    public MenuItem findById(Long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        MenuItem menuItem = em.find(MenuItem.class, id);
        em.close();
        return menuItem;
    }
    @Override
    public List<MenuItem> findByName (String menuItemName) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<MenuItem> menuItems = em.createQuery("SELECT m FROM MenuItem m WHERE m.name = :name", MenuItem.class)
                .setParameter("name", menuItemName)
                .getResultList();
        em.close();
        return menuItems;
    }
    @Override
    public List<MenuItem> findByRestaurant(Restaurant restaurant)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<MenuItem> restaurantMenu = em.createQuery("SELECT m FROM MenuItem m WHERE m.restaurant = :restaurant", MenuItem.class)
                .setParameter("restaurant", restaurant)
                .getResultList();
        em.close();
        return restaurantMenu;
    }
}
