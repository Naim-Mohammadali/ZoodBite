package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import network.dto.admin.CreateCouponRequestDto;
import network.dto.user.LoginRequestDto;
import network.endpoint.AdminEndpoint;
import network.endpoint.UserEndpoint;
import util.SessionManager;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CreateCouponController implements Initializable {

    @FXML private TextField codeField;
    @FXML private TextField valueField;
    @FXML private TextField minPriceField;
    @FXML private TextField userCountField;
    @FXML private DatePicker validFromField;
    @FXML private DatePicker validUntilField;
    @FXML private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createButton.setOnAction(e -> handleCreateCoupon());
    }

    private void handleCreateCoupon() {
        try {
            // Ensure admin session
            if (SessionManager.getInstance().getLoggedInUser() == null ||
                    !"ADMIN".equalsIgnoreCase(SessionManager.getInstance().getLoggedInUser().role)) {

                var loginResponse = new UserEndpoint().login(new LoginRequestDto("admin", "admin"));
                SessionManager.getInstance().setToken(loginResponse.token);
                SessionManager.getInstance().setLoggedInUser(loginResponse.user);
            }

            // Build request DTO
            CreateCouponRequestDto coupon = new CreateCouponRequestDto();
            coupon.code = codeField.getText().trim();
            coupon.value = Integer.parseInt(valueField.getText().trim());
            coupon.min_price = Integer.parseInt(minPriceField.getText().trim());
            coupon.user_count = Integer.parseInt(userCountField.getText().trim());
            coupon.validFrom = validFromField.getValue().format(DateTimeFormatter.ISO_DATE);
            coupon.validUntil = validUntilField.getValue().format(DateTimeFormatter.ISO_DATE);

            // Send request
            new AdminEndpoint().createCoupon(coupon);

            // Show success
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Coupon Created");
            alert.setHeaderText(null);
            alert.setContentText("✅ Coupon '" + coupon.code + "' created successfully!");
            alert.showAndWait();

            // Clear fields
            codeField.clear();
            valueField.clear();
            minPriceField.clear();
            userCountField.clear();
            validFromField.setValue(null);
            validUntilField.setValue(null);

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("⚠️ Failed to create coupon. Please try again.");
            alert.showAndWait();
        }
    }
}
