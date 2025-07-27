package example.org.tst1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddFoodController {
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    private Restaurant restaurant;
    private RestaurantController restaurantController;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setRestaurantController(RestaurantController controller) {
        this.restaurantController = controller;
    }

    @FXML
    private void handleSubmit() {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String description = descriptionField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (name.isEmpty() || category.isEmpty()) {
                showAlert("Error", "Name and category are required fields");
                return;
            }

            Food newFood = new Food(name, category, price, description);
            restaurant.addFood(newFood, quantity);

            // Refresh the table in the main controller
            if (restaurantController != null) {
                restaurantController.refreshTable();
            }

            // Close the window
            nameField.getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for price and quantity");
        }
    }

    @FXML
    private void handleGoBack() {
        nameField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}