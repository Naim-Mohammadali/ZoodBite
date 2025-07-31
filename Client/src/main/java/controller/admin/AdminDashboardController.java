package controller.admin;

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

public class AdminDashboardController implements Initializable {

    public ToggleButton profileButton;
    @FXML private ImageView logoImage;
    @FXML private Label welcomeLabel;

    @FXML private ToggleButton usersButton;
    @FXML private ToggleButton ordersButton;
    @FXML private ToggleButton couponButton;

    @FXML private Button logoutButton;
    @FXML private StackPane mainContent;

    private final ToggleGroup sidebarGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load logo
        logoImage.setImage(new Image(getClass().getResourceAsStream("/assets/logo/mainLogo.png")));

        UserDto user = SessionManager.getInstance().getLoggedInUser();
        welcomeLabel.setText("Welcome, " + user.name + "!");

        // Add sidebar ToggleButtons to a group so only one is active
        usersButton.setToggleGroup(sidebarGroup);
        ordersButton.setToggleGroup(sidebarGroup);
        couponButton.setToggleGroup(sidebarGroup);

        // Load default page (Users)
        loadPage("/view/admin/AdminUsers.fxml");
        usersButton.setSelected(true);

        // Sidebar navigation
        usersButton.setOnAction(e -> loadPage("/view/admin/AdminUsers.fxml"));
        ordersButton.setOnAction(e -> loadPage("/view/admin/AdminOrders.fxml"));
        couponButton.setOnAction(e -> loadPage("/view/admin/AdminCoupon.fxml"));

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
