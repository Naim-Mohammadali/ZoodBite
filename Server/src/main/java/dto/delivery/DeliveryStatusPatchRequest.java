package dto.delivery;

import jakarta.validation.constraints.NotBlank;

public record DeliveryStatusPatchRequest(@NotBlank String status) { }
