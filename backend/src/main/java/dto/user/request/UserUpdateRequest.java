package dto.user.request;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
         String name,
         String address,
        @Email  String email) { }
