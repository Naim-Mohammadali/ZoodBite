package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private ImageView logoImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the logo image
        logoImage.setImage(new Image(getClass().getResourceAsStream("/assets/logo/mainLogo.png")));

        // Wait 2 seconds then load login
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 2 seconds
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                try {
                    URL loginFXML = getClass().getResource("/view/Login.fxml");
                    System.out.println("Login.fxml URL: " + loginFXML);

                    Parent root = FXMLLoader.load(loginFXML);
                    Stage stage = (Stage) logoImage.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600)); // ✅ Match main window size
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }
}
