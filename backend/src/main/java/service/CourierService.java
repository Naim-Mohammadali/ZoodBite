package service;

import model.Courier;

public class CourierService extends UserService {

    public Courier setAvailability(Long courierId, boolean available) {
        Courier courier = (Courier) userDAO.findById(courierId);
        if (courier == null) throw new IllegalArgumentException("Courier not found");

        courier.setAvailable(available);
        userDAO.update(courier);
        return courier;
    }

    public Courier changePhone(Long courierId, String newPhone) throws Exception {
        Courier courier = (Courier) userDAO.findById(courierId);
        if (courier == null) throw new IllegalArgumentException("Courier not found");

        if (userDAO.findByPhone(newPhone) != null)
            throw new Exception("Phone already in use!");

        courier.setPhone(newPhone);
        userDAO.update(courier);
        return courier;
    }

    public Courier changePassword(Long courierId, String newPassword) {
        Courier courier = (Courier) userDAO.findById(courierId);
        if (courier == null) throw new IllegalArgumentException("Courier not found");

        courier.setPassword(newPassword);
        userDAO.update(courier);
        return courier;
    }

    public Courier viewProfile(Long courierId) {
        Courier courier = (Courier) userDAO.findById(courierId);
        if (courier == null) throw new IllegalArgumentException("Courier not found");

        return courier;
    }
}
