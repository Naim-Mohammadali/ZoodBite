package controller.courier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import network.dto.user.UserDto;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class CourierDashboardController implements Initializable {

    public ToggleButton profileButton;
    public ToggleButton getordersButton;
    public ToggleButton myordersButton;
    @FXML private ImageView logoImage;
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private StackPane mainContent;

    private final ToggleGroup sidebarGroup = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load logo
        logoImage.setImage(new Image(getClass().getResourceAsStream("/assets/logo/mainLogo.png")));

        UserDto user = SessionManager.getInstance().getLoggedInUser();
        welcomeLabel.setText("Welcome, " + user.name + "!");

        // Add sidebar ToggleButtons to a group
        profileButton.setToggleGroup(sidebarGroup);
        myordersButton.setToggleGroup(sidebarGroup);
        getordersButton.setToggleGroup(sidebarGroup);

        boolean isActive = "ACTIVE".equalsIgnoreCase(user.status);

        if (!isActive) {
            // Disable delivery buttons
            myordersButton.setDisable(true);
            getordersButton.setDisable(true);

            // Load warning message instead of a page
            showPendingActivationMessage();

            // Profile is still clickable
            profileButton.setOnAction(e -> loadPage("/view/Profile.fxml"));

        } else {
            // Default: show My Deliveries
            loadPage("/view/delivery/DeliveryCurrent.fxml");
            myordersButton.setSelected(true);

            // Sidebar navigation
            profileButton.setOnAction(e -> loadPage("/view/Profile.fxml"));
            getordersButton.setOnAction(e -> loadPage("/view/delivery/DeliveryAvailable.fxml"));
            myordersButton.setOnAction(e -> loadPage("/view/delivery/DeliveryCurrent.fxml"));
        }

        // Log Out logic
        logoutButton.setOnAction(e -> {
            SessionManager.getInstance().setToken(null);
            SessionManager.getInstance().setLoggedInUser(null);
            SessionManager.getInstance().deleteTempFile();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /** Show a warning message in the dashboard content area */
    private void showPendingActivationMessage() {
        mainContent.getChildren().clear();
        Label msg = new Label("⚠ You are not activated.\nPending admin approval.");
        msg.setStyle("-fx-font-size: 18px; -fx-text-fill: red; -fx-alignment: center;");
        mainContent.getChildren().add(msg);
    }

    /** Load an FXML page into the main content area */
    private void loadPage(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
