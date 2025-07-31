package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.Courier;
import model.Role;
import model.Seller;
import util.EntityManagerFactorySingleton;
import model.User;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    private final EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance();

    @Override
    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public User update(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User merged = em.merge(user);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(user) ? user : em.merge(user));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public User findById(Long id) {
        EntityManager em = emf.createEntityManager();
        User u = em.find(User.class, id);
        em.close();
        return u;
    }

    @Override
    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        List<User> list = em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
        em.close();
        return list;
    }

    @Override
    public User findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        User u = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
        em.close();
        return u;
    }

    @Override
    public User findByPhone(String phone) {
        EntityManager em = emf.createEntityManager();
        User u = em.createQuery(
                        "SELECT u FROM User u WHERE u.phone = :phone", User.class)
                .setParameter("phone", phone)
                .getResultStream()
                .findFirst()
                .orElse(null);

        // Force subclass load
        if (u != null) {
            if (u.getRole() == Role.SELLER) {
                u = em.find(Seller.class, u.getId());
            } else if (u.getRole() == Role.COURIER) {
                u = em.find(Courier.class, u.getId());
            }
        }
        em.close();
        return u;
    }


    @Override
    public List<User> findByType(Class<? extends User> type) {
        EntityManager em = emf.createEntityManager();
        List<User> list = em.createQuery(
                        "SELECT u FROM User u WHERE TYPE(u) = :cls", User.class)
                .setParameter("cls", type)
                .getResultList();
        em.close();
        return list;
    }
}