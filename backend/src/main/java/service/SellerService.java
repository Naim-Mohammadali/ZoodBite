package service;

import model.Seller;
public class SellerService extends UserService {


    public Seller updatePhone(Seller seller, String newPhone) throws Exception {
        if (userDAO.findByPhone(newPhone) != null) {
            throw new Exception("Phone already in use!");
        }
        seller.setPhone(newPhone);
        return (Seller) userDAO.update(seller);
    }

    public Seller changePassword(Seller seller, String newPassword) {
        seller.setPassword(newPassword);
        return (Seller) userDAO.update(seller);
    }

    public Seller viewProfile(Seller seller) {
        return (Seller) userDAO.findById(seller.getId());
    }
}
