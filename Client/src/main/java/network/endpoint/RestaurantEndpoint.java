package network.endpoint;

import network.ApiClient;
import network.dto.user.CreateRestaurantRequestDto;
import network.dto.user.RestaurantResponseDto;
import network.dto.restaurant.*;
import util.SessionManager;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RestaurantEndpoint {

    public RestaurantResponseDto createRestaurant(CreateRestaurantRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPost("restaurants", dto);
        return ApiClient.getInstance().send(request, RestaurantResponseDto.class);
    }
    public MenuItemResponseDto addMenuItem(Long restaurantId, MenuItemRequestDto dto) throws Exception {
        String path = "restaurants/" + restaurantId + "/item";
        HttpRequest request = ApiClient.getInstance().buildPost(path, dto);

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
        HttpRequest request = ApiClient.getInstance().buildPut(path, dto);

        return ApiClient.getInstance().send(request, MenuItemToMenuResponseDto.class);
    }
    public List<RestaurantResponseDto> getMyRestaurants() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("restaurants/mine");
        return ApiClient.getInstance().sendList(request, RestaurantResponseDto.class);
    }
    public List<RestaurantResponseDto> getMyRestaurantsAndStore() throws Exception {
        List<RestaurantResponseDto> restaurants = getMyRestaurants();

        if (restaurants != null && !restaurants.isEmpty()) {
            // Store the first restaurant in session
            SessionManager.getInstance().setCurrentRestaurant(restaurants.get(0));
        } else {
            SessionManager.getInstance().setCurrentRestaurant(null);
        }

        return restaurants;
    }
    public RestaurantResponseDto updateRestaurant(Long restaurantId, RestaurantUpdateRequestDto dto) throws Exception {
        HttpRequest request = ApiClient.getInstance().buildPatch(
                "restaurants/" + restaurantId, dto
        );

        RestaurantResponseDto updatedRestaurant = ApiClient.getInstance().send(request, RestaurantResponseDto.class);
        SessionManager.getInstance().setCurrentRestaurant(updatedRestaurant);
        SessionManager.getInstance().writeSessionToTemp();

        return updatedRestaurant;
    }
    public List<MenuItemResponseDto> getMenuItems(Long restaurantId, String menuTitle) throws Exception {
        String encodedMenuTitle = URLEncoder.encode(menuTitle, StandardCharsets.UTF_8).replace("+", "%20");
        String path = "restaurants/" + restaurantId + "/menu/" + encodedMenuTitle;

        HttpRequest request = ApiClient.getInstance().buildGet(path);

        return ApiClient.getInstance().sendList(request, MenuItemResponseDto.class);
    }
    public List<MenuItemResponseDto> getUnassignedItems(Long restaurantId) throws Exception {
        // First, refresh restaurant menus from backend
        new RestaurantEndpoint().getMyRestaurantsAndStore();
        RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();

        // Now get all items
        String path = "menu-items/restaurant/" + restaurantId;
        HttpRequest request = ApiClient.getInstance().buildGet(path);
        List<MenuItemResponseDto> allItems = ApiClient.getInstance().sendList(request, MenuItemResponseDto.class);

        // Filter using refreshed data
        List<Long> assignedIds = restaurant.getMenus().stream()
                .flatMap(menu -> menu.getItems().stream())
                .map(RestaurantResponseDto.MenuDto.ItemDto::getItemId)
                .toList();

        return allItems.stream()
                .filter(item -> !assignedIds.contains(item.id))
                .toList();
    }
    /** Browse all active restaurants */
    public List<RestaurantResponseDto> browseRestaurants() throws Exception {
        HttpRequest request = ApiClient.getInstance().buildGet("restaurants");
        return ApiClient.getInstance().sendList(request, RestaurantResponseDto.class);
    }

    /** Get details of a specific restaurant by ID */
    public RestaurantResponseDto getRestaurantById(Long restaurantId) throws Exception {
        String path = "restaurants/" + restaurantId;
        HttpRequest request = ApiClient.getInstance().buildGet(path);
        return ApiClient.getInstance().send(request, RestaurantResponseDto.class);
    }
}
