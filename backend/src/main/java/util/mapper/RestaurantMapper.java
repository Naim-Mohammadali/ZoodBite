package util.mapper;

import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantCreateDto;
import dto.restaurant.RestaurantResponseDto;
import model.Restaurant;
import model.Seller;

public final class RestaurantMapper {
    private RestaurantMapper() {}

    public static RestaurantResponseDto toDto(Restaurant r) {
        return new RestaurantResponseDto(
                r.getId(), r.getName(), r.getAddress(), r.getPhone(),
                r.getLogoBase64(), r.getTaxFee(), r.getAdditionalFee(), r.getStatus());
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
