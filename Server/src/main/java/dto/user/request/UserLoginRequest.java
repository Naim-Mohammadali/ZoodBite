package dto.user.request;

import jakarta.validation.constraints.*;

public record UserLoginRequest(
        @Pattern(regexp="\\+?\\d{8,15}") String phone,
        @NotBlank String password) { }
