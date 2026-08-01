package com.wong.stockmanagement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StockManagementApp extends Application {
    private static final String BACKGROUND_COLOR = "#f4f6f8";
    private static final String PRIMARY_COLOR = "#2563eb";

    private final InventoryManager inventoryManager = new InventoryManager();

    private UserInfo currentUser;
    private TableView<Product> productTable;
    private Label totalProductsValue;
    private Label totalUnitsValue;
    private Label activeProductsValue;
    private Label totalValueValue;

    private Button addStockButton;
    private Button deductStockButton;
    private Button discontinueButton;
    private Button viewDetailsButton;

    @Override
    public void start(Stage stage) {
        if (!collectUserInformation()) {
            Platform.exit();
            return;
        }

        productTable = createProductTable();

        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createSidebar());
        root.setCenter(createDashboard());
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        configureSelectionBehavior();
        inventoryManager.getProducts().addListener(
                (ListChangeListener<Product>) change -> refreshDashboard()
        );
        refreshDashboard();

        Scene scene = new Scene(root, 1180, 760);

        stage.setTitle("Stock Management System");
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.setScene(scene);
        stage.show();
    }

    private boolean collectUserInformation() {
        UserInfoDialog dialog = new UserInfoDialog();
        currentUser = dialog.showAndWait().orElse(null);
        return currentUser != null;
    }

    private HBox createHeader() {
        Label title = new Label("Stock Management System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Inventory Dashboard");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #cbd5e1;");

        VBox titleBox = new VBox(3, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userName = new Label("Welcome, " + currentUser.getUserName());
        userName.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;"
        );

        Label userId = new Label("User ID: " + currentUser.getUserID());
        userId.setStyle("-fx-font-size: 12px; -fx-text-fill: #cbd5e1;");

        String formattedDate = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd MMM yyyy")
        );
        Label date = new Label(formattedDate);
        date.setStyle("-fx-font-size: 12px; -fx-text-fill: #e2e8f0;");

        Label version = new Label("JavaFX v2.0");
        version.setStyle(
                "-fx-background-color: #334155; -fx-background-radius: 14px; "
                        + "-fx-padding: 6px 11px; -fx-text-fill: white; -fx-font-weight: bold;"
        );

        HBox metadata = new HBox(10, date, version);
        metadata.setAlignment(Pos.CENTER_RIGHT);

        VBox headerInfo = new VBox(4, userName, userId, metadata);
        headerInfo.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(20, titleBox, spacer, headerInfo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 28, 18, 28));
        header.setStyle("-fx-background-color: #172033;");
        return header;
    }

    private VBox createSidebar() {
        Label navigationTitle = new Label("INVENTORY ACTIONS");
        navigationTitle.setStyle(
                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #94a3b8;"
        );

        Button addProductButton = createActionButton("Add Product", PRIMARY_COLOR);
        addStockButton = createActionButton("Add Stock", "#475569");
        deductStockButton = createActionButton("Deduct Stock", "#475569");
        discontinueButton = createActionButton("Discontinue", "#b91c1c");
        viewDetailsButton = createActionButton("View Details", "#475569");

        addProductButton.setOnAction(event -> openAddProductDialog());
        addStockButton.setOnAction(event -> openStockQuantityDialog(
                StockQuantityDialog.Operation.ADD
        ));
        deductStockButton.setOnAction(event -> openStockQuantityDialog(
                StockQuantityDialog.Operation.DEDUCT
        ));
        discontinueButton.setOnAction(event -> discontinueSelectedProduct());
        viewDetailsButton.setOnAction(event -> showSelectedProductDetails());

        VBox sidebar = new VBox(
                12,
                navigationTitle,
                addProductButton,
                addStockButton,
                deductStockButton,
                discontinueButton,
                viewDetailsButton
        );
        sidebar.setPadding(new Insets(24, 16, 24, 16));
        sidebar.setPrefWidth(205);
        sidebar.setStyle("-fx-background-color: #1f2937;");
        return sidebar;
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-background-radius: 6px; -fx-text-fill: white; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 0 14px;"
        );
        return button;
    }

    private VBox createDashboard() {
        Label sectionTitle = new Label("Product Inventory");
        sectionTitle.setStyle("-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: #172033;");

        Label sectionSubtitle = new Label(
                "Select a product to manage its stock, status, or details."
        );
        sectionSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        VBox sectionHeading = new VBox(4, sectionTitle, sectionSubtitle);

        HBox summaryBar = createSummaryBar();

        VBox dashboard = new VBox(18, sectionHeading, productTable, summaryBar);
        dashboard.setPadding(new Insets(24));
        dashboard.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(productTable, Priority.ALWAYS);
        return dashboard;
    }

    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();
        table.setItems(inventoryManager.getProducts());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(
                "-fx-background-color: white; -fx-border-color: #dbe2ea; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px;"
        );

        Label emptyTitle = new Label("No products in inventory");
        emptyTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        Label emptyHint = new Label("Use Add Product to create the first inventory item.");
        emptyHint.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        VBox placeholder = new VBox(7, emptyTitle, emptyHint);
        placeholder.setAlignment(Pos.CENTER);
        table.setPlaceholder(placeholder);

        TableColumn<Product, Number> itemNumberColumn = new TableColumn<>("Item No.");
        itemNumberColumn.setCellValueFactory(cellData ->
                new ReadOnlyIntegerWrapper(cellData.getValue().getItemNum())
        );
        itemNumberColumn.setMinWidth(85);

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getProductName())
        );
        productNameColumn.setMinWidth(170);

        TableColumn<Product, String> productTypeColumn = new TableColumn<>("Type");
        productTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(getProductType(cellData.getValue()))
        );
        productTypeColumn.setMinWidth(125);

        TableColumn<Product, Number> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData ->
                new ReadOnlyDoubleWrapper(cellData.getValue().getPrice())
        );
        priceColumn.setCellFactory(column -> createCurrencyCell());
        priceColumn.setMinWidth(115);

        TableColumn<Product, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData ->
                new ReadOnlyIntegerWrapper(cellData.getValue().getQuantityAvailable())
        );
        quantityColumn.setMinWidth(90);

        TableColumn<Product, Number> inventoryValueColumn = new TableColumn<>("Inventory Value");
        inventoryValueColumn.setCellValueFactory(cellData ->
                new ReadOnlyDoubleWrapper(cellData.getValue().getInventoryValue())
        );
        inventoryValueColumn.setCellFactory(column -> createCurrencyCell());
        inventoryValueColumn.setMinWidth(145);

        TableColumn<Product, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getStatus() ? "Active" : "Discontinued"
                )
        );
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(status);
                if ("Active".equals(status)) {
                    setStyle("-fx-text-fill: #15803d; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #b91c1c; -fx-font-weight: bold;");
                }
            }
        });
        statusColumn.setMinWidth(120);

        table.getColumns().addAll(java.util.List.of(
                itemNumberColumn,
                productNameColumn,
                productTypeColumn,
                priceColumn,
                quantityColumn,
                inventoryValueColumn,
                statusColumn
        ));

        table.setRowFactory(ignored -> new TableRow<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null || product.getStatus()) {
                    setStyle("");
                } else {
                    setStyle("-fx-opacity: 0.65;");
                }
            }
        });

        return table;
    }

    private TableCell<Product, Number> createCurrencyCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : String.format("RM %,.2f", amount.doubleValue()));
            }
        };
    }

    private HBox createSummaryBar() {
        totalProductsValue = createSummaryValue();
        totalUnitsValue = createSummaryValue();
        activeProductsValue = createSummaryValue();
        totalValueValue = createSummaryValue();

        VBox totalProductsCard = createSummaryCard("TOTAL PRODUCTS", totalProductsValue);
        VBox totalUnitsCard = createSummaryCard("TOTAL UNITS", totalUnitsValue);
        VBox activeProductsCard = createSummaryCard("ACTIVE PRODUCTS", activeProductsValue);
        VBox totalValueCard = createSummaryCard("INVENTORY VALUE", totalValueValue);

        HBox summaryBar = new HBox(
                14,
                totalProductsCard,
                totalUnitsCard,
                activeProductsCard,
                totalValueCard
        );
        summaryBar.getChildren().forEach(card -> HBox.setHgrow(card, Priority.ALWAYS));
        return summaryBar;
    }

    private Label createSummaryValue() {
        Label value = new Label("0");
        value.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #172033;");
        return value;
    }

    private VBox createSummaryCard(String title, Label value) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #64748b;"
        );

        VBox card = new VBox(7, titleLabel, value);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 6px; " +
                        "-fx-border-color: #dbe2ea; -fx-border-radius: 6px;"
        );
        return card;
    }

    private void configureSelectionBehavior() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, previousProduct, selectedProduct) -> updateActionButtons(selectedProduct)
        );
        updateActionButtons(null);
    }

    private void updateActionButtons(Product selectedProduct) {
        boolean noSelection = selectedProduct == null;
        boolean discontinued = !noSelection && !selectedProduct.getStatus();

        addStockButton.setDisable(noSelection || discontinued);
        deductStockButton.setDisable(noSelection || discontinued);
        discontinueButton.setDisable(noSelection || discontinued);
        viewDetailsButton.setDisable(noSelection);
    }

    private void refreshDashboard() {
        productTable.refresh();
        totalProductsValue.setText(String.valueOf(inventoryManager.getTotalProducts()));
        totalUnitsValue.setText(String.valueOf(inventoryManager.getTotalQuantity()));
        activeProductsValue.setText(String.valueOf(inventoryManager.getActiveProductCount()));
        totalValueValue.setText(String.format("RM %,.2f", inventoryManager.getTotalInventoryValue()));

        updateActionButtons(productTable.getSelectionModel().getSelectedItem());
    }

    private String getProductType(Product product) {
        return ProductType.fromProduct(product).toString();
    }

    private void openAddProductDialog() {
        ProductDialog dialog = new ProductDialog();

        initializeDialogOwner(dialog);
        dialog.showAndWait().ifPresent(this::addProduct);
    }

    private void addProduct(Product product) {
        try {
            inventoryManager.addProduct(product);
            productTable.getSelectionModel().select(product);
            refreshDashboard();
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

            refreshDashboard();
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
            refreshDashboard();
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
        return productTable.getSelectionModel().getSelectedItem();
    }

    private void initializeDialogOwner(Dialog<?> dialog) {
        if (productTable.getScene() != null) {
            dialog.initOwner(productTable.getScene().getWindow());
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
