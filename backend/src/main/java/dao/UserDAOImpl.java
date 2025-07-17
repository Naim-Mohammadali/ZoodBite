package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.User;
import util.EntityManagerFactorySingleton;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void save(User user) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(user);
        tx.commit();
        em.close();
    }

    @Override
    public User findById(Long id) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        User user = em.find(User.class, id);
        em.close();
        return user;
    }

    @Override
    public void update(User user) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(user);
        tx.commit();
        em.close();
    }

    @Override
    public void delete(User user) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        User managedUser = em.find(User.class, user.getId());
        if (managedUser != null) {
            em.remove(managedUser);
        }
        tx.commit();
        em.close();
    }
    @Override
    public User findByEmail(String email) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
        em.close();
        return user;
    }
    @Override
    public User findByPhone(String phone) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        User user = em.createQuery("SELECT u FROM User u WHERE u.phone = :phone", User.class)
                .setParameter("phone", phone)
                .getResultStream()
                .findFirst()
                .orElse(null);
        em.close();
        return user;
    }
    @Override
    public List<User> findAll()
    {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<User> users = em.createQuery("Select u FROM User u", User.class).getResultList();
        em.close();
        return users;
    }
    @Override
    public List<User> findByRole(User.Role role) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.ROLE = :role", User.class)
                .setParameter("role", role)
                .getResultList();
        em.close();
        return users;
    }
    @Override
    public List<User> findByStatus(User.Status status) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        List<model.User> users = em.createQuery("SELECT u FROM User u WHERE u.Status = :status", model.User.class)
                .setParameter("status", status)
                .getResultList();
        em.close();
        return users;
    }
}
