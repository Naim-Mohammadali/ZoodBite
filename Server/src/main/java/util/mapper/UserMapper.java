package util.mapper;

import dto.user.request.*;
import dto.user.response.LoginResponseDto;
import dto.user.response.UserProfileResponse;
import model.*;

public final class UserMapper {
    private UserMapper() {}

    public static User toEntity(UserRegisterRequest dto) {
        return switch (dto.role()) {
            case CUSTOMER -> new Customer(dto.name(), dto.phone(), dto.password(), dto.address());
            case SELLER -> {
                Seller seller = new Seller(dto.name(), dto.phone(), dto.password(), dto.address());
                if (dto.bank_info() != null) {
                    seller.setBankName(dto.bank_info().bank_name());
                    seller.setAccountNumber(dto.bank_info().account_number());
                }
                yield seller;
            }            case COURIER  -> new Courier (dto.name(), dto.phone(), dto.password(), dto.address());
            case ADMIN    -> new Admin   (dto.name(), dto.phone(), dto.password());
            default       -> throw new IllegalArgumentException("Invalid role: " + dto.role());
        };
    }

    public static LoginResponseDto toDto(User u) {
        String bankName = null;
        String accountNumber = null;
        Boolean available = null;

        if (u instanceof Seller s) {
            bankName = s.getBankName();
            accountNumber = s.getAccountNumber();
        } else if (u instanceof Courier c) {
            available = c.isAvailable();
        }

        return new LoginResponseDto(
                u.getId(),
                u.getName(),
                u.getPhone(),
                u.getEmail(),
                u.getAddress(),
                u.getStatus(),
                u.getRole(),
                bankName,
                accountNumber,
                available
        );
    }
    public static UserProfileResponse toProfileDto(User u) {
        return new UserProfileResponse(
                u.getId(), u.getName(), u.getPhone(), u.getEmail(),
                u.getAddress(), u.getStatus(), u.getRole());
    }


    public static UserRegisterRequest toRequest(User u) {
        BankInfoDto bankInfo = null;

        if (u instanceof Seller seller) {
            bankInfo = new BankInfoDto(
                    seller.getBankName(),
                    seller.getAccountNumber()
            );
        }

        return new UserRegisterRequest(
                u.getName(),
                u.getPhone(),
                u.getEmail(),
                u.getPassword(),
                u.getAddress(),
                u.getRole(),
                bankInfo
        );
    }

}
