package dto.user.response;

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
        String bankName,
        String accountNumber,
        Boolean available
) {}
