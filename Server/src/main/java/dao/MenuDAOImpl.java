package dao;

import jakarta.persistence.EntityManager;
import model.Menu;
import util.EntityManagerFactorySingleton;

public class MenuDAOImpl implements MenuDAO {
    @Override
    public Menu findWithItems(Long menuId) {
        EntityManager em = EntityManagerFactorySingleton.getInstance().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Menu m LEFT JOIN FETCH m.items WHERE m.id = :id", Menu.class)
                    .setParameter("id", menuId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
