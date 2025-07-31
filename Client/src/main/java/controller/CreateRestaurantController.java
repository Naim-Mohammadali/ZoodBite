package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import network.dto.user.CreateRestaurantRequestDto;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRestaurantController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField taxFeeField;
    @FXML private TextField additionalFeesField;
    @FXML private Button createButton;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);

        createButton.setOnAction(e -> handleCreateRestaurant());
    }

    private void handleCreateRestaurant() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String taxFeeStr = taxFeeField.getText().trim();
            String additionalFeeStr = additionalFeesField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() ||
                    taxFeeStr.isEmpty() || additionalFeeStr.isEmpty()) {
                showError("Please fill all fields correctly.");
                return;
            }

            int taxFee;
            int additionalFee;
            try {
                taxFee = Integer.parseInt(taxFeeStr);
                additionalFee = Integer.parseInt(additionalFeeStr);
            } catch (NumberFormatException nfe) {
                showError("Tax Fee and Additional Fees must be numbers.");
                return;
            }

            // Call backend
            CreateRestaurantRequestDto dto = new CreateRestaurantRequestDto(name, address, phone, taxFee, additionalFee);
            RestaurantResponseDto restaurant = new RestaurantEndpoint().createRestaurant(dto);

            if (restaurant == null || restaurant.getId() == null) {
                showError("Restaurant creation failed.");
                return;
            }

            // Save restaurant in session
            SessionManager.getInstance().setCurrentRestaurant(restaurant);
            SessionManager.getInstance().writeSessionToTemp();
            System.out.println("✅ Restaurant created: " + restaurant.getName() + " (ID: " + restaurant.getId() + ")");

    // Redirect to Seller Dashboard
            Parent root = FXMLLoader.load(getClass().getResource("/view/seller/SellerDashboard.fxml"));
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));


        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Failed to create restaurant. Try again.");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
