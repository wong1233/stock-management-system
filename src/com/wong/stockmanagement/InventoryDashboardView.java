package com.wong.stockmanagement;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds and updates the inventory dashboard without owning business operations.
 */
public class InventoryDashboardView {
    private static final String BACKGROUND_COLOR = "#f4f6f8";
    private static final String PRIMARY_COLOR = "#2563eb";

    private final BorderPane root = new BorderPane();
    private final FilteredList<Product> filteredProducts;
    private final TableView<Product> productTable;

    private InventoryFilterPane inventoryFilterPane;
    private Label totalProductsValue;
    private Label totalUnitsValue;
    private Label activeProductsValue;
    private Label totalValueValue;

    private Button addStockButton;
    private Button deductStockButton;
    private Button discontinueButton;
    private Button viewDetailsButton;

    public InventoryDashboardView(
            UserInfo currentUser,
            ObservableList<Product> products,
            Runnable addProductAction,
            Runnable addStockAction,
            Runnable deductStockAction,
            Runnable discontinueAction,
            Runnable viewDetailsAction
    ) {
        filteredProducts = new FilteredList<>(products, product -> true);
        productTable = createProductTable();

        root.setTop(createHeader(currentUser));
        root.setLeft(createSidebar(
                addProductAction,
                addStockAction,
                deductStockAction,
                discontinueAction,
                viewDetailsAction
        ));
        root.setCenter(createDashboard());
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        configureSelectionBehavior();
    }

    public BorderPane getRoot() {
        return root;
    }

    public Product getSelectedProduct() {
        return productTable.getSelectionModel().getSelectedItem();
    }

    public void selectProduct(Product product) {
        productTable.getSelectionModel().select(product);
    }

    public void refresh(InventoryManager inventoryManager) {
        applyProductFilters();
        productTable.refresh();
        totalProductsValue.setText(String.valueOf(inventoryManager.getTotalProducts()));
        totalUnitsValue.setText(String.valueOf(inventoryManager.getTotalQuantity()));
        activeProductsValue.setText(String.valueOf(inventoryManager.getActiveProductCount()));
        totalValueValue.setText(
                String.format("RM %,.2f", inventoryManager.getTotalInventoryValue())
        );

        updateActionButtons(getSelectedProduct());
    }

    private HBox createHeader(UserInfo currentUser) {
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

    private VBox createSidebar(
            Runnable addProductAction,
            Runnable addStockAction,
            Runnable deductStockAction,
            Runnable discontinueAction,
            Runnable viewDetailsAction
    ) {
        Label navigationTitle = new Label("INVENTORY ACTIONS");
        navigationTitle.setStyle(
                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #94a3b8;"
        );

        Button addProductButton = createActionButton("Add Product", PRIMARY_COLOR);
        addStockButton = createActionButton("Add Stock", "#475569");
        deductStockButton = createActionButton("Deduct Stock", "#475569");
        discontinueButton = createActionButton("Discontinue", "#b91c1c");
        viewDetailsButton = createActionButton("View Details", "#475569");

        addProductButton.setOnAction(_ -> addProductAction.run());
        addStockButton.setOnAction(_ -> addStockAction.run());
        deductStockButton.setOnAction(_ -> deductStockAction.run());
        discontinueButton.setOnAction(_ -> discontinueAction.run());
        viewDetailsButton.setOnAction(_ -> viewDetailsAction.run());

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
                "-fx-background-color: " + color + "; "
                        + "-fx-background-radius: 6px; -fx-text-fill: white; "
                        + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 0 14px;"
        );
        return button;
    }

    private VBox createDashboard() {
        Label sectionTitle = new Label("Product Inventory");
        sectionTitle.setStyle(
                "-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: #172033;"
        );

        Label sectionSubtitle = new Label(
                "Search or filter products, then select one to manage its inventory."
        );
        sectionSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        VBox sectionHeading = new VBox(4, sectionTitle, sectionSubtitle);
        inventoryFilterPane = new InventoryFilterPane(this::applyProductFilters);

        HBox summaryBar = createSummaryBar();

        VBox dashboard = new VBox(
                18,
                sectionHeading,
                inventoryFilterPane,
                productTable,
                summaryBar
        );
        dashboard.setPadding(new Insets(24));
        dashboard.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        VBox.setVgrow(productTable, Priority.ALWAYS);
        return dashboard;
    }

    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();

        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedProducts);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(
                "-fx-background-color: white; -fx-border-color: #dbe2ea; "
                        + "-fx-border-radius: 6px; -fx-background-radius: 6px;"
        );

        Label emptyTitle = new Label("No products to display");
        emptyTitle.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #475569;"
        );
        Label emptyHint = new Label("Add a product or adjust the search and filters.");
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
                new ReadOnlyStringWrapper(
                        ProductType.fromProduct(cellData.getValue()).toString()
                )
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

        TableColumn<Product, Number> inventoryValueColumn =
                new TableColumn<>("Inventory Value");
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

        table.getColumns().addAll(List.of(
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
                setText(
                        empty || amount == null
                                ? null
                                : String.format("RM %,.2f", amount.doubleValue())
                );
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
        value.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #172033;"
        );
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
                "-fx-background-color: white; -fx-background-radius: 6px; "
                        + "-fx-border-color: #dbe2ea; -fx-border-radius: 6px;"
        );
        return card;
    }

    private void configureSelectionBehavior() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, previousProduct, selectedProduct) ->
                        updateActionButtons(selectedProduct)
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

    private void applyProductFilters() {
        if (inventoryFilterPane != null) {
            filteredProducts.setPredicate(inventoryFilterPane.createPredicate());
        }
    }
}
