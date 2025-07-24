package dao;

import model.Menu;

public interface MenuDAO {
    Menu findWithItems(Long menuId);
}
