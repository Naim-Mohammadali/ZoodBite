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
        List<MenuResponse> menuDtos = r.getMenus() == null ? List.of()
                : r.getMenus().stream().map(MenuMapper::toDto).toList();


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
