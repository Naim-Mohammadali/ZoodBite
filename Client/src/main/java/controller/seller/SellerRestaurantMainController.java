package controller.seller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.dto.user.RestaurantResponseDto;
import network.dto.restaurant.RestaurantUpdateRequestDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SellerRestaurantMainController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField taxFeeField;
    @FXML private TextField additionalFeeField;
    @FXML private Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();

        if (restaurant != null) {
            fillFields(restaurant);
        }

        updateButton.setOnAction(e -> handleUpdate());
    }

    private void fillFields(RestaurantResponseDto restaurant) {
        nameField.setText(restaurant.getName());
        addressField.setText(restaurant.getAddress());
        phoneField.setText(restaurant.getPhone());
        taxFeeField.setText(restaurant.getTaxFee() != null ? restaurant.getTaxFee().toString() : "");
        additionalFeeField.setText(restaurant.getAdditionalFee() != null ? restaurant.getAdditionalFee().toString() : "");
    }

    private void handleUpdate() {
        try {
            RestaurantResponseDto current = SessionManager.getInstance().getCurrentRestaurant();
            if (current == null) {
                showAlert("Error", "No restaurant found in session.");
                return;
            }

            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            Integer taxFee = Integer.parseInt(taxFeeField.getText().trim());
            Integer additionalFee = Integer.parseInt(additionalFeeField.getText().trim());

            RestaurantUpdateRequestDto dto = new RestaurantUpdateRequestDto(
                    name, address, phone,null, taxFee, additionalFee
            );

            RestaurantEndpoint endpoint = new RestaurantEndpoint();
            RestaurantResponseDto updated = endpoint.updateRestaurant(current.getId(), dto);

            SessionManager.getInstance().setCurrentRestaurant(updated);
            SessionManager.getInstance().writeSessionToTemp();
            fillFields(updated);

            showAlert("Success", "Restaurant updated successfully!");

        } catch (NumberFormatException nfe) {
            showAlert("Invalid Input", "Tax Fee and Additional Fee must be numbers.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update restaurant.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
