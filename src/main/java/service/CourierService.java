package service;

import model.User;

public class CourierService extends UserService {

    public void setAvailability(User courier, boolean available) throws Exception {
        if (courier.getRole() != User.Role.COURIER) {
            throw new Exception("Not a courier!");
        }
        courier.setAvailable(available);
        userDAO.update(courier);
    }

    public void updateProfile(User courier, String newPhone) throws Exception {
        if (courier.getRole() != User.Role.COURIER) {
            throw new Exception("Not a courier!");
        }
        if (userDAO.findByPhone(newPhone) != null) {
            throw new Exception("Phone already in use!");
        }
        courier.setPhone(newPhone);
        userDAO.update(courier);
    }
}
