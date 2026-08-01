package com.wong.stockmanagement;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Displays the common and type-specific values of one inventory product.
 */
public class ProductDetailsDialog extends Dialog<ButtonType> {
    public ProductDetailsDialog(Product product) {
        setTitle("Product Details");
        setHeaderText(product.getProductName());
        setResizable(true);

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        getDialogPane().setContent(createContent(product));
        getDialogPane().setPrefWidth(560);
        getDialogPane().setPrefHeight(580);
    }

    private ScrollPane createContent(Product product) {
        Label commonTitle = createSectionTitle("Inventory Information");
        GridPane commonDetails = createGrid();
        int row = 0;
        addDetail(commonDetails, row++, "Product Type", ProductType.fromProduct(product).toString());
        addDetail(commonDetails, row++, "Item Number", String.valueOf(product.getItemNum()));
        addDetail(commonDetails, row++, "Product Name", product.getProductName());
        addDetail(commonDetails, row++, "Price", formatCurrency(product.getPrice()));
        addDetail(commonDetails, row++, "Quantity", String.valueOf(product.getQuantityAvailable()));
        addDetail(commonDetails, row++, "Inventory Value", formatCurrency(product.getInventoryValue()));
        addDetail(commonDetails, row, "Status", product.getStatus() ? "Active" : "Discontinued");

        Label specificTitle = createSectionTitle("Product Specifications");
        GridPane specificDetails = createSpecificDetails(product);

        VBox content = new VBox(12, commonTitle, commonDetails, specificTitle, specificDetails);
        content.setPadding(new Insets(8, 10, 8, 4));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    private GridPane createSpecificDetails(Product product) {
        GridPane details = createGrid();

        if (product instanceof Refrigerator refrigerator) {
            addDetail(details, 0, "Door Design", refrigerator.getDoorDesign());
            addDetail(details, 1, "Color", refrigerator.getColor());
            addDetail(details, 2, "Capacity", refrigerator.getCapacity() + " L");
        } else if (product instanceof TV tv) {
            addDetail(details, 0, "Screen Type", tv.getScreenType());
            addDetail(details, 1, "Resolution", tv.getResolution());
            addDetail(details, 2, "Display Size", tv.getDisplaySize() + " inches");
        } else if (product instanceof Laptop laptop) {
            addDetail(details, 0, "Color", laptop.getColor());
            addDetail(details, 1, "Processor", laptop.getProcessor());
            addDetail(details, 2, "Operating System", laptop.getOperatingSystem());
            addDetail(details, 3, "Storage", laptop.getStorageSize() + " GB " + laptop.getStorageType());
            addDetail(details, 4, "Graphics Card", laptop.getGraphicsCard());
            addDetail(details, 5, "RAM", laptop.getRamSize() + " GB");
            addDetail(details, 6, "Screen Size", laptop.getScreenSize() + " inches");
        } else if (product instanceof WashingMachine washingMachine) {
            addDetail(details, 0, "Load Type", washingMachine.getLoadType());
            addDetail(details, 1, "Capacity", washingMachine.getCapacity() + " kg");
            addDetail(details, 2, "Spin Speed", washingMachine.getSpinSpeed() + " RPM");
            addDetail(details, 3, "Energy Rating", washingMachine.getEnergyRating() + " stars");
        }

        return details;
    }

    private static GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(11);
        grid.setPadding(new Insets(4, 0, 10, 0));
        return grid;
    }

    private static void addDetail(GridPane grid, int row, String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setMinWidth(140);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");

        Label value = new Label(valueText);
        value.setWrapText(true);
        value.setMaxWidth(Double.MAX_VALUE);
        value.setStyle("-fx-text-fill: #475569;");
        GridPane.setHgrow(value, Priority.ALWAYS);

        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }

    private static Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #172033;");
        return label;
    }

    private static String formatCurrency(double amount) {
        return String.format("RM %,.2f", amount);
    }
}
