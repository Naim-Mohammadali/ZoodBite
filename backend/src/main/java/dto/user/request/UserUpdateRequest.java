package dto.user.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
         String name,
         String address,
        @Nullable @Email  String email) { }
