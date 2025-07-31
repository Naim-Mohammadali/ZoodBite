package controller.seller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.dto.restaurant.MenuItemRequestDto;
import network.dto.restaurant.MenuItemResponseDto;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SellerRestaurantItemsController implements Initializable {

    @FXML private Label restaurantNameLabel;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField descriptionField;
    @FXML private TextField quantityField;
    @FXML private TextField categoriesField;
    @FXML private Button addItemButton;
    @FXML private VBox itemsContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
        if (restaurant != null) {
            restaurantNameLabel.setText("Items for " + restaurant.getName());
        }

        addItemButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String priceStr = priceField.getText().trim();
                String description = descriptionField.getText().trim();
                String quantityStr = quantityField.getText().trim();
                String categoriesStr = categoriesField.getText().trim();

                if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                    showAlert("Error", "Name, Price, and Quantity are required!");
                    return;
                }

                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                List<String> categories = categoriesStr.isEmpty()
                        ? List.of()
                        : Arrays.asList(categoriesStr.split("\\s+"));

                RestaurantResponseDto restaurantObj = SessionManager.getInstance().getCurrentRestaurant();
                if (restaurantObj == null) {
                    showAlert("Error", "No restaurant found in session.");
                    return;
                }

                MenuItemRequestDto dto = new MenuItemRequestDto(
                        name, description, price, quantity, categories, restaurantObj.getId()
                );

                RestaurantEndpoint endpoint = new RestaurantEndpoint();
                MenuItemResponseDto response = endpoint.addMenuItem(restaurantObj.getId(), dto);

                // Add item card to UI
                HBox itemCard = new HBox(20);
                itemCard.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 8; -fx-padding: 10;");
                Label label = new Label(response.name + " | $" + response.price
                        + " | Qty: " + response.quantity
                        + (response.description != null ? " | " + response.description : ""));
                label.setStyle("-fx-font-size: 14px; -fx-text-fill: #1C1C1C;");
                itemCard.getChildren().add(label);

                itemsContainer.getChildren().add(itemCard);

                // Clear fields
                nameField.clear();
                priceField.clear();
                descriptionField.clear();
                quantityField.clear();
                categoriesField.clear();

                showAlert("Success", "Item '" + response.name + "' added successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add item.");
            }
        });
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
