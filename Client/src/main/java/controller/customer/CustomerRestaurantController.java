package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import network.dto.restaurant.MenuItemResponseDto;
import network.dto.user.RestaurantResponseDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomerRestaurantController implements Initializable {
    @FXML private Label restaurantInfoLabel;
    public Label backLabel;
    @FXML private Label restaurantNameLabel, addressLabel, phoneLabel, taxFeeLabel, additionalFeeLabel;
    @FXML private VBox menusContainer;
    @FXML private Button orderButton;
    private final Map<Long, Integer> cartItems = new HashMap<>();
    private RestaurantResponseDto currentRestaurant;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderButton.setOnAction(e -> {
            try {
                // Save cart and restaurant in SessionManager
                SessionManager.getInstance().setCurrentCart(new HashMap<>(cartItems));
                SessionManager.getInstance().setCurrentRestaurant(currentRestaurant);

                // Load Cart page into dashboard mainContent
                Parent page = FXMLLoader.load(getClass().getResource("/view/customer/CustomerCart.fxml"));
                BorderPane root = (BorderPane) orderButton.getScene().getRoot();
                StackPane mainContent = (StackPane) root.lookup("#mainContent");
                mainContent.getChildren().setAll(page);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to open Cart page.");
            }
        });


        backLabel.setOnMouseClicked(e -> goBackToPlaceOrder());
        Long restaurantId = customerSelectedRestaurantId(); // you can store it in SessionManager or pass it
        try {
            RestaurantEndpoint endpoint = new RestaurantEndpoint();
            currentRestaurant = endpoint.getRestaurantById(restaurantId);

            updateSummaryBox();
            loadUnassignedItems();
            loadMenus();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load restaurant.");
        }
    }
    private void goBackToPlaceOrder() {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/view/customer/CustomerPlaceOrder.fxml"));
            // Assuming you are inside the dashboard → replace the center content
            BorderPane root = (BorderPane) backLabel.getScene().getRoot();
            StackPane mainContent = (StackPane) root.lookup("#mainContent");
            mainContent.getChildren().setAll(page);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to go back.");
        }
    }
    private void updateSummaryBox() {
        restaurantNameLabel.setText(currentRestaurant.getName());
        restaurantInfoLabel.setText(
                currentRestaurant.getAddress() + " | " + currentRestaurant.getPhone()
        );
    }

    private void loadUnassignedItems() {
        try {
            List<MenuItemResponseDto> items = new RestaurantEndpoint()
                    .getUnassignedItems(currentRestaurant.getId());

            if (items == null || items.isEmpty()) {
                // Do not add any titled pane if no items
                return;
            }

            TitledPane unassignedPane = new TitledPane();
            unassignedPane.setText(""); // No label
            VBox itemsBox = new VBox(5);

            for (MenuItemResponseDto item : items) {
                HBox card = createItemCard(item);
                itemsBox.getChildren().add(card);
            }

            unassignedPane.setContent(itemsBox);
            unassignedPane.setExpanded(true);
            menusContainer.getChildren().add(0, unassignedPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadMenus() {
        if (currentRestaurant.getMenus() == null) return;
        for (RestaurantResponseDto.MenuDto menu : currentRestaurant.getMenus()) {
            TitledPane pane = new TitledPane();
            pane.setText(menu.getTitle());
            VBox box = new VBox(5);
            box.getChildren().add(new Label("Loading..."));
            pane.setContent(box);

            pane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
                if (isNowExpanded) {
                    try {
                        RestaurantEndpoint endpoint = new RestaurantEndpoint();
                        List<MenuItemResponseDto> items = endpoint.getMenuItems(currentRestaurant.getId(), menu.getTitle());
                        box.getChildren().clear();
                        for (MenuItemResponseDto item : items) {
                            box.getChildren().add(createItemCard(item));
                        }
                    } catch (Exception ex) {
                        box.getChildren().clear();
                        box.getChildren().add(new Label("⚠ Failed to load items."));
                    }
                }
            });

            pane.setExpanded(false);
            menusContainer.getChildren().add(pane);
        }
    }

    private HBox createItemCard(MenuItemResponseDto item) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #F4F4F4; -fx-padding: 12; -fx-background-radius: 12;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #391DAA;");

        Label priceLabel = new Label("Price: $" + item.price);
        priceLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1C1C1C;");
        Label quantityLabel = new Label("Available: " + item.quantity);
        quantityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1C1C1C;");

        VBox infoBox = new VBox(8, nameLabel, new HBox(20, priceLabel, quantityLabel));
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // ✅ Wider Add button
        Button addToCartBtn = new Button("Add");
        addToCartBtn.getStyleClass().add("update-button");
        addToCartBtn.setMinWidth(90); // fixed min width
        addToCartBtn.setPrefWidth(90);

        // ✅ Compact quantity controls
        HBox qtyControls = new HBox(4);
        qtyControls.setAlignment(Pos.CENTER_RIGHT);
        qtyControls.setVisible(false);

        Button plusBtn = new Button("+");
        plusBtn.setPrefWidth(25);

        TextField qtyField = new TextField("1");
        qtyField.setPrefWidth(28); // really compact
        qtyField.setMaxWidth(28);
        qtyField.setStyle("-fx-alignment: center;"); // center number text
        qtyField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                qtyField.setText(oldVal);
            } else if (!newVal.isEmpty()) {
                int val = Integer.parseInt(newVal);
                if (val < 1) qtyField.setText("1");
                else if (val > item.quantity) qtyField.setText(String.valueOf(item.quantity));
                else cartItems.put(item.id, val);
            }
        });

        Button cancelBtn = new Button("x");
        cancelBtn.setPrefWidth(25);

        plusBtn.setOnAction(e -> {
            int val = Integer.parseInt(qtyField.getText());
            if (val < item.quantity) {
                qtyField.setText(String.valueOf(val + 1));
            }
        });

        cancelBtn.setOnAction(e -> {
            cartItems.remove(item.id);
            qtyControls.setVisible(false);
            addToCartBtn.setVisible(true);
            updateOrderButtonVisibility();
        });

        qtyControls.getChildren().addAll(plusBtn, qtyField, cancelBtn);

        addToCartBtn.setOnAction(e -> {
            cartItems.put(item.id, 1);
            qtyControls.setVisible(true);
            addToCartBtn.setVisible(false);
            updateOrderButtonVisibility();
        });

        card.getChildren().addAll(infoBox, addToCartBtn, qtyControls);
        return card;
    }

    private void updateOrderButtonVisibility() {
        orderButton.setVisible(!cartItems.isEmpty());
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private Long customerSelectedRestaurantId() {
        return SessionManager.getInstance().getCurrentRestaurant().getId(); // Or whatever logic you use
    }
}
