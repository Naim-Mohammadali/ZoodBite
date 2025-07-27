package example.org.tst1;

import java.util.ArrayList;
import java.util.Arrays;

public class Food {
    private String name;
    private ArrayList<String> categories;
    private double price;
    private String description;
    private String comments;
    private int quantity;


    public Food(String name, String categories, double price, String description) {
        this.name = name;
        this.categories = new ArrayList<>(Arrays.asList(categories.split("\\|")));
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCategories() { return String.join("|", categories); }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
