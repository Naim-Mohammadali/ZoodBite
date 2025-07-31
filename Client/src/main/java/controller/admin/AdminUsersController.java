package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.dto.user.UpdateUserStatusResponseDto;
import network.dto.user.UserDto;
import network.endpoint.AdminEndpoint;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUsersController implements Initializable {

    @FXML private ToggleButton allUsersButton;
    @FXML private ToggleButton pendingUsersButton;
    @FXML private VBox usersContainer;
    @FXML private Button approveButton;

    private final List<UserDto> selectedUsers = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Make toggle buttons mutually exclusive
        ToggleGroup toggleGroup = new ToggleGroup();
        allUsersButton.setToggleGroup(toggleGroup);
        pendingUsersButton.setToggleGroup(toggleGroup);

        // Default to pending users
        pendingUsersButton.setSelected(true);
        loadPendingUsers();

        allUsersButton.setOnAction(e -> {
            if (allUsersButton.isSelected()) loadAllUsers();
        });
        pendingUsersButton.setOnAction(e -> {
            if (pendingUsersButton.isSelected()) loadPendingUsers();
        });

        approveButton.setOnAction(e -> approveSelectedUsers());
    }

    private void loadAllUsers() {
        approveButton.setVisible(false);
        usersContainer.getChildren().clear();
        try {
            AdminEndpoint admin = new AdminEndpoint();
            List<UserDto> allUsers = admin.getAllUsers().users;

            if (allUsers.isEmpty()) {
                Label noUsers = new Label("No users found.");
                noUsers.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                usersContainer.getChildren().add(noUsers);
                return;
            }

            for (UserDto user : allUsers) {
                HBox card = createUserCard(user, false);
                usersContainer.getChildren().add(card);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Label error = new Label("⚠️ Failed to load users.");
            error.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            usersContainer.getChildren().add(error);
        }
    }

    private void loadPendingUsers() {
        approveButton.setVisible(true);
        selectedUsers.clear();
        updateApproveButtonState();
        usersContainer.getChildren().clear();

        try {
            AdminEndpoint admin = new AdminEndpoint();
            List<UserDto> pendingUsers = admin.getUsersByStatus("PENDING");

            if (pendingUsers.isEmpty()) {
                Label noUsers = new Label("No pending users.");
                noUsers.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                usersContainer.getChildren().add(noUsers);
                return;
            }

            for (UserDto user : pendingUsers) {
                HBox card = createUserCard(user, true);
                usersContainer.getChildren().add(card);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Label error = new Label("⚠️ Failed to load pending users.");
            error.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            usersContainer.getChildren().add(error);
        }
    }

    private HBox createUserCard(UserDto user, boolean selectable) {
        HBox card = new HBox();
        card.getStyleClass().add("user-card");
        card.setSpacing(10);
        card.setFillHeight(true);

        if ("ACTIVE".equalsIgnoreCase(user.status)) {
            card.getStyleClass().add("user-card-active");
        }

        // Info box: 80% width
        Label info = new Label(user.id + " | " + user.phone + " | " + user.role + " | " +
                (user.accountNumber != null ? user.accountNumber : "-"));
        info.getStyleClass().add("user-info");

        HBox infoBox = new HBox(info);
        HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);
        infoBox.setMaxWidth(Double.MAX_VALUE);

        if (selectable) {
            // Checkbox box: 20% width
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("custom-check-box");

            checkBox.setOnAction(ev -> {
                if (checkBox.isSelected()) selectedUsers.add(user);
                else selectedUsers.remove(user);
                updateApproveButtonState();
            });

            HBox checkBoxBox = new HBox(checkBox);
            checkBoxBox.setMinWidth(0);
            checkBoxBox.setPrefWidth(100); // adjust proportion here
            checkBoxBox.setMaxWidth(Double.MAX_VALUE);
            checkBoxBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

            card.getChildren().addAll(infoBox, checkBoxBox);
        } else {
            card.getChildren().add(infoBox);
        }

        return card;
    }



    private void approveSelectedUsers() {
        try {
            AdminEndpoint admin = new AdminEndpoint();

            for (UserDto user : new ArrayList<>(selectedUsers)) {
                Long userId = user.id;
                UpdateUserStatusResponseDto response = admin.approveUser(userId);
                System.out.println("✅ Approved User ID: " + userId + " | New Status: " + response.new_status);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Approved " + selectedUsers.size() + " users.");
            alert.showAndWait();

            // clear selection + refresh list
            selectedUsers.clear();
            loadPendingUsers();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to approve selected users.");
            alert.showAndWait();
        }
    }

    private void updateApproveButtonState() {
        int count = selectedUsers.size();
        approveButton.setDisable(count == 0);
        if (count == 0) {
            approveButton.setText("Approve");
        } else {
            approveButton.setText("Approve (" + count + ")");
        }
    }

}
