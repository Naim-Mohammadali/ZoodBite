package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import network.dto.order.PlaceOrderResponseDto;
import network.endpoint.AdminEndpoint;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOrdersController implements Initializable {

    @FXML private VBox ordersContainer;
    @FXML private Button refreshButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrders();

        refreshButton.setOnAction(e -> {
            ordersContainer.getChildren().clear();
            loadOrders();
        });
    }

    private void loadOrders() {
        try {
            AdminEndpoint admin = new AdminEndpoint();
            List<PlaceOrderResponseDto> allOrders = admin.getAllOrders();

            if (allOrders.isEmpty()) {
                Label noOrders = new Label("No orders found.");
                noOrders.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
                ordersContainer.getChildren().add(noOrders);
                return;
            }

            for (PlaceOrderResponseDto order : allOrders) {
                VBox card = new VBox(5);
                card.getStyleClass().add("order-card");

                Label id = new Label("Order ID: " + order.id);
                Label restaurant = new Label("Restaurant: " + order.restaurantName);
                Label total = new Label("Total: " + order.total);
                Label status = new Label("Status: " + order.status);
                Label address = new Label("Address: " + order.address);
                Label items = new Label("Items: " + order.itemIds);

                id.getStyleClass().add("order-label");
                restaurant.getStyleClass().add("order-label");
                total.getStyleClass().add("order-label");
                status.getStyleClass().add("order-label");
                address.getStyleClass().add("order-label");
                items.getStyleClass().add("order-label");

                card.getChildren().addAll(id, restaurant, total, status, address, items);
                ordersContainer.getChildren().add(card);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Label error = new Label("⚠️ Failed to load orders.");
            error.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            ordersContainer.getChildren().add(error);
        }
    }
}
