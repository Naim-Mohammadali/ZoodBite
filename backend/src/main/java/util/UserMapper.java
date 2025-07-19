package util;

import dto.user.RegisterDTO;
import dto.user.UserProfileDTO;
import model.*;

public class UserMapper {

    // Convert User → UserProfileDTO
    public static UserProfileDTO toDTO(User user) {
        if (user == null) return null;
        return new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getStatus()
        );
    }

    // Convert RegisterRequestDTO → concrete User subclass
    public static User fromRegisterDTO(RegisterDTO dto) {
        return switch (dto.getRole()) {
            case CUSTOMER -> new Customer(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case SELLER   -> new Seller(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case COURIER  -> new Courier(dto.getName(), dto.getPhone(), dto.getPassword(), dto.getAddress());
            case ADMIN    -> new Admin(dto.getName(), dto.getPhone(), dto.getPassword());
        };
    }



}
