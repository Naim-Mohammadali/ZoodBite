package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.dto.user.BankInfoDto;
import network.dto.user.RegisterRequestDto;
import network.endpoint.UserEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public TextField nameField;
    public TextField emailField;
    public Hyperlink loginLink;
    public ComboBox roleComboBox;
    public Label bankNameLabel;
    public TextField bankNameField;
    public Label cardNumberLabel;
    public TextField cardNumberField;
    public Label errorLabel;
    public TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button SignUpButton;
    @FXML private ImageView logoImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);

        loginLink.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
                Stage stage = (Stage) loginLink.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        roleComboBox.setOnAction(e -> {
            String selectedRole = ((String) roleComboBox.getValue()).toUpperCase();
            boolean showBankInfo = "SELLER".equals(selectedRole) || "COURIER".equals(selectedRole);

            bankNameLabel.setVisible(showBankInfo);
            bankNameField.setVisible(showBankInfo);
            cardNumberLabel.setVisible(showBankInfo);
            cardNumberField.setVisible(showBankInfo);

            // Ensure they take up space only when visible
            bankNameLabel.setManaged(showBankInfo);
            bankNameField.setManaged(showBankInfo);
            cardNumberLabel.setManaged(showBankInfo);
            cardNumberField.setManaged(showBankInfo);
        });


        SignUpButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String address = addressField.getText().trim();
            String role = ((String) roleComboBox.getValue()).toUpperCase();
            boolean isSeller = "SELLER".equalsIgnoreCase(role);
            boolean isCourier = "COURIER".equalsIgnoreCase(role);
            String bank = bankNameField.getText().trim();
            String card = cardNumberField.getText().trim();

            // Validate fields
            boolean invalid =
                    name.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty() || isSeller && (bank.isEmpty() || card.isEmpty());

            if (invalid) {
                errorLabel.setText("Please fill all the fields correctly");
                errorLabel.setVisible(true);
                return;
            }

            try {
                var bankInfo = (isSeller || isCourier)? new BankInfoDto(bank, card) : null;
                var dto = new RegisterRequestDto(name, phone, email, password, address, role, bankInfo);
                var response = new UserEndpoint().register(dto);

                SessionManager.getInstance().setToken(response.token);
                SessionManager.getInstance().setLoggedInUser(response.user);
                SessionManager.getInstance().writeSessionToTemp();

                // Redirect based on role
                String fxml = switch (role.toLowerCase()) {
                    case "customer" -> "/view/customer/CustomerDashboard.fxml";
                    case "seller"   -> "/view/seller/SellerCreateRestaurant.fxml";
                    case "courier" -> "/view/delivery/DeliveryDashboard.fxml";
                    default -> throw new IllegalArgumentException("Unknown role: " + role);
                };

                Parent root = FXMLLoader.load(getClass().getResource(fxml));
                Stage stage = (Stage) SignUpButton.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));

            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("Registration failed. Try again.");
                errorLabel.setVisible(true);
            }
        });
    }

}
