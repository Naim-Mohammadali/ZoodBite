package util;

import network.dto.order.PlaceOrderResponseDto;
import network.dto.user.RestaurantResponseDto;
import network.dto.user.UserDto;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private String token;
    private UserDto loggedInUser;
    private RestaurantResponseDto currentRestaurant;

    private final File tempFile = new File("temp-session.txt");

    // 🛒 Cart (itemId → quantity)
    private Map<Long, Integer> currentCart = new HashMap<>();

    // 📜 Order history
    private List<PlaceOrderResponseDto> orderHistory = new ArrayList<>();

    public static SessionManager getInstance() { return instance; }

    // ---------- Token / User ----------
    public void setToken(String token) { this.token = token; }
    public String getToken() { return token; }

    public UserDto getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(UserDto user) { this.loggedInUser = user; }

    public RestaurantResponseDto getCurrentRestaurant() { return currentRestaurant; }
    public void setCurrentRestaurant(RestaurantResponseDto restaurant) { this.currentRestaurant = restaurant; }

    // ---------- Cart ----------
    public Map<Long, Integer> getCurrentCart() { return currentCart; }
    public void setCurrentCart(Map<Long, Integer> cart) { this.currentCart = cart; }

    public void clearCart() {
        currentCart.clear();
        writeSessionToTemp();
    }

    // ---------- Order History ----------
    public List<PlaceOrderResponseDto> getOrderHistory() { return orderHistory; }

    public void addOrderToHistory(PlaceOrderResponseDto order) {
        if (order != null) {
            orderHistory.add(order);
            writeSessionToTemp();
        }
    }

    // ---------- Session persistence ----------
    public void clear() {
        token = null;
        loggedInUser = null;
        currentRestaurant = null;
        currentCart.clear();
        orderHistory.clear();
        deleteTempFile();
    }

    public void writeSessionToTemp() {
        try (FileWriter writer = new FileWriter(tempFile)) {
            ObjectMapper mapper = new ObjectMapper();

            writer.write("Token=" + (token != null ? token : "") + "\n");

            if (loggedInUser != null)
                writer.write("User=" + mapper.writeValueAsString(loggedInUser) + "\n");
            if (currentRestaurant != null)
                writer.write("Restaurant=" + mapper.writeValueAsString(currentRestaurant) + "\n");
            if (!currentCart.isEmpty())
                writer.write("Cart=" + mapper.writeValueAsString(currentCart) + "\n");
            if (!orderHistory.isEmpty())
                writer.write("History=" + mapper.writeValueAsString(orderHistory) + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSessionFromTempFile() {
        if (!tempFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            String tokenLine = null, userLine = null, restaurantLine = null, cartLine = null, historyLine = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Token=")) tokenLine = line.substring("Token=".length());
                else if (line.startsWith("User=")) userLine = line.substring("User=".length());
                else if (line.startsWith("Restaurant=")) restaurantLine = line.substring("Restaurant=".length());
                else if (line.startsWith("Cart=")) cartLine = line.substring("Cart=".length());
                else if (line.startsWith("History=")) historyLine = line.substring("History=".length());
            }

            ObjectMapper mapper = new ObjectMapper();
            if (tokenLine != null) this.token = tokenLine;
            if (userLine != null) this.loggedInUser = mapper.readValue(userLine, UserDto.class);
            if (restaurantLine != null) this.currentRestaurant = mapper.readValue(restaurantLine, RestaurantResponseDto.class);
            if (cartLine != null) this.currentCart = mapper.readValue(cartLine, new TypeReference<Map<Long, Integer>>() {});
            if (historyLine != null) this.orderHistory = mapper.readValue(historyLine, new TypeReference<List<PlaceOrderResponseDto>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTempFile() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
