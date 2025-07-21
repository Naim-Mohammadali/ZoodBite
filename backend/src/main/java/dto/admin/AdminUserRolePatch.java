package dto.admin;
import jakarta.validation.constraints.NotBlank;
public record AdminUserRolePatch(@NotBlank String role) { }
