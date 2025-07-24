package dto.restaurant;

import java.util.List;

public record MenuResponse(Long id, String title, List<ItemIdDto> items) {}