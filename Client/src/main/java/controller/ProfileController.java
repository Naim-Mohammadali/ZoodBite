package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import network.dto.user.UserDto;
import network.dto.user.UserUpdateRequestDto;
import network.endpoint.UserEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private ImageView profileImage;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private TextField bankNameField;
    @FXML private TextField accountNumberField;
    @FXML private Button updateButton;
    @FXML private VBox bankContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserDto user = SessionManager.getInstance().getLoggedInUser();

        if (user != null) {
            nameField.setText(user.name);
            phoneField.setText(user.phone);
            addressField.setText(user.address);
            passwordField.setText(user.password != null ? user.password : "");

            // Bank info visible for seller/courier
            if ("SELLER".equalsIgnoreCase(user.role) || "COURIER".equalsIgnoreCase(user.role)) {
                bankContainer.setVisible(true);
                bankContainer.setManaged(true);

                bankNameField.setVisible(true);
                bankNameField.setManaged(true);
                bankNameField.setText(user.bankName != null ? user.bankName : "");

                accountNumberField.setVisible(true);
                accountNumberField.setManaged(true);
                accountNumberField.setText(user.accountNumber != null ? user.accountNumber : "");
            }

// For password
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.setText(user.password != null ? user.password : "");

        }

        updateButton.setOnAction(e -> handleUpdate());
    }


    private void handleUpdate() {
        try {
            UserDto currentUser = SessionManager.getInstance().getLoggedInUser();

            // Only update password if a new one is provided
            String newPassword = passwordField.getText().trim();
            if (newPassword.isEmpty()) {
                newPassword = null; // keep old password
            }

            UserUpdateRequestDto update = new UserUpdateRequestDto(
                    nameField.getText().trim(),
                    currentUser.email,
                    addressField.getText().trim(),
                    newPassword, // null means no change
                    bankNameField.isVisible() ? bankNameField.getText().trim() : null,
                    accountNumberField.isVisible() ? accountNumberField.getText().trim() : null
            );

            UserDto updated = new UserEndpoint().updateProfile(update);

            SessionManager.getInstance().setToken(SessionManager.getInstance().getToken());
            SessionManager.getInstance().setLoggedInUser(updated);
            SessionManager.getInstance().writeSessionToTemp();

            nameField.setText(updated.name);
            phoneField.setText(updated.phone);
            addressField.setText(updated.address);
            passwordField.setText(""); // reset to empty for security
            if (bankContainer.isVisible()) {
                bankNameField.setText(updated.bankName != null ? updated.bankName : "");
                accountNumberField.setText(updated.accountNumber != null ? updated.accountNumber : "");
            }

            showAlert("Profile updated successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Update failed. Please try again.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
