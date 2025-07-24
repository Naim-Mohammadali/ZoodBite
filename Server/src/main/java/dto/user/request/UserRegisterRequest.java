package dto.user.request;

import jakarta.validation.constraints.*;

public record UserRegisterRequest(
        @NotBlank String name,
        @Pattern(regexp="\\+?\\d{8,15}") String phone,
        @Email String email,
        @Size(min = 8) String password,
        String address,
        model.Role role,
        BankInfoDto bank_info) { }
