package example.org.tst1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final ArrayList<User> usersDatabase = new ArrayList<>();
    private static final ArrayList<Admin> adminDatabase = new ArrayList<>();

    private static WorkerDraft tempWorker;

    @FXML
    private PasswordField passwordLoginField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label welcomeText;
    @FXML
    private Label statusLabel; // to show error messages
    @FXML
    private TextField emailTextField;

    @FXML
    private TextField emailVerifictionCodeTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordSignupTextField;

    @FXML
    private PasswordField passwordVerificationTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField phoneNumberVerifictionCodeTextField;

    @FXML
    private TextField usernameSIgnupTextField;
    @FXML
    private TextArea adressTextField;

    @FXML
    private RadioButton courierRadioButton;

    @FXML
    private RadioButton restaurantRadioButton;

    @FXML
    private ImageView historyPageImageView;

    @FXML
    private ImageView homePageImageView;

    @FXML
    void goTOConfigurationPage(ActionEvent event) {

    }

    @FXML
    void handleHistoryImageClicked(MouseEvent event) {

    }

    @FXML
    void handleHomeImageClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logout(ActionEvent event) {

    }



    public void goTOHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signOrLoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goTOLoginPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goTOSignupPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signupPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToProfilePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goTOChoosingWorkPage(ActionEvent event) throws IOException {
        String enteredUser = usernameSIgnupTextField.getText().trim();
        String enteredPass = passwordSignupTextField.getText().trim();
        String enteredName = nameTextField.getText().trim();
        String enteredLastName = lastNameTextField.getText().trim();
        String enteredPhoneNumber = phoneNumberTextField.getText().trim();
        String enteredPhoneVerificationCode = phoneNumberVerifictionCodeTextField.getText().trim();
        String enteredEmailVerificationCode = emailVerifictionCodeTextField.getText().trim();
        String enteredEmail = emailTextField.getText().trim();
        String enteredPasswordVerification = passwordVerificationTextField.getText().trim();

        if (enteredUser.isEmpty() || enteredPass.isEmpty() || enteredName.isEmpty() || enteredLastName.isEmpty() || enteredPhoneNumber.isEmpty() || enteredPhoneVerificationCode.isEmpty() || enteredPasswordVerification.isEmpty()) {
            statusLabel.setText("⚠️ Please fill in all information.");
            statusLabel.setStyle("-fx-text-fill: orange;");
            statusLabel.setVisible(true);
            return;
        }

        for (Admin admin : adminDatabase) {
            if (admin.getUsername().equals(enteredUser)) {
                statusLabel.setText("❌ this username is used please use an another username");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }

        for (User user : usersDatabase) {
            if (user.getUsername().equals(enteredUser)) {
                statusLabel.setText("❌ this username is used please use an another username");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }
        if (!enteredPass.equals(enteredPasswordVerification)) {
            statusLabel.setText("❌ make sure the password matches");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }

        if (!enteredPhoneNumber.matches("\\d{11}")) {
            statusLabel.setText("❌ make sure the phone number matches");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }
        if (!enteredEmailVerificationCode.isEmpty()) {
            if (!enteredEmailVerificationCode.equals("123456")) {
                statusLabel.setText("❌ the email verification code is wrong");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }

        if (!enteredPhoneVerificationCode.equals("123456")) {
            statusLabel.setText("❌ the phone number verification code is wrong");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }

        if(enteredEmail.isEmpty()) {
            tempWorker = new WorkerDraft(enteredUser,enteredPass,enteredName,enteredLastName,enteredPhoneNumber);
        }
        else {
            tempWorker = new WorkerDraft(enteredUser,enteredPass,enteredName,enteredLastName,enteredPhoneNumber,enteredEmail);
        }

        Parent root = FXMLLoader.load(getClass().getResource("choosingWorkPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goTOUserPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("userPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        adminDatabase.add(new Admin("add123min321", "123456"));
        usersDatabase.add(new Buyer("buyer","o", "o", "123456", "123456"));
        usersDatabase.add(new Courier("courier", "o","o", "123456", "123456", "o"));
        usersDatabase.add(new Restaurant("restaurant", "o","o", "123456", "123456", "o"));
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String enteredUser = usernameTextField.getText().trim();
        String enteredPass = passwordLoginField.getText().trim();

        if (enteredUser.isEmpty() || enteredPass.isEmpty()) {
            statusLabel.setText("⚠️ Please enter both username and password.");
            statusLabel.setStyle("-fx-text-fill: orange;");
            statusLabel.setVisible(true);
            return;
        }

        // Admin login check
        for (Admin admin : adminDatabase) {
            if (admin.getUsername().equals(enteredUser)) {
                if (admin.getPassword().equals(enteredPass)) {
                    loadPage(event, "adminPage.fxml");
                    return;
                } else {
                    statusLabel.setText("❌ Incorrect password.");
                    statusLabel.setVisible(true);
                    break;
                }
            }
        }

        // User login check
        for (User user : usersDatabase) {
            if (user.getUsername().equals(enteredUser)) {
                if (user.getPassword().equals(enteredPass)) {
                    if (user instanceof Buyer) {
                        loadPage(event, "userPage.fxml");
                    } else if (user instanceof Courier) {
                        loadPage(event, "courierPage.fxml");
                    } else if (user instanceof Restaurant) {
                        loadPage(event, "restaurantPage.fxml");
                    }
                    return;
                } else {
                    statusLabel.setText("❌ Incorrect password.");
                    statusLabel.setVisible(true);
                    break;
                }
            }
        }

        // If no match was found
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
        statusLabel.setText("not found.");

        if (statusLabel.getText().isEmpty()) {
            statusLabel.setText("❌ Username not found.");
        }
    }

    @FXML
    private void signupUser(ActionEvent event) {
        String enteredUser = usernameSIgnupTextField.getText().trim();
        String enteredPass = passwordSignupTextField.getText().trim();
        String enteredName = nameTextField.getText().trim();
        String enteredLastName = lastNameTextField.getText().trim();
        String enteredPhoneNumber = phoneNumberTextField.getText().trim();
        String enteredPhoneVerificationCode = phoneNumberVerifictionCodeTextField.getText().trim();
        String enteredEmailVerificationCode = emailVerifictionCodeTextField.getText().trim();
        String enteredEmail = emailTextField.getText().trim();
        String enteredPasswordVerification = passwordVerificationTextField.getText().trim();

        if (enteredUser.isEmpty() || enteredPass.isEmpty() || enteredName.isEmpty() || enteredLastName.isEmpty() || enteredPhoneNumber.isEmpty() || enteredPhoneVerificationCode.isEmpty() || enteredPasswordVerification.isEmpty()) {
            statusLabel.setText("⚠️ Please fill in all information.");
            statusLabel.setStyle("-fx-text-fill: orange;");
            statusLabel.setVisible(true);
            return;
        }

        for (Admin admin : adminDatabase) {
            if (admin.getUsername().equals(enteredUser)) {
                statusLabel.setText("❌ this username is used please use an another username");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }

        for (User user : usersDatabase) {
            if (user.getUsername().equals(enteredUser)) {
                statusLabel.setText("❌ this username is used please use an another username");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }
        if (!enteredPass.equals(enteredPasswordVerification)) {
            statusLabel.setText("❌ make sure the password matches");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }

        if (!enteredPhoneNumber.matches("\\d{11}")) {
            statusLabel.setText("❌ make sure the phone number matches");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }
        if (!enteredEmailVerificationCode.isEmpty()) {
            if (!enteredEmailVerificationCode.equals("123456")) {
                statusLabel.setText("❌ the email verification code is wrong");
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setVisible(true);
                return;
            }
        }

        if (!enteredPhoneVerificationCode.equals("123456")) {
            statusLabel.setText("❌ the phone number verification code is wrong");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }
        if(enteredEmail.isEmpty()) {
            usersDatabase.add(new Buyer(enteredUser, enteredName, enteredLastName, enteredPhoneNumber, enteredPass));
        }
        else {
            usersDatabase.add(new Buyer(enteredUser, enteredName, enteredLastName, enteredPhoneNumber,enteredEmail ,enteredPass));

        }
        loadPage(event, "userPage.fxml");
    }

    // Utility method to reduce repetition
    private void loadPage(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("❌ Failed to load " + fxmlFile);
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
        }
    }

    @FXML
    private void goFromChoosingWorkTOUserPage(ActionEvent event) {
        String address = adressTextField.getText();
        if (!courierRadioButton.isSelected() && !restaurantRadioButton.isSelected()) {
            statusLabel.setText("⚠️ Please select you job");
            statusLabel.setStyle("-fx-text-fill: orange;");
            statusLabel.setVisible(true);
            return;
        }

        if (address.isEmpty()) {
            statusLabel.setText("⚠️ Please enter your address.");
            statusLabel.setStyle("-fx-text-fill: orange;");
            statusLabel.setVisible(true);
            return;
        }

        if (restaurantRadioButton.isSelected()) {
            if (tempWorker.getEmail() == null || tempWorker.getEmail().isEmpty()) {
                usersDatabase.add(new Restaurant(tempWorker.getUsername(), tempWorker.getName(), tempWorker.getLastName(), tempWorker.getPhoneNumber(), tempWorker.getPassword(), address));
            } else {
                usersDatabase.add(new Restaurant(tempWorker.getUsername(), tempWorker.getName(), tempWorker.getLastName(), tempWorker.getPhoneNumber(), tempWorker.getEmail(), tempWorker.getPassword(), address));
            }
            System.out.println("dn");
            loadPage(event, "restaurantPage.fxml");

        } else if (courierRadioButton.isSelected()){
            if (tempWorker.getEmail() == null || tempWorker.getEmail().isEmpty()) {
                usersDatabase.add(new Courier(tempWorker.getUsername(), tempWorker.getName(), tempWorker.getLastName(), tempWorker.getPhoneNumber(), tempWorker.getPassword(), address));

            } else {
                usersDatabase.add(new Courier(tempWorker.getUsername(), tempWorker.getName(), tempWorker.getLastName(), tempWorker.getPhoneNumber(), tempWorker.getEmail(), tempWorker.getPassword(), address));
            }
            System.out.println("dn");
            loadPage(event, "courierPage.fxml");
        }
    }


}