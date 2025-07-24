package dto.courier;

import jakarta.validation.constraints.NotBlank;

public record CourierStatusPatchRequest(
        @NotBlank String status
) {}
