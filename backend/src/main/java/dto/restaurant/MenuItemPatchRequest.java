package dto.restaurant;

import java.util.List;

public record MenuItemPatchRequest(List<Long> item_ids) {}