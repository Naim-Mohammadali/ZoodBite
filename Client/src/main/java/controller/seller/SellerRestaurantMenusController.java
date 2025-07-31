package controller.seller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import network.dto.user.RestaurantResponseDto;
import network.dto.restaurant.MenuItemResponseDto;
import network.dto.restaurant.MenuCreateResponseDto;
import network.endpoint.RestaurantEndpoint;
import util.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SellerRestaurantMenusController implements Initializable {

    @FXML private Label restaurantNameLabel;
    @FXML private TextField menuNameField;
    @FXML private Button addMenuButton;
    @FXML private VBox menusContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();

        if (restaurant != null) {
            restaurantNameLabel.setText("Menus for " + restaurant.getName());

            // First: Load unassigned items
            addUnassignedItemsPane(restaurant);

            // Then: Load existing menus
            if (restaurant.getMenus() != null && !restaurant.getMenus().isEmpty()) {
                for (RestaurantResponseDto.MenuDto menu : restaurant.getMenus()) {
                    addMenuToUI(menu.getTitle());
                }
            }
        }

        // Add Menu Button
        addMenuButton.setOnAction(e -> {
            String menuName = menuNameField.getText().trim();
            if (menuName.isEmpty()) {
                showAlert("Error", "Menu name cannot be empty.");
                return;
            }

            try {
                RestaurantResponseDto current = SessionManager.getInstance().getCurrentRestaurant();
                if (current == null) {
                    showAlert("Error", "No restaurant found in session.");
                    return;
                }

                // Call backend to create menu
                RestaurantEndpoint endpoint = new RestaurantEndpoint();
                MenuCreateResponseDto response = endpoint.addMenuToRestaurant(current.getId(), menuName);

                // Update session
                RestaurantResponseDto.MenuDto newMenu = new RestaurantResponseDto.MenuDto();
                newMenu.setTitle(response.getCreated_menu());
                current.getMenus().add(newMenu);

                SessionManager.getInstance().setCurrentRestaurant(current);
                SessionManager.getInstance().writeSessionToTemp();

                // Add to UI
                addMenuToUI(newMenu.getTitle());
                menuNameField.clear();

                showAlert("Success", "Menu '" + newMenu.getTitle() + "' created successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to create menu.");
            }
        });
    }

    /** Creates a titled pane for unassigned items */
    private void addUnassignedItemsPane(RestaurantResponseDto restaurant) {
        TitledPane unassignedPane = new TitledPane();
        unassignedPane.setText("Unassigned Items");

        VBox itemsBox = new VBox(5);
        itemsBox.getChildren().add(new Label("Loading unassigned items..."));
        unassignedPane.setContent(itemsBox);

        // Load items when expanded
        unassignedPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                try {
                    RestaurantEndpoint endpoint = new RestaurantEndpoint();
                    List<MenuItemResponseDto> items = endpoint.getUnassignedItems(restaurant.getId());

                    itemsBox.getChildren().clear();
                    if (items == null || items.isEmpty()) {
                        itemsBox.getChildren().add(new Label("No unassigned items."));
                    } else {
                        for (MenuItemResponseDto item : items) {
                            HBox card = createMenuItemCard(item, "#391DAA", "#E89E1C", itemsBox);
                            itemsBox.getChildren().add(card);
                        }


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    itemsBox.getChildren().clear();
                    itemsBox.getChildren().add(new Label("⚠ Failed to load unassigned items."));
                }
            }
        });

        unassignedPane.setExpanded(false);
        menusContainer.getChildren().add(0, unassignedPane); // Insert at the top
    }


    private void addMenuToUI(String menuTitle) {
        TitledPane menuPane = new TitledPane();
        menuPane.setText(menuTitle);

        VBox itemsBox = new VBox(5);
        itemsBox.getChildren().add(new Label("Loading items..."));
        menuPane.setContent(itemsBox);

        // Load items on expand
        menuPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                try {
                    RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
                    if (restaurant == null) return;

                    RestaurantEndpoint endpoint = new RestaurantEndpoint();
                    List<MenuItemResponseDto> items = endpoint.getMenuItems(restaurant.getId(), menuTitle);

                    itemsBox.getChildren().clear();
                    if (items == null || items.isEmpty()) {
                        itemsBox.getChildren().add(new Label("No items in this menu."));
                    } else {
                        for (MenuItemResponseDto item : items) {
                            HBox card = createMenuItemCardForMenu(item);
                            itemsBox.getChildren().add(card);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    itemsBox.getChildren().clear();
                    itemsBox.getChildren().add(new Label("⚠ Failed to load items."));
                }
            }
        });

        menuPane.setExpanded(false);
        menusContainer.getChildren().add(menuPane);
    }
    private HBox createMenuItemCard(MenuItemResponseDto item, String nameColorHex, String dropdownColorHex, VBox parentBox) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #F4F4F4; -fx-padding: 12; -fx-background-radius: 12;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + nameColorHex + ";");

        Label priceLabel = new Label("Price: $" + item.price);
        Label quantityLabel = new Label("Qty: " + item.quantity);

        HBox priceQtyBox = new HBox(10, priceLabel, quantityLabel);
        VBox infoBox = new VBox(5, nameLabel, priceQtyBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        ComboBox<String> menuDropdown = new ComboBox<>();
        RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
        if (restaurant != null && restaurant.getMenus() != null) {
            for (RestaurantResponseDto.MenuDto menu : restaurant.getMenus()) {
                menuDropdown.getItems().add(menu.getTitle());
            }
        }

        menuDropdown.setPromptText("Assign to menu...");
        menuDropdown.setStyle(
                "-fx-background-color: " + dropdownColorHex + ";" +
                        "-fx-border-color: transparent;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: null;"
        );

        menuDropdown.setOnAction(e -> {
            String selectedMenu = menuDropdown.getValue();
            if (selectedMenu != null) {
                handleAssignToMenu(item, selectedMenu, card, parentBox);
            }
        });

        card.getChildren().addAll(infoBox, menuDropdown);
        return card;
    }

    private void handleAssignToMenu(MenuItemResponseDto item, String menuTitle, HBox card, VBox parentBox) {
        try {
            RestaurantResponseDto restaurant = SessionManager.getInstance().getCurrentRestaurant();
            if (restaurant == null) {
                showAlert("Error", "No restaurant found in session.");
                return;
            }

            RestaurantEndpoint endpoint = new RestaurantEndpoint();
            List<Long> itemIds = List.of(item.id);

            // Call backend
            endpoint.addItemsToMenu(restaurant.getId(), menuTitle, itemIds);

            // Update session locally
            restaurant.getMenus().stream()
                    .filter(menu -> menu.getTitle().equals(menuTitle))
                    .findFirst()
                    .ifPresent(menu -> {
                        RestaurantResponseDto.MenuDto.ItemDto newItem = new RestaurantResponseDto.MenuDto.ItemDto();
                        newItem.setItemId(item.id);
                        menu.getItems().add(newItem);
                    });

            SessionManager.getInstance().setCurrentRestaurant(restaurant);
            SessionManager.getInstance().writeSessionToTemp();

            // ✅ Remove card from UI
            parentBox.getChildren().remove(card);

            showAlert("Success", "Item '" + item.name + "' added to menu: " + menuTitle);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to add item to menu.");
        }
    }
    /** Creates a styled card for items that are already in a menu (no dropdown). */
    private HBox createMenuItemCardForMenu(MenuItemResponseDto item) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #F4F4F4; -fx-padding: 12; -fx-background-radius: 12;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #391DAA;");

        Label priceLabel = new Label("Price: $" + item.price);
        Label quantityLabel = new Label("Qty: " + item.quantity);

        HBox priceQtyBox = new HBox(10, priceLabel, quantityLabel);
        VBox infoBox = new VBox(5, nameLabel, priceQtyBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        card.getChildren().add(infoBox);
        return card;
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
