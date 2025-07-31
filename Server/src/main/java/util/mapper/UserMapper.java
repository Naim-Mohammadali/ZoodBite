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
            }
            case COURIER -> {
                Courier courier = new Courier(dto.name(), dto.phone(), dto.password(), dto.address());
                if (dto.bank_info() != null) {
                    courier.setBankName(dto.bank_info().bank_name());
                    courier.setAccountNumber(dto.bank_info().account_number());
                }
                yield courier;
            }

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
            bankName = c.getBankName();
            accountNumber = c.getAccountNumber();
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
        String bankname = null;
        String AccountNumber= null;
        if (u.getRole() == Role.COURIER) {
            bankname = ((Courier) u).getBankName();
            AccountNumber = ((Courier) u).getAccountNumber();
        } else if (u.getRole() == Role.SELLER) {
            bankname = ((Seller) u).getBankName();
            AccountNumber = ((Seller) u).getAccountNumber();
        }
        return new UserProfileResponse(
                u.getId(), u.getName(), u.getPhone(), u.getEmail(),
                u.getAddress(), u.getStatus(), bankname,AccountNumber, u.getRole());
    }


    public static UserRegisterRequest toRequest(User u) {
        String bankname = null;
        String AccountNumber= null;
        if (u.getRole() == Role.COURIER) {
            bankname = ((Courier) u).getBankName();
            AccountNumber = ((Courier) u).getAccountNumber();
        } else if (u.getRole() == Role.SELLER) {
            bankname = ((Seller) u).getBankName();
            AccountNumber = ((Seller) u).getAccountNumber();
        }
        BankInfoDto bankInfo = new BankInfoDto(
                bankname,
                AccountNumber
        );
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
