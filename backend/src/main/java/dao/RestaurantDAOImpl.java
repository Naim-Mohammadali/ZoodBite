package dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Restaurant;
import model.User;
import util.EntityManagerFactorySingleton;
import java.util.List;

public class RestaurantDAOImpl implements RestaurantDAO {
    @Override
    public void save(Restaurant restaurant) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(restaurant);
        tx.commit();
        em.close();
    }
    @Override
    public Restaurant findById(Long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        Restaurant restaurant = em.find(Restaurant.class, id);
        em.close();
        return restaurant;
    }
    @Override
    public void update(Restaurant restaurant)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(restaurant);
        tx.commit();
        em.close();
    };
    @Override
    public void delete(Restaurant restaurant)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Restaurant managed = em.find(Restaurant.class, restaurant.getId());
        if (managed != null)
        {
            em.remove(managed);
        }
        tx.commit();
        em.close();
    };
    @Override
    public List<Restaurant> findBySeller(User seller)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<Restaurant> list = em.createQuery("SELECT r FROM Restaurant r WHERE r.seller = :seller",Restaurant.class)
                .setParameter("seller",seller)
                .getResultList();
        em.close();
        return list;
    };
    @Override
    public List<Restaurant> findByStatus(Restaurant.Status status)
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<Restaurant> list = em.createQuery("SLECT r FROM Restaurant r WHERE r.status = :status", Restaurant.class)
                .setParameter("status" , status)
                .getResultList();
        em.close();
        return list;
    };

}
