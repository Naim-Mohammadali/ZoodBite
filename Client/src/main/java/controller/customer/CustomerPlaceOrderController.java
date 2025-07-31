package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.CustomerEndpoint;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerPlaceOrderController implements Initializable {
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private VBox restaurantsContainer;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRestaurants(); // initial load

        // Wire search button
        searchButton.setOnAction(e -> performSearch());
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadRestaurants(); // fallback: show all
            return;
        }

        try {
            CustomerEndpoint endpoint = new CustomerEndpoint();
            List<RestaurantResponseDto> results = endpoint.searchVendors(keyword);

            displayRestaurants(results);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Search Error", "Could not fetch search results.");
        }
    }

    private void loadRestaurants() {
        try {
            RestaurantEndpoint endpoint = new RestaurantEndpoint();
            List<RestaurantResponseDto> restaurants = endpoint.browseRestaurants();

            displayRestaurants(restaurants);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load restaurants.");
        }
    }

    private void displayRestaurants(List<RestaurantResponseDto> restaurants) {
        restaurantsContainer.getChildren().clear();

        if (restaurants == null || restaurants.isEmpty()) {
            Label noRestaurants = new Label("No restaurants found.");
            noRestaurants.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            restaurantsContainer.getChildren().add(noRestaurants);
            return;
        }

        for (RestaurantResponseDto restaurant : restaurants) {
            HBox card = createRestaurantCard(restaurant);
            restaurantsContainer.getChildren().add(card);
        }
    }

    private HBox createRestaurantCard(RestaurantResponseDto restaurant) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D3D3D3; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(600);

        VBox textBox = new VBox(4);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(restaurant.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #391DAA;");

        Label detailsLabel = new Label("☎ " + restaurant.getPhone() + "   |   📍 " + restaurant.getAddress());
        detailsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");

        textBox.getChildren().addAll(nameLabel, detailsLabel);
        card.getChildren().add(textBox);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: #C1C1C1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D3D3D3; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;"));

        // Navigation action (see next fix)
        card.setOnMouseClicked(e -> loadRestaurantPage(restaurant));

        return card;
    }
    private void loadRestaurantPage(RestaurantResponseDto restaurant) {
        try {
            // Save selected restaurant
            SessionManager.getInstance().setCurrentRestaurant(restaurant);
            SessionManager.getInstance().writeSessionToTemp();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/CustomerRestaurant.fxml"));
            Parent restaurantPage = loader.load();

            // Get the mainContent StackPane from the BorderPane root
            BorderPane root = (BorderPane) searchField.getScene().getRoot();
            StackPane mainContent = (StackPane) root.lookup("#mainContent");

            if (mainContent != null) {
                mainContent.getChildren().setAll(restaurantPage);
            } else {
                showAlert("Navigation Error", "Main content area not found.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Navigation Error", "Could not open restaurant page.");
        }
    }



    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
