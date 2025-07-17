package service;

import model.Courier;

public class CourierService extends UserService {


    public Courier setAvailability(Courier courier, boolean available) {
        courier.setAvailable(available);
        return (Courier) userDAO.update(courier);
    }


    public Courier changePhone(Courier courier, String newPhone) throws Exception {
        if (userDAO.findByPhone(newPhone) != null) {
            throw new Exception("Phone already in use!");
        }
        courier.setPhone(newPhone);
        return (Courier) userDAO.update(courier);
    }

    public Courier changePassword(Courier courier, String newPassword) {
        courier.setPassword(newPassword);
        return (Courier) userDAO.update(courier);
    }

    public Courier viewProfile(Courier courier) {
        return (Courier) userDAO.findById(courier.getId());
    }
}
