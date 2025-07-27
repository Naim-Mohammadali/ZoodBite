package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.dto.user.LoginRequestDto;
import network.endpoint.UserEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Label errorLabel;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink signUpLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        errorLabel.setVisible(false);
        loginButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String password = passwordField.getText().trim();
            if (phone.isEmpty() || password.isEmpty()) {
                errorLabel.setText("*Please enter phone and password");
                errorLabel.setVisible(true);
                return;
            }
            try {
                var dto = new LoginRequestDto(phone, password);
                var endpoint = new UserEndpoint();
                var response = endpoint.login(dto);

                // Save session
                SessionManager.getInstance().setToken(response.token);
                SessionManager.getInstance().setLoggedInUser(response.user);
                SessionManager.getInstance().writeSessionToTemp();

                // Redirect based on role
                String role = response.user.role.toLowerCase();
                String fxml = switch (role) {
                    case "customer" -> "/view/CustomerDashboard.fxml";
                    case "seller"   -> "/view/SellerDashboard.fxml";
                    case "delivery" -> "/view/DeliveryDashboard.fxml";
                    case "admin"    -> "/view/AdminDashboard.fxml";
                    default -> throw new IllegalArgumentException("Unknown role: " + role);
                };

                Parent root = FXMLLoader.load(getClass().getResource(fxml));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));

            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("*Invalid Phone or Password");
                errorLabel.setVisible(true);
            }
        });
        signUpLink.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
                Stage stage = (Stage) signUpLink.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
