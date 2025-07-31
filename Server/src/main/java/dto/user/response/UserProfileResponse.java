package dto.user.response;

import model.Role;
import model.User.Status;

public record UserProfileResponse(
        long id,
        String name,
        String phone,
        String email,
        String address,
        Status status,
        String bankName,
        String accountNumber,
        Role role) { }
