package dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.Role;
import model.User.Status;

public record LoginResponseDto(
        Long id,
        String name,
        String phone,
        String email,
        String address,
        Status status,
        Role role,
        @JsonProperty("bankName")
        String bankName,
        @JsonProperty("accountNumber")
        String accountNumber,
        Boolean available
) {}
