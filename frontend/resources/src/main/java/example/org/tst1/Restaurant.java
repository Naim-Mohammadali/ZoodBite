package example.org.tst1;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant extends User{
    private String address;
    private String name;
    private String description;
    private ArrayList<String> categories;
    private String profilePicture;
    private HashMap<Food,Integer>foods;
    private String restaurant;
    public Restaurant(String username ,String name, String lastName, String phoneNumber, String email, String password, String address) {
        super(username,name, lastName, phoneNumber, email, password);
        this.address = address;
    }

    public Restaurant(String username ,String name, String lastName, String phoneNumber, String password, String address) {
        super (username ,name, lastName, phoneNumber, password);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
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

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}


