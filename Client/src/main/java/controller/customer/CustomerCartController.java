package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.dto.order.PlaceOrderItemDto;
import network.dto.order.PlaceOrderRequestDto;
import network.dto.order.PlaceOrderResponseDto;
import network.dto.restaurant.MenuItemResponseDto;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.CustomerEndpoint;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerCartController implements Initializable {

    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    @FXML private CheckBox couponCheckBox;
    @FXML private TextField couponField;
    @FXML private Button checkoutButton;

    private RestaurantResponseDto currentRestaurant;
    private Map<Long, Integer> cart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cart = SessionManager.getInstance().getCurrentCart();

        try {
            currentRestaurant = new RestaurantEndpoint()
                    .getRestaurantById(SessionManager.getInstance().getCurrentRestaurant().getId());

            if (cart == null || cart.isEmpty()) {
                cartContainer.getChildren().add(new Label("Your cart is empty."));
                checkoutButton.setDisable(true);
                return;
            }

            // Build item cards
            double subtotal = 0;
            for (Long itemId : cart.keySet()) {
                MenuItemResponseDto item = findItemInRestaurant(currentRestaurant, itemId);
                int qty = cart.get(itemId);

                if (item != null) {
                    subtotal += item.price * qty;
                    HBox card = new HBox(15);
                    card.setStyle("-fx-background-color: #F4F4F4; -fx-padding: 10; -fx-background-radius: 8;");
                    card.setAlignment(Pos.CENTER_LEFT);
                    Label info = new Label(item.name + " | Qty: " + qty +
                            " | $" + (item.price * qty));
                    info.setStyle("-fx-font-size: 16px; -fx-text-fill: #1C1C1C;");
                    card.getChildren().add(info);
                    cartContainer.getChildren().add(card);
                }
            }

            double total = subtotal
                    + (currentRestaurant.getTaxFee() != null ? currentRestaurant.getTaxFee() : 0)
                    + (currentRestaurant.getAdditionalFee() != null ? currentRestaurant.getAdditionalFee() : 0);

            totalLabel.setText("Total: $" + total);

        } catch (Exception ex) {
            ex.printStackTrace();
            cartContainer.getChildren().add(new Label("⚠ Failed to load cart items."));
        }

        // Show/hide coupon field
        couponField.setVisible(false);
        couponCheckBox.setOnAction(e -> couponField.setVisible(couponCheckBox.isSelected()));

        checkoutButton.setOnAction(e -> doCheckout());
    }

    private void doCheckout() {
        try {
            String couponCode = couponCheckBox.isSelected() ? couponField.getText().trim() : null;

            List<PlaceOrderItemDto> items = cart.entrySet().stream()
                    .map(e -> new PlaceOrderItemDto(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            PlaceOrderRequestDto request = new PlaceOrderRequestDto(
                    currentRestaurant.getId(),
                    "Tehran, Valiasr Street, No. 10", // TODO: make dynamic later
                    items,
                    couponCode
            );

            CustomerEndpoint ce = new CustomerEndpoint();
            PlaceOrderResponseDto response = ce.placeOrder(request);

            // Save the order in session for history
            SessionManager.getInstance().addOrderToHistory(response);

            showAlert("Success", "Order placed! ID: " + response.id);

            // Clear cart after checkout
            SessionManager.getInstance().clearCart();
            checkoutButton.setDisable(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to place order.");
        }
    }

    /** Search for an item in menus + unassigned items */
    private MenuItemResponseDto findItemInRestaurant(RestaurantResponseDto restaurant, Long itemId) {
        try {
            RestaurantEndpoint endpoint = new RestaurantEndpoint();

            // Menus
            if (restaurant.getMenus() != null) {
                for (RestaurantResponseDto.MenuDto menu : restaurant.getMenus()) {
                    List<MenuItemResponseDto> items = endpoint.getMenuItems(restaurant.getId(), menu.getTitle());
                    for (MenuItemResponseDto i : items) {
                        if (i.id == (itemId)) return i;
                    }
                }
            }

            // Unassigned
            List<MenuItemResponseDto> unassigned = endpoint.getUnassignedItems(restaurant.getId());
            for (MenuItemResponseDto i : unassigned) {
                if (i.id == (itemId)) return i;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
