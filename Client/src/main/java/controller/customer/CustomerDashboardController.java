package controller.customer;

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

public class CustomerDashboardController implements Initializable {

    public ToggleButton profileButton;
    public ToggleButton historyButton;
    public ToggleButton cartButton;
    public ToggleButton PlaceOrderButton;
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

        // Add sidebar ToggleButtons to a group so only one is active
        profileButton.setToggleGroup(sidebarGroup);
        PlaceOrderButton.setToggleGroup(sidebarGroup);
        historyButton.setToggleGroup(sidebarGroup);
        cartButton.setToggleGroup(sidebarGroup);

        // Load default page (Users)
        loadPage("/view/customer/CustomerPlaceOrder.fxml");
        PlaceOrderButton.setSelected(true);

        // Sidebar navigation
        profileButton.setOnAction(e -> loadPage("/view/Profile.fxml"));
        historyButton.setOnAction(e -> loadPage("/view/customer/CustomerHistory.fxml"));
        cartButton.setOnAction(e -> loadPage("/view/customer/CustomerCart.fxml"));
        PlaceOrderButton.setOnAction(e -> loadPage("/view/customer/CustomerPlaceOrder.fxml"));

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
