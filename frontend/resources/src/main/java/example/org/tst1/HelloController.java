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
    private static User tempUser;
    @FXML
    private PasswordField passwordLoginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label welcomeText;
    @FXML
    private Label statusLabel; // to show error messages
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField oldPasswordField;

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
    private ImageView profileImageView;

    @FXML
    private Label bankIdLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label namelabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label usernameLabel;
    @FXML
    private TextField bankIdTextField;

    @FXML
    private Hyperlink editProfilePictureHyperlink;



    private User user;

    @FXML
    void handleProfileImageClicked(MouseEvent event) throws IOException {
        if (tempUser == null) {
            System.err.println("Error: No user is logged in.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("profilePage.fxml"));
        Parent root = loader.load();

        // Get the new HelloController created for profilePage.fxml
        HelloController profileController = loader.getController();

        // Pass the tempUser to the new controller
        profileController.setUserData(tempUser);  // <- you define this method below

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goTOConfigurationPage(ActionEvent event) throws IOException {
        if (tempUser == null) {
            System.err.println("Error: No user is logged in.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("configurationProfilePage.fxml"));
        Parent root = loader.load();

        // Get the new HelloController created for profilePage.fxml
        HelloController profileEditController = loader.getController();

        // Pass the tempUser to the new controller
        profileEditController.setUserDataTextField(tempUser);  // <- you define this method below

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void informationEdited(ActionEvent event) throws IOException {
        // Get all field values
        String enteredUser = usernameTextField.getText().trim();
        String enteredPass = passwordField.getText().trim();
        String enteredName = nameTextField.getText().trim();
        String enteredLastName = lastNameTextField.getText().trim();
        String enteredPhoneNumber = phoneNumberTextField.getText().trim();
        String enteredEmail = emailTextField.getText().trim();
        String oldPass = oldPasswordField.getText().trim();
        String enteredBankId = bankIdTextField.getText().trim();

        // Validate old password
        if (oldPass.isEmpty()) {
            showError("Please enter your old password");
            return;
        }

        if (!oldPass.equals(tempUser.getPassword())) {
            showError("❌ Incorrect old password");
            return;
        }

        // Validate new password if changed
        if (!enteredPass.isEmpty() && enteredPass.length() < 6) {
            showError("❌ Password must be at least 6 characters");
            return;
        }

        // Validate phone number format if changed
        if (!enteredPhoneNumber.isEmpty() && !enteredPhoneNumber.matches("\\d{11}")) {
            showError("❌ Phone number must be 11 digits");
            return;
        }

        // Validate email format if changed
        if (!enteredEmail.isEmpty() && !enteredEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("❌ Invalid email format");
            return;
        }

        // Update user information
        try {
            if (!enteredUser.isEmpty()) tempUser.setUsername(enteredUser);
            if (!enteredPass.isEmpty()) tempUser.setPassword(enteredPass);
            if (!enteredName.isEmpty()) tempUser.setName(enteredName);
            if (!enteredLastName.isEmpty()) tempUser.setLastName(enteredLastName);
            if (!enteredPhoneNumber.isEmpty()) tempUser.setPhoneNumber(enteredPhoneNumber);
            if (!enteredEmail.isEmpty()) tempUser.setEmail(enteredEmail);
            if (!enteredBankId.isEmpty()) tempUser.setBankId(enteredBankId);

            showSuccess("✓ Profile updated successfully");
            loadProfilePage(event);
        } catch (Exception e) {
            showError("❌ Error updating profile: " + e.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setVisible(true);
    }

    private void loadProfilePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profilePage.fxml"));
        Parent root = loader.load();
        HelloController profileController = loader.getController();
        profileController.setUserData(tempUser);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void handleHistoryImageClicked(MouseEvent event) {

    }

    @FXML
    void editProfilePicture(ActionEvent event) {

    }

    @FXML
    void handleHomeImageClicked(MouseEvent event) throws IOException {
        if (tempUser == null) {
            System.err.println("Error: tempUser is null");
            return;
        }
        namelabel.setText(tempUser.getName());
        lastNameLabel.setText(tempUser.getLastName());
        usernameLabel.setText(tempUser.getUsername());
        phoneNumberTextField.setText(tempUser.getPhoneNumber());
        if(!tempUser.getEmail().isEmpty()) {
            emailLabel.setText(tempUser.getEmail());
        }
        else {
            emailLabel.setText("No email");
        }
        if (!tempUser.getBankId().isEmpty()){
            bankIdLabel.setText(tempUser.getBankId());
        }
        else {
            bankIdLabel.setText("No bank ID");
        }

        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

        namelabel.setText(tempUser.getName());
        lastNameLabel.setText(tempUser.getLastName());
        usernameLabel.setText(tempUser.getUsername());
        phoneNumberTextField.setText(tempUser.getPhoneNumber());
        if(!tempUser.getEmail().isEmpty()) {
            emailLabel.setText(tempUser.getEmail());
        }
        else {
            emailLabel.setText("No email");
        }
        if (!tempUser.getBankId().isEmpty()){
            bankIdLabel.setText(tempUser.getBankId());
        }
        else {
            bankIdLabel.setText("No bank ID");
        }

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

    public void setUserData(User user) {
        tempUser = user;

        if (namelabel != null) namelabel.setText(user.getName());
        if (lastNameLabel != null) lastNameLabel.setText(user.getLastName());
        if (usernameLabel != null) usernameLabel.setText(user.getUsername());
        if (phoneNumberLabel != null) phoneNumberLabel.setText(user.getPhoneNumber());
        int passwordWordCount = user.getPassword().length();
        String pass = ".";
        for(int i= 1 ;i<passwordWordCount ; i++){
            pass =pass +".";
        }
        if (passwordLabel != null) passwordLabel.setText(pass);

        if (emailLabel != null) {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                emailLabel.setText(email);
            } else {
                emailLabel.setText("No email");
            }
        }

        if (bankIdLabel != null) {
            String bankId = user.getBankId();
            if (bankId != null && !bankId.isEmpty()) {
                bankIdLabel.setText(bankId);
            } else {
                bankIdLabel.setText("No bank ID");
            }
        }
    }
    public void setUserDataTextField(User user) {
        tempUser = user;

        if (namelabel != null) nameTextField.setPromptText(user.getName());
        if (lastNameLabel != null) lastNameTextField.setPromptText(user.getLastName());
        if (usernameLabel != null) usernameTextField.setPromptText(user.getUsername());
        if (phoneNumberLabel != null) phoneNumberTextField.setPromptText(user.getPhoneNumber());
        int passwordWordCount = user.getPassword().length();
        String pass = ".";
        for(int i= 1 ;i<passwordWordCount ; i++){
            pass =pass +".";
        }
        if (passwordLabel != null) passwordField.setPromptText(pass);

        if (emailLabel != null) {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                emailTextField.setPromptText(email);
            } else {
                emailTextField.setPromptText("No email");
            }
        }

        if (bankIdLabel != null) {
            String bankId = user.getBankId();
            if (bankId != null && !bankId.isEmpty()) {
                bankIdTextField.setPromptText(bankId);
            } else {
                bankIdTextField.setPromptText("No bank ID");
            }
        }
    }


    @FXML
    public void goToProfilePage(ActionEvent event) throws IOException {
        if (tempUser == null) {
            System.err.println("Error: No user is logged in.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("profilePage.fxml"));
        Parent root = loader.load();

        // Get the new HelloController created for profilePage.fxml
        HelloController profileController = loader.getController();

        // Pass the tempUser to the new controller
        profileController.setUserData(tempUser);  // <- you define this method below

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
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
        usersDatabase.add(new Buyer("mhmdMsr2005","Mohamad", "Masri", "09044013962","mohamadelmasri05@gmail.com", "123456"));
        if (user != null) {
            setUserData(tempUser);
        }

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
                        tempUser = user;
                        loadPage(event, "userPage.fxml");
                        tempUser = user;
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
            tempUser = new Buyer(enteredUser, enteredName, enteredLastName, enteredPhoneNumber, enteredPass);
            usersDatabase.add(tempUser);
        }
        else {
            tempUser = new Buyer(enteredUser, enteredName, enteredLastName, enteredPhoneNumber,enteredEmail ,enteredPass);
            usersDatabase.add(tempUser);

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