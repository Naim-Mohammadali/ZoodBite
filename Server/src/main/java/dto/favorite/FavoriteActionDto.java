package dto.favorite;

import jakarta.validation.constraints.NotNull;

public record FavoriteActionDto(@NotNull Long restaurantId) { }
