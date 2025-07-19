package util;

import dto.RegisterDTO;
import dto.UserDTO;
import model.*;

public class UserMapper {

    // Convert User → UserDTO
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getStatus()
        );
    }

    // Convert RegisterDTO → concrete User subclass
    public static User fromRegisterDTO(RegisterDTO dto) {
        return switch (dto.getRole()) {
            case CUSTOMER -> new Customer(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case SELLER   -> new Seller(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case COURIER  -> new Courier(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case ADMIN    -> new Admin(dto.getName(), dto.getPhone(), dto.getPassword());
        };
    }



}
