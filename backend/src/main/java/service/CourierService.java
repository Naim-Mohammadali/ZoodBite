package service;

import dto.user.request.UserRegisterRequest;
import util.mapper.UserMapper;
import model.Courier;
import model.Role;

public class CourierService extends UserService {


    public CourierService() {
        super();
    }

    public Courier registerCourier(UserRegisterRequest dto) {
        if (dto == null) throw new IllegalArgumentException("DTO must not be null");

        if (dto.role() != Role.COURIER) {
            dto = new UserRegisterRequest(
                    dto.name(), dto.phone(), dto.email(),
                    dto.password(), dto.address(), Role.COURIER);
        }

        Courier courier = (Courier) UserMapper.toEntity(dto);
        return (Courier) register(courier);
    }


    public Courier viewProfile(long courierId) {
        return (Courier) findById(courierId);
    }


    public Courier setAvailability(long courierId, boolean available) {
        Courier c = (Courier) findById(courierId);
        c.setAvailable(available);
        return (Courier) update(c);
    }

    public Courier changePhone(long courierId, String newPhone) {
        if (findByPhone(newPhone) != null)
            throw new IllegalArgumentException("Phone already in use!");

        Courier c = (Courier) findById(courierId);
        c.setPhone(newPhone);
        return (Courier) update(c);
    }

    public Courier changePassword(long courierId, String newPassword) {
        Courier c = (Courier) findById(courierId);
        c.setPassword(newPassword);
        return (Courier) update(c);
    }
}
