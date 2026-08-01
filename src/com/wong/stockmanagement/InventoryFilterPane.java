package com.wong.stockmanagement;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * Provides search, product-type, and status filters for the inventory table.
 */
public class InventoryFilterPane extends HBox {
    private enum StatusFilter {
        ALL("All Statuses"),
        ACTIVE("Active"),
        DISCONTINUED("Discontinued");

        private final String displayName;

        StatusFilter(String displayName) {
            this.displayName = displayName;
        }

        private boolean matches(Product product) {
            return switch (this) {
                case ALL -> true;
                case ACTIVE -> product.getStatus();
                case DISCONTINUED -> !product.getStatus();
            };
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private final TextField searchField = new TextField();
    private final ComboBox<ProductType> productTypeFilter = new ComboBox<>();
    private final ComboBox<StatusFilter> statusFilter = new ComboBox<>();
    private final Runnable filterChanged;

    public InventoryFilterPane(Runnable filterChanged) {
        this.filterChanged = filterChanged;

        configureControls();

        VBox searchBox = createControlBox("SEARCH", searchField);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        VBox typeBox = createControlBox("PRODUCT TYPE", productTypeFilter);
        VBox statusBox = createControlBox("STATUS", statusFilter);

        Button clearButton = new Button("Clear Filters");
        clearButton.setPrefHeight(38);
        clearButton.setStyle(
                "-fx-background-color: #e2e8f0; -fx-background-radius: 6px; "
                        + "-fx-text-fill: #334155; -fx-font-weight: bold;"
        );
        clearButton.setOnAction(event -> clearFilters());

        setSpacing(12);
        setAlignment(Pos.BOTTOM_LEFT);
        setStyle(
                "-fx-background-color: white; -fx-background-radius: 6px; "
                        + "-fx-border-color: #dbe2ea; -fx-border-radius: 6px; "
                        + "-fx-padding: 12px;"
        );
        getChildren().addAll(List.of(searchBox, typeBox, statusBox, clearButton));
        HBox.setHgrow(searchBox, Priority.ALWAYS);
    }

    public Predicate<Product> createPredicate() {
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        ProductType selectedType = productTypeFilter.getValue();
        StatusFilter selectedStatus = statusFilter.getValue();

        return product -> matchesSearch(product, query)
                && (selectedType == null || ProductType.fromProduct(product) == selectedType)
                && (selectedStatus == null || selectedStatus.matches(product));
    }

    private void configureControls() {
        searchField.setPromptText("Search by name, item number, or type");
        searchField.setPrefWidth(260);
        searchField.setMaxWidth(Double.MAX_VALUE);

        productTypeFilter.setItems(
                FXCollections.observableArrayList(ProductType.values())
        );
        productTypeFilter.setPromptText("All Product Types");
        productTypeFilter.setPrefWidth(150);

        statusFilter.setItems(
                FXCollections.observableArrayList(Arrays.asList(StatusFilter.values()))
        );
        statusFilter.getSelectionModel().select(StatusFilter.ALL);
        statusFilter.setPrefWidth(135);

        searchField.textProperty().addListener(
                (observable, previousValue, currentValue) -> filterChanged.run()
        );
        productTypeFilter.valueProperty().addListener(
                (observable, previousValue, currentValue) -> filterChanged.run()
        );
        statusFilter.valueProperty().addListener(
                (observable, previousValue, currentValue) -> filterChanged.run()
        );
    }

    private VBox createControlBox(String title, javafx.scene.Node control) {
        Label label = new Label(title);
        label.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #64748b;"
        );

        VBox box = new VBox(5, label, control);
        box.setAlignment(Pos.BOTTOM_LEFT);
        return box;
    }

    private boolean matchesSearch(Product product, String query) {
        if (query.isEmpty()) {
            return true;
        }

        String productName = product.getProductName().toLowerCase(Locale.ROOT);
        String itemNumber = String.valueOf(product.getItemNum());
        String productType = ProductType.fromProduct(product)
                .toString()
                .toLowerCase(Locale.ROOT);

        return productName.contains(query)
                || itemNumber.contains(query)
                || productType.contains(query);
    }

    private void clearFilters() {
        searchField.clear();
        productTypeFilter.getSelectionModel().clearSelection();
        statusFilter.getSelectionModel().select(StatusFilter.ALL);
        filterChanged.run();
    }
}
