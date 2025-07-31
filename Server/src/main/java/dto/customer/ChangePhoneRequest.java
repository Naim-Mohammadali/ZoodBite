package dto.customer;

import jakarta.validation.constraints.NotBlank;

public record ChangePhoneRequest(@NotBlank String phone) {}
