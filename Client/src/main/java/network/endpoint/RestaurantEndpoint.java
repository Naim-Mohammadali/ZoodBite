package network.endpoint;

import network.ApiClient;
import network.dto.user.CreateRestaurantRequestDto;
import network.dto.user.RestaurantResponseDto;
import network.dto.restaurant.*;
import util.SessionManager;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class RestaurantEndpoint {

    public RestaurantResponseDto createRestaurant(CreateRestaurantRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("restaurants", dto);
        return ApiClient.getInstance().send(request, RestaurantResponseDto.class);
    }
    public MenuItemResponseDto addMenuItem(Long restaurantId, MenuItemRequestDto dto) throws Exception {
        String path = "restaurants/" + restaurantId + "/item";
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/" + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return ApiClient.getInstance().send(request, MenuItemResponseDto.class);
    }
    public MenuCreateResponseDto addMenuToRestaurant(Long restaurantId, String title) throws Exception {
        MenuCreateRequestDto dto = new MenuCreateRequestDto(title);

        HttpRequest request = ApiClient.getInstance().buildPost(
                "restaurants/" + restaurantId + "/menu", dto
        );

        return ApiClient.getInstance().send(request, MenuCreateResponseDto.class);
    }
    public MenuItemToMenuResponseDto addItemsToMenu(Long restaurantId, String menuTitle, List<Long> itemIds) throws Exception {
        String encodedMenuTitle = URLEncoder.encode(menuTitle, StandardCharsets.UTF_8).replace("+", "%20");
        String path = "restaurants/" + restaurantId + "/menu/" + encodedMenuTitle;


        MenuItemToMenuRequestDto dto = new MenuItemToMenuRequestDto(itemIds);
        String json = ApiClient.getInstance().getObjectMapper().writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/" + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return ApiClient.getInstance().send(request, MenuItemToMenuResponseDto.class);
    }

}
