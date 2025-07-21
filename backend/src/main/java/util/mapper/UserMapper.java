package util.mapper;

import dto.user.request.*;
import dto.user.response.UserProfileResponse;
import model.*;

public final class UserMapper {
    private UserMapper() {}

    public static User toEntity(UserRegisterRequest dto) {
        return switch (dto.role()) {
            case CUSTOMER -> new Customer(dto.name(), dto.phone(), dto.password(), dto.address());
            case SELLER   -> new Seller  (dto.name(), dto.phone(), dto.password(), dto.address());
            case COURIER  -> new Courier (dto.name(), dto.phone(), dto.password(), dto.address());
            case ADMIN    -> new Admin   (dto.name(), dto.phone(), dto.password());
        };
    }

    public static UserProfileResponse toDto(User u) {
        return new UserProfileResponse(
                u.getId(), u.getName(), u.getPhone(), u.getEmail(),
                u.getAddress(), u.getStatus(), u.getRole());
    }

    public static UserRegisterRequest toRequest(User u) {
        return new UserRegisterRequest(
                u.getName(),
                u.getPhone(),
                u.getEmail(),
                u.getPassword(),        // hashed or plain – test only
                u.getAddress(),
                u.getRole());
    }
}
