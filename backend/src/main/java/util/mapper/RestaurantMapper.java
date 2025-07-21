package util.mapper;

import dto.restaurant.RestaurantBriefDto;
import dto.restaurant.RestaurantResponseDto;
import model.Restaurant;

public final class RestaurantMapper {
    private RestaurantMapper() {}

    public static RestaurantResponseDto toDto(Restaurant r) {
        return new RestaurantResponseDto(
                r.getId(), r.getName(), r.getAddress(), r.getPhone(),
                r.getLogoBase64(), r.getTaxFee(), r.getAdditionalFee());
    }
    public static RestaurantBriefDto toBriefDto(Restaurant r) {
        return new RestaurantBriefDto(r.getId(), r.getName(), r.getLogoBase64());
    }
}
