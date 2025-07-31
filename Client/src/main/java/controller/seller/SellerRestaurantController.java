package controller.seller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.dto.user.RestaurantResponseDto;
import network.dto.user.UserDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SellerRestaurantController implements Initializable {

    @FXML private ToggleButton mainButton;
    @FXML private ToggleButton menusButton;
    @FXML private ToggleButton itemsButton;
    @FXML private StackPane restaurantContent;

    private final ToggleGroup navGroup = new ToggleGroup();
    @FXML private VBox noRestaurantBox;
    @FXML private Button createRestaurantButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            RestaurantEndpoint endpoint = new RestaurantEndpoint();
            List<RestaurantResponseDto> restaurants = endpoint.getMyRestaurantsAndStore();
            SessionManager.getInstance().writeSessionToTemp();

            RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
            UserDto user = SessionManager.getInstance().getLoggedInUser();

            if (restaurant == null) {
                // Show "no restaurant" box instead of dashboard content
                noRestaurantBox.setVisible(true);

                createRestaurantButton.setOnAction(e -> {
                    try {
                        // Switch entire scene to SellerCreateRestaurant.fxml
                        Parent root = FXMLLoader.load(getClass().getResource("/view/seller/SellerCreateRestaurant.fxml"));
                        Stage stage = (Stage) createRestaurantButton.getScene().getWindow();
                        stage.setScene(new Scene(root, 800, 600));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                disableNav(true);

            } else {
                // Restaurant exists → normal flow
                boolean isPending = user != null && "PENDING".equalsIgnoreCase(user.status);

                mainButton.setToggleGroup(navGroup);
                menusButton.setToggleGroup(navGroup);
                itemsButton.setToggleGroup(navGroup);

                mainButton.setSelected(true);
                loadContent("/view/seller/restaurant/SellerRestaurantMain.fxml");

                mainButton.setOnAction(e -> loadContent("/view/seller/restaurant/SellerRestaurantMain.fxml"));

                if (isPending) {
                    disableButton(menusButton);
                    disableButton(itemsButton);
                } else {
                    menusButton.setOnAction(e -> loadContent("/view/seller/restaurant/SellerRestaurantMenus.fxml"));
                    itemsButton.setOnAction(e -> loadContent("/view/seller/restaurant/SellerRestaurantItems.fxml"));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void disableNav(boolean disable) {
        mainButton.setDisable(disable);
        menusButton.setDisable(disable);
        itemsButton.setDisable(disable);
    }

    private void disableButton(ToggleButton button) {
        button.setOnAction(e -> {
            showAlert("Access Restricted",
                    "Your account is still pending approval by admin.\nYou can only update your restaurant info for now.");
            button.setSelected(false); // reset toggle selection
            mainButton.setSelected(true);
            loadContent("/view/seller/restaurant/SellerRestaurantMain.fxml");

        });
    }

    private void loadContent(String fxmlPath) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            restaurantContent.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
