package dto.admin;
import jakarta.validation.constraints.NotBlank;
public record AdminRestaurantStatusPatch(@NotBlank String status) { }
