package util.mapper;

import dto.restaurant.MenuResponse;
import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantCreateDto;
import dto.restaurant.RestaurantResponseDto;
import model.Restaurant;
import model.Seller;

import java.util.List;


public final class RestaurantMapper {
    private RestaurantMapper() {}

    public static RestaurantResponseDto toDto(Restaurant r) {
        List<MenuResponse> menuDtos;

        try {
            // Try accessing menus
            if (r.getMenus() == null || r.getMenus().isEmpty()) {
                menuDtos = List.of();
            } else {
                menuDtos = r.getMenus().stream()
                        .map(MenuMapper::toDto)
                        .toList();
            }
        } catch (org.hibernate.LazyInitializationException e) {
            // If menus were not initialized due to lazy loading
            menuDtos = List.of();
        }

        return new RestaurantResponseDto(
                r.getId(),
                r.getName(),
                r.getAddress(),
                r.getPhone(),
                r.getLogoBase64(),
                r.getTaxFee(),
                r.getAdditionalFee(),
                r.getStatus(),
                menuDtos
        );
    }

    public static RestaurantBriefDto toBriefDto(Restaurant r) {
        return new RestaurantBriefDto(r.getId(), r.getName(), r.getLogoBase64());
    }

    public static Restaurant fromCreateDto(RestaurantCreateDto dto, Seller seller) {
        return new Restaurant(
                dto.name(), dto.address(), dto.phone(),
                seller, dto.logoBase64(), dto.taxFee(), dto.additionalFee());
    }
}
