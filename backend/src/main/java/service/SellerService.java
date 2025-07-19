package service;

import dto.user.request.UserRegisterRequest;
import util.mapper.UserMapper;
import model.Role;
import model.Seller;


public class SellerService extends UserService {


    public SellerService() {
        super();
    }


    public Seller registerSeller(UserRegisterRequest dto) {
        if (dto == null) throw new IllegalArgumentException("DTO must not be null");

        if (dto.role() != Role.SELLER) {
            dto = new UserRegisterRequest(
                    dto.name(), dto.phone(), dto.email(),
                    dto.password(), dto.address(), Role.SELLER);
        }

        Seller seller = (Seller) UserMapper.toEntity(dto);
        return (Seller) register(seller);
    }


    public Seller viewProfile(long sellerId) {
        return (Seller) findById(sellerId);
    }


    public Seller updatePhone(long sellerId, String newPhone) {
        if (findByPhone(newPhone) != null)
            throw new IllegalArgumentException("Phone already in use!");

        Seller s = (Seller) findById(sellerId);
        s.setPhone(newPhone);
        return (Seller) update(s);
    }

    public Seller changePassword(long sellerId, String newPassword) {
        Seller s = (Seller) findById(sellerId);
        s.setPassword(newPassword);
        return (Seller) update(s);
    }
}
