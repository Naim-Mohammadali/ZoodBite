package service;

import model.User;

public class SellerService extends UserService {

    public void updateProfile(User seller, String newPhone, String newAddress) throws Exception {
        if (seller.getRole() != User.Role.SELLER) {
            throw new Exception("Not a seller!");
        }
        if (userDAO.findByPhone(newPhone) != null) {
            throw new Exception("Phone already in use!");
        }
        seller.setPhone(newPhone);
        seller.setAddress(newAddress);
        userDAO.update(seller);
    }

    public boolean hasApprovedRestaurant(User seller) throws Exception {
        if (seller.getRole() != User.Role.SELLER) {
            throw new Exception("Not a seller!");
        }
        return false;
    }
}
