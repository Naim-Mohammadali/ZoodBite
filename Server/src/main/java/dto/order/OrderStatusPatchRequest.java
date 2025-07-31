package dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusPatchRequest(@NotBlank String status) { }
