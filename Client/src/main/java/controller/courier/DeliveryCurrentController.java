package controller.courier;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.dto.order.PlaceOrderResponseDto;
import network.endpoint.CourierEndpoint;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryCurrentController implements Initializable {

    @FXML private Button refreshButton;
    @FXML private Button deliverButton;
    @FXML private VBox ordersContainer;

    private final List<Long> selectedOrders = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMyDeliveries();
        refreshButton.setOnAction(e -> loadMyDeliveries());
        deliverButton.setOnAction(e -> deliverSelectedOrders());
    }
    private void loadMyDeliveries() {
        ordersContainer.getChildren().clear();
        selectedOrders.clear();
        deliverButton.setDisable(true);

        try {
            CourierEndpoint courier = new CourierEndpoint();
            List<PlaceOrderResponseDto> deliveries = courier.getMyDeliveries(null);

            if (deliveries == null || deliveries.isEmpty()) {
                Label noOrders = new Label("No active deliveries.");
                noOrders.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                ordersContainer.getChildren().add(noOrders);
                return;
            }

            // Place PLACED/IN_TRANSIT orders on top
            deliveries = deliveries.stream()
                    .sorted(Comparator.comparing(o -> !"IN_TRANSIT".equalsIgnoreCase(o.status)))
                    .collect(Collectors.toList());

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

        Label infoLabel = new Label(
                "ID: " + order.id +
                        " | Total: $" + order.total +
                        " | Status: " + order.status +
                        " | Address: " + order.address +
                        " | Created At: " + formatDate(order.createdAt)
        );
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1C1C1C;");
        card.getChildren().add(infoLabel);

        // Only allow selection if status is IN_TRANSIT
        if ("IN_TRANSIT".equalsIgnoreCase(order.status)) {
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("custom-check-box");
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    selectedOrders.add(order.id);
                } else {
                    selectedOrders.remove(order.id);
                }
                deliverButton.setDisable(selectedOrders.isEmpty());
            });
            card.getChildren().add(checkBox);
        }

        return card;
    }

    private void deliverSelectedOrders() {
        if (selectedOrders.isEmpty()) {
            showAlert("No Selection", "Please select at least one order to deliver.");
            return;
        }

        try {
            CourierEndpoint courier = new CourierEndpoint();
            for (Long orderId : selectedOrders) {
                courier.deliverOrder(orderId);
            }
            showAlert("Success", selectedOrders.size() + " order(s) marked as delivered.");
            loadMyDeliveries();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to deliver selected orders.");
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
