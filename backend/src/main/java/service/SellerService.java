package service;

import model.Seller;

public class SellerService extends UserService {

    public Seller viewProfile(Long sellerId) {
        return (Seller) userDAO.findById(sellerId);
    }

    public Seller updatePhone(Long sellerId, String newPhone) {
        if (userDAO.findByPhone(newPhone) != null)
            throw new IllegalArgumentException("Phone already in use!");

        Seller seller = (Seller) userDAO.findById(sellerId);
        seller.setPhone(newPhone);
        userDAO.update(seller);
        return seller;
    }

    public Seller changePassword(Long sellerId, String newPassword) {
        Seller seller = (Seller) userDAO.findById(sellerId);
        seller.setPassword(newPassword);
        userDAO.update(seller);
        return seller;
    }
}
