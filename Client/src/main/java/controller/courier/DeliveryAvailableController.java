package controller.courier;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import network.dto.order.PlaceOrderResponseDto;
import network.endpoint.CourierEndpoint;

import java.net.URL;
import java.util.*;

public class DeliveryAvailableController implements Initializable {

    @FXML private Button refreshButton;
    @FXML private Button approveButton;
    @FXML private VBox ordersContainer;

    // Track selected orders
    private final Map<CheckBox, PlaceOrderResponseDto> selectedOrders = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrders();


        refreshButton.setOnAction(e -> loadOrders());
        approveButton.setOnAction(e -> {
            pickSelectedOrders();
            loadOrders();});

        approveButton.setDisable(true); // Initially disabled
    }

    private void loadOrders() {
        ordersContainer.getChildren().clear();
        selectedOrders.clear();
        approveButton.setDisable(true);

        try {
            CourierEndpoint courier = new CourierEndpoint();
            List<PlaceOrderResponseDto> deliveries = courier.getAvailableDeliveries();

            if (deliveries == null || deliveries.isEmpty()) {
                Label noOrders = new Label("No deliveries available.");
                noOrders.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                ordersContainer.getChildren().add(noOrders);
                return;
            }

            for (PlaceOrderResponseDto order : deliveries) {
                HBox card = createOrderCard(order);
                ordersContainer.getChildren().add(card);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load deliveries.");
        }
    }

    private HBox createOrderCard(PlaceOrderResponseDto order) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-padding: 12;");
        card.setAlignment(Pos.CENTER_LEFT);

        String infoText = String.format(
                "ID: %d | Total: $%.2f | Status: %s | Address: %s | Created At: %s",
                order.id,
                order.total,
                order.status,
                order.address,
                formatDate(order.createdAt)
        );
        Label infoLabel = new Label(infoText);
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1C1C1C;");
        HBox.setHgrow(infoLabel, Priority.ALWAYS);

        card.getChildren().add(infoLabel);

        // Add a checkbox for PLACED or READY_FOR_PICKUP deliveries
        if ("PLACED".equalsIgnoreCase(order.status) || "READY_FOR_PICKUP".equalsIgnoreCase(order.status)) {
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("custom-check-box");

            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    selectedOrders.put(checkBox, order);
                } else {
                    selectedOrders.remove(checkBox);
                }
                approveButton.setDisable(selectedOrders.isEmpty());
            });

            card.getChildren().add(checkBox);
        }

        return card;
    }

    private void pickSelectedOrders() {
        if (selectedOrders.isEmpty()) {
            showAlert("No Selection", "Please select at least one delivery to pick up.");
            return;
        }

        try {
            CourierEndpoint courier = new CourierEndpoint();
            for (PlaceOrderResponseDto order : selectedOrders.values()) {
                courier.pickOrder(order.id);
            }

            showAlert("Success", "Picked up " + selectedOrders.size() + " deliveries.");
            loadOrders(); // Refresh after picking up

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to pick up deliveries.");
        }
    }

    private String formatDate(List<Integer> createdAt) {
        if (createdAt == null || createdAt.size() < 6) return "Unknown date";
        return String.format("%d-%02d-%02d %02d:%02d",
                createdAt.get(0), createdAt.get(1), createdAt.get(2),
                createdAt.get(3), createdAt.get(4));
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
