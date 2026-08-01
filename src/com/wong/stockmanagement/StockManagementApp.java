package com.wong.stockmanagement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * Coordinates the JavaFX application, business operations, and persistence.
 */
public class StockManagementApp extends Application {
    private final InventoryManager inventoryManager = new InventoryManager();
    private final InventoryStorage inventoryStorage = InventoryStorage.createDefault();

    private UserInfo currentUser;
    private InventoryDashboardView dashboardView;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        if (!collectUserInformation()) {
            Platform.exit();
            return;
        }

        loadSavedInventory();
        dashboardView = createDashboardView();

        inventoryManager.getProducts().addListener(
                (ListChangeListener<Product>) _ -> {
                    dashboardView.refresh(inventoryManager);
                    saveInventory();
                }
        );
        dashboardView.refresh(inventoryManager);

        Scene scene = new Scene(dashboardView.getRoot(), 1180, 760);

        stage.setTitle("Stock Management System");
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.setScene(scene);
        stage.show();
    }

    private InventoryDashboardView createDashboardView() {
        return new InventoryDashboardView(
                currentUser,
                inventoryManager.getProducts(),
                this::openAddProductDialog,
                () -> openStockQuantityDialog(StockQuantityDialog.Operation.ADD),
                () -> openStockQuantityDialog(StockQuantityDialog.Operation.DEDUCT),
                this::discontinueSelectedProduct,
                this::showSelectedProductDetails
        );
    }

    private void loadSavedInventory() {
        try {
            inventoryManager.replaceProducts(inventoryStorage.loadProducts());
        } catch (InventoryException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Load Inventory");
            alert.setHeaderText("The saved inventory could not be loaded");
            alert.setContentText(
                    exception.getMessage()
                            + "\n\nA new empty inventory will be used."
            );
            alert.showAndWait();
        }
    }

    private void saveInventory() {
        try {
            inventoryStorage.saveProducts(inventoryManager.getProducts());
        } catch (InventoryException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Unable to Save Inventory",
                    exception.getMessage()
            );
        }
    }

    private boolean collectUserInformation() {
        UserInfoDialog dialog = new UserInfoDialog();
        currentUser = dialog.showAndWait().orElse(null);
        return currentUser != null;
    }

    private void openAddProductDialog() {
        ProductDialog dialog = new ProductDialog();
        initializeDialogOwner(dialog);
        dialog.showAndWait().ifPresent(this::addProduct);
    }

    private void addProduct(Product product) {
        try {
            inventoryManager.addProduct(product);
            dashboardView.selectProduct(product);
            dashboardView.refresh(inventoryManager);
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Product Added",
                    product.getProductName() + " was added successfully."
            );
        } catch (InventoryException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Unable to Add Product",
                    exception.getMessage()
            );
        }
    }

    private void openStockQuantityDialog(StockQuantityDialog.Operation operation) {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }

        StockQuantityDialog dialog = new StockQuantityDialog(product, operation);
        initializeDialogOwner(dialog);
        dialog.showAndWait().ifPresent(quantity ->
                updateStock(product, operation, quantity)
        );
    }

    private void updateStock(
            Product product,
            StockQuantityDialog.Operation operation,
            int quantity
    ) {
        try {
            if (operation == StockQuantityDialog.Operation.ADD) {
                inventoryManager.addStock(product, quantity);
            } else {
                inventoryManager.deductStock(product, quantity);
            }

            saveInventory();
            dashboardView.refresh(inventoryManager);
            String action = operation == StockQuantityDialog.Operation.ADD
                    ? "added to"
                    : "deducted from";
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Stock Updated",
                    quantity + " units were " + action + " "
                            + product.getProductName() + ".\nNew quantity: "
                            + product.getQuantityAvailable()
            );
        } catch (InventoryException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Unable to Update Stock",
                    exception.getMessage()
            );
        }
    }

    private void discontinueSelectedProduct() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Discontinue " + product.getProductName()
                        + "? Stock operations will no longer be available.",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        confirmation.setTitle("Discontinue Product");
        confirmation.setHeaderText("Confirm product status change");
        initializeDialogOwner(confirmation);

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.YES) {
            return;
        }

        try {
            inventoryManager.discontinueProduct(product);
            saveInventory();
            dashboardView.refresh(inventoryManager);
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Product Discontinued",
                    product.getProductName() + " is now discontinued."
            );
        } catch (InventoryException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Unable to Discontinue Product",
                    exception.getMessage()
            );
        }
    }

    private void showSelectedProductDetails() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }

        ProductDetailsDialog dialog = new ProductDetailsDialog(product);
        initializeDialogOwner(dialog);
        dialog.showAndWait();
    }

    private Product getSelectedProduct() {
        return dashboardView.getSelectedProduct();
    }

    private void initializeDialogOwner(Dialog<?> dialog) {
        if (primaryStage != null && primaryStage.isShowing()) {
            dialog.initOwner(primaryStage);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        initializeDialogOwner(alert);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
