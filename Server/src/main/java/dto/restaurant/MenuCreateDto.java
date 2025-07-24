package dto.restaurant;

import jakarta.validation.constraints.NotBlank;

public record MenuCreateDto(@NotBlank String title) { }
