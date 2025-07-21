package dto.restaurant;

import jakarta.validation.constraints.Min;

public record ItemIdDto(@Min(1) Integer itemId) { }
