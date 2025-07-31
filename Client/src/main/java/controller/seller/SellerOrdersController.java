package controller.seller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.SellerEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class SellerOrdersController implements Initializable {

    @FXML private Button refreshButton;
    @FXML private Button approveButton;
    @FXML private VBox ordersContainer;

    // Track selected orders
    private final List<Long> selectedOrders = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrders();
        refreshButton.setOnAction(e -> loadOrders());
        approveButton.setOnAction(e -> approveSelectedOrders());

    }

    private void loadOrders() {
        ordersContainer.getChildren().clear();
        selectedOrders.clear();
        approveButton.setDisable(false);

        try {
            RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
            if (restaurant == null) {
                showAlert("No Restaurant", "You don't have a restaurant yet.");
                return;
            }

            SellerEndpoint endpoint = new SellerEndpoint();
            List<PlaceOrderResponseDto> orders = endpoint.getOrders(restaurant.getId(), null);

            // Sort: PLACED orders first
            orders = orders.stream()
                    .sorted(Comparator.comparing(o -> !"PLACED".equalsIgnoreCase(o.status)))
                    .collect(Collectors.toList());

            boolean hasPlaced = false;
            for (PlaceOrderResponseDto order : orders) {
                HBox card = createOrderCard(order);
                ordersContainer.getChildren().add(card);
                if ("PLACED".equalsIgnoreCase(order.status)) {
                    hasPlaced = true;
                }
            }
            approveButton.setDisable(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load orders.");
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
                        " | Coupon: " + (order.couponCode != null ? order.couponCode : "None") +
                        " | Address: " + order.address +
                        " | Created At: " + formatDate(order.createdAt)
        );
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1C1C1C;");

        card.getChildren().add(infoLabel);

        // Only show checkbox for PLACED orders
        if ("PLACED".equalsIgnoreCase(order.status)) {
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("custom-check-box");
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    selectedOrders.add(order.id);
                } else {
                    selectedOrders.remove(order.id);
                }
            });
            card.getChildren().add(checkBox);
        }

        return card;
    }

    private void approveSelectedOrders() {
        if (selectedOrders.isEmpty()) {
            showAlert("No Selection", "Please select at least one order to approve.");
            return;
        }

        try {
            SellerEndpoint endpoint = new SellerEndpoint();
            for (Long orderId : selectedOrders) {
                endpoint.updateOrderStatus(orderId, "ACCEPTED");
            }
            showAlert("Success", selectedOrders.size() + " order(s) approved.");
            loadOrders(); // reload after approval
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to approve selected orders.");
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
