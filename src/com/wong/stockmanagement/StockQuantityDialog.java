package com.wong.stockmanagement;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.List;

/**
 * Collects and validates a quantity for an add-stock or deduct-stock operation.
 */
public class StockQuantityDialog extends Dialog<Integer> {
    public enum Operation {
        ADD("Add Stock", "Quantity to add"),
        DEDUCT("Deduct Stock", "Quantity to deduct");

        private final String title;
        private final String fieldLabel;

        Operation(String title, String fieldLabel) {
            this.title = title;
            this.fieldLabel = fieldLabel;
        }

        public String getTitle() {
            return title;
        }

        public String getFieldLabel() {
            return fieldLabel;
        }
    }

    private final Product product;
    private final Operation operation;
    private final TextField quantityField = new TextField();
    private final ButtonType confirmButtonType;
    private int selectedQuantity;

    public StockQuantityDialog(Product product, Operation operation) {
        this.product = product;
        this.operation = operation;
        this.confirmButtonType = new ButtonType(
                operation.getTitle(),
                ButtonBar.ButtonData.OK_DONE
        );

        setTitle(operation.getTitle());
        setHeaderText(operation.getTitle() + " for " + product.getProductName());
        getDialogPane().getButtonTypes().addAll(
                List.of(confirmButtonType, ButtonType.CANCEL)
        );
        getDialogPane().setContent(createContent());
        getDialogPane().setPrefWidth(430);

        Node confirmButton = getDialogPane().lookupButton(confirmButtonType);
        confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                selectedQuantity = readQuantity();
            } catch (InventoryException exception) {
                showValidationError(exception.getMessage());
                event.consume();
            }
        });

        setResultConverter(buttonType ->
                buttonType == confirmButtonType ? selectedQuantity : null
        );
    }

    private VBox createContent() {
        Label currentQuantity = new Label(
                "Current quantity: " + product.getQuantityAvailable()
        );
        currentQuantity.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");

        Label quantityLabel = new Label(operation.getFieldLabel());
        quantityLabel.setStyle("-fx-text-fill: #475569;");

        quantityField.setPromptText("Enter a whole number greater than 0");

        VBox content = new VBox(10, currentQuantity, quantityLabel, quantityField);
        content.setPadding(new Insets(8));
        return content;
    }

    private int readQuantity() {
        String text = quantityField.getText() == null
                ? ""
                : quantityField.getText().trim();

        if (text.isEmpty()) {
            throw new InventoryException("Quantity cannot be empty.");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new InventoryException("Quantity must be a whole number.");
        }

        if (quantity <= 0) {
            throw new InventoryException("Quantity must be greater than 0.");
        }
        if (operation == Operation.DEDUCT && quantity > product.getQuantityAvailable()) {
            throw new InventoryException(
                    "Not enough stock. Current quantity is "
                            + product.getQuantityAvailable() + "."
            );
        }

        return quantity;
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Quantity");
        alert.setHeaderText("Please correct the stock quantity");
        alert.setContentText(message);

        Window owner = getDialogPane().getScene() == null
                ? null
                : getDialogPane().getScene().getWindow();
        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.showAndWait();
    }
}
