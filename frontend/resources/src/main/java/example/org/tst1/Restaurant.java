package example.org.tst1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Restaurant extends User{
    private String address;
    private String description;
    private ArrayList<String> categories;
    private String profilePicture;
    private HashMap<Food,Integer>foods;
    private String restaurantName;
    public Restaurant(String username ,String name, String lastName, String phoneNumber, String email, String password, String address) {
        super(username,name, lastName, phoneNumber, email, password);
        this.address = address;
        this.foods = new HashMap<>();
        this.categories = new ArrayList<>();
    }

    public Restaurant(String username ,String name, String lastName, String phoneNumber, String password, String address) {
        super (username ,name, lastName, phoneNumber, password);
        this.address = address;
        this.foods = new HashMap<>();
        this.categories = new ArrayList<>();
    }

    public void addFood(Food food) {
        this.foods.put(food, 0); // Default quantity is 0
    }

    public void addFood(Food food, int quantity) {
        this.foods.put(food, quantity);
    }

    public void removeFood(Food food) {
        this.foods.remove(food);
    }

    public void updateFoodQuantity(Food food, int newQuantity) {
        if (this.foods.containsKey(food)) {
            this.foods.put(food, newQuantity);
        }
    }

    // Get all foods as a List (useful for TableView)
    public ObservableList<Food> getFoodList() {
        return FXCollections.observableArrayList(foods.keySet());
    }

    // Get quantity for a specific food
    public int getFoodQuantity(Food food) {
        return this.foods.getOrDefault(food, 0);
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = new ArrayList<>(Arrays.asList(categories.split("\\|")));
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public HashMap<Food, Integer> getFoods() {
        return foods;
    }

    public void setFoods(HashMap<Food, Integer> foods) {
        this.foods = foods;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


    @Override
    public String toString() {
        return  ""+categories ;
    }
}


