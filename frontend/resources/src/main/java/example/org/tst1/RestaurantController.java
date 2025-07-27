package example.org.tst1;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class RestaurantController {

    @FXML private TableView<Food> foodTable;
    @FXML private TableColumn<Food, String> nameColumn;
    @FXML private TableColumn<Food, String> categoryColumn;
    @FXML private TableColumn<Food, Double> priceColumn;
    @FXML private TableColumn<Food, Integer> quantityColumn;
    @FXML private Label restaurantNameLabel;
    @FXML private Button addFoodButton;


    private Restaurant restaurant;


        public void initialize() {
            try {
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            } catch (Exception e) {
                System.err.println("Error initializing table columns: " + e.getMessage());
            }
        }


    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        refreshTable();
        restaurantNameLabel.setText(restaurant.getRestaurantName());
        foodTable.getItems().setAll(restaurant.getFoodList());
    }

    void refreshTable() {
        if (restaurant != null) {
            foodTable.getItems().setAll(restaurant.getFoodList());
        }
    }

    @FXML
    private void handleAddFood() {
        // You'll implement this later
        System.out.println("Add food button clicked");
    }
}