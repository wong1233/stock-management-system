package com.wong.stockmanagement;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.List;

/**
 * Collects and validates all values needed to create one of the supported products.
 */
public class ProductDialog extends Dialog<Product> {
    private static final ButtonType ADD_BUTTON_TYPE =
            new ButtonType("Add Product", ButtonBar.ButtonData.OK_DONE);

    private final ComboBox<ProductType> productType =
            createComboBox(List.of(ProductType.values()));

    private final TextField productName = createTextField("e.g. Samsung Smart TV");
    private final TextField itemNumber = createTextField("e.g. 1001");
    private final TextField price = createTextField("e.g. 2499.00");
    private final TextField quantity = createTextField("e.g. 5");

    private final ComboBox<String> refrigeratorDoorDesign = createComboBox(List.of(
            "Single", "Double", "Side", "French", "Top", "Bottom"
    ));
    private final TextField refrigeratorColor = createTextField("e.g. Silver");
    private final TextField refrigeratorCapacity = createTextField("Capacity in litres");

    private final ComboBox<String> tvScreenType = createComboBox(List.of(
            "LED", "LCD", "OLED", "QLED"
    ));
    private final ComboBox<String> tvResolution = createComboBox(List.of(
            "HD", "FHD", "2K", "4K", "8K"
    ));
    private final TextField tvDisplaySize = createTextField("Display size in inches");

    private final TextField laptopColor = createTextField("e.g. Black");
    private final ComboBox<String> laptopProcessor = createComboBox(List.of(
            "Intel Core", "AMD Ryzen"
    ));
    private final ComboBox<String> laptopOperatingSystem = createComboBox(List.of(
            "Linux", "Windows", "iOS"
    ));
    private final TextField laptopStorageSize = createTextField("Storage size in GB");
    private final ComboBox<String> laptopStorageType = createComboBox(List.of("SSD", "HDD"));
    private final TextField laptopGraphicsCard = createTextField("e.g. NVIDIA RTX 4060");
    private final TextField laptopRamSize = createTextField("RAM size in GB");
    private final TextField laptopScreenSize = createTextField("Screen size in inches");

    private final ComboBox<String> washingMachineLoadType = createComboBox(List.of(
            "Front Load", "Top Load"
    ));
    private final TextField washingMachineCapacity = createTextField("Capacity in kg");
    private final TextField washingMachineSpinSpeed = createTextField("Spin speed in RPM");
    private final ComboBox<Integer> washingMachineEnergyRating = createComboBox(List.of(1, 2, 3, 4, 5));

    private final GridPane specificFields = createGrid();
    private Product createdProduct;

    public ProductDialog() {
        setTitle("Add Product");
        setHeaderText("Create a new inventory product");
        setResizable(true);

        getDialogPane().getButtonTypes().addAll(ADD_BUTTON_TYPE, ButtonType.CANCEL);
        getDialogPane().setContent(createContent());
        getDialogPane().setPrefWidth(620);
        getDialogPane().setPrefHeight(690);

        productType.getSelectionModel().selectFirst();
        selectDefaults();
        rebuildSpecificFields(productType.getValue());

        productType.valueProperty().addListener(
                (observable, previousType, selectedType) -> rebuildSpecificFields(selectedType)
        );

        Node addButton = getDialogPane().lookupButton(ADD_BUTTON_TYPE);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                createdProduct = createProduct();
            } catch (InventoryException exception) {
                createdProduct = null;
                showValidationError(exception.getMessage());
                event.consume();
            }
        });

        setResultConverter(buttonType ->
                buttonType == ADD_BUTTON_TYPE ? createdProduct : null
        );
    }

    private ScrollPane createContent() {
        Label commonTitle = createSectionTitle("Common Information");
        GridPane commonFields = createGrid();
        addField(commonFields, 0, "Product Type", productType);
        addField(commonFields, 1, "Product Name", productName);
        addField(commonFields, 2, "Item Number", itemNumber);
        addField(commonFields, 3, "Price (RM)", price);
        addField(commonFields, 4, "Initial Quantity", quantity);

        Label detailsTitle = createSectionTitle("Product Details");

        VBox content = new VBox(12, commonTitle, commonFields, detailsTitle, specificFields);
        content.setPadding(new Insets(8, 10, 8, 4));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    private void rebuildSpecificFields(ProductType selectedType) {
        specificFields.getChildren().clear();

        if (selectedType == null) {
            return;
        }

        switch (selectedType) {
            case REFRIGERATOR -> {
                addField(specificFields, 0, "Door Design", refrigeratorDoorDesign);
                addField(specificFields, 1, "Color", refrigeratorColor);
                addField(specificFields, 2, "Capacity (L)", refrigeratorCapacity);
            }
            case TV -> {
                addField(specificFields, 0, "Screen Type", tvScreenType);
                addField(specificFields, 1, "Resolution", tvResolution);
                addField(specificFields, 2, "Display Size", tvDisplaySize);
            }
            case LAPTOP -> {
                addField(specificFields, 0, "Color", laptopColor);
                addField(specificFields, 1, "Processor", laptopProcessor);
                addField(specificFields, 2, "Operating System", laptopOperatingSystem);
                addField(specificFields, 3, "Storage Size (GB)", laptopStorageSize);
                addField(specificFields, 4, "Storage Type", laptopStorageType);
                addField(specificFields, 5, "Graphics Card", laptopGraphicsCard);
                addField(specificFields, 6, "RAM Size (GB)", laptopRamSize);
                addField(specificFields, 7, "Screen Size", laptopScreenSize);
            }
            case WASHING_MACHINE -> {
                addField(specificFields, 0, "Load Type", washingMachineLoadType);
                addField(specificFields, 1, "Capacity (kg)", washingMachineCapacity);
                addField(specificFields, 2, "Spin Speed (RPM)", washingMachineSpinSpeed);
                addField(specificFields, 3, "Energy Rating", washingMachineEnergyRating);
            }
        }
    }

    private Product createProduct() {
        ProductType selectedType = requireSelection(productType, "Product type");
        CommonProductData commonData = readCommonProductData();

        return switch (selectedType) {
            case REFRIGERATOR -> createRefrigerator(commonData);
            case TV -> createTv(commonData);
            case LAPTOP -> createLaptop(commonData);
            case WASHING_MACHINE -> createWashingMachine(commonData);
        };
    }

    private CommonProductData readCommonProductData() {
        return new CommonProductData(
                requireText(productName, "Product name"),
                parsePositiveInteger(itemNumber, "Item number"),
                parsePositiveDouble(price, "Price"),
                parseNonNegativeInteger(quantity, "Initial quantity")
        );
    }

    private Refrigerator createRefrigerator(CommonProductData commonData) {
        return new Refrigerator(
                commonData.name(),
                commonData.itemNumber(),
                commonData.price(),
                commonData.quantity(),
                requireSelection(refrigeratorDoorDesign, "Door design"),
                requireText(refrigeratorColor, "Color"),
                parsePositiveDouble(refrigeratorCapacity, "Capacity")
        );
    }

    private TV createTv(CommonProductData commonData) {
        return new TV(
                commonData.name(),
                commonData.itemNumber(),
                commonData.price(),
                commonData.quantity(),
                requireSelection(tvScreenType, "Screen type"),
                requireSelection(tvResolution, "Resolution"),
                parsePositiveDouble(tvDisplaySize, "Display size")
        );
    }

    private Laptop createLaptop(CommonProductData commonData) {
        return new Laptop(
                commonData.name(),
                commonData.itemNumber(),
                commonData.price(),
                commonData.quantity(),
                requireText(laptopColor, "Color"),
                requireSelection(laptopProcessor, "Processor"),
                requireSelection(laptopOperatingSystem, "Operating system"),
                parsePositiveInteger(laptopStorageSize, "Storage size"),
                requireSelection(laptopStorageType, "Storage type"),
                requireText(laptopGraphicsCard, "Graphics card"),
                parsePositiveInteger(laptopRamSize, "RAM size"),
                parsePositiveDouble(laptopScreenSize, "Screen size")
        );
    }

    private WashingMachine createWashingMachine(CommonProductData commonData) {
        return new WashingMachine(
                commonData.name(),
                commonData.itemNumber(),
                commonData.price(),
                commonData.quantity(),
                requireSelection(washingMachineLoadType, "Load type"),
                parsePositiveInteger(washingMachineCapacity, "Capacity"),
                parsePositiveInteger(washingMachineSpinSpeed, "Spin speed"),
                requireSelection(washingMachineEnergyRating, "Energy rating")
        );
    }

    private void selectDefaults() {
        refrigeratorDoorDesign.getSelectionModel().selectFirst();
        tvScreenType.getSelectionModel().selectFirst();
        tvResolution.getSelectionModel().selectFirst();
        laptopProcessor.getSelectionModel().selectFirst();
        laptopOperatingSystem.getSelectionModel().selectFirst();
        laptopStorageType.getSelectionModel().selectFirst();
        washingMachineLoadType.getSelectionModel().selectFirst();
        washingMachineEnergyRating.getSelectionModel().selectLast();
    }

    private static GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(11);
        grid.setPadding(new Insets(4, 0, 10, 0));
        return grid;
    }

    private static void addField(GridPane grid, int row, String labelText, Control control) {
        Label label = new Label(labelText);
        label.setMinWidth(135);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");

        control.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(control, Priority.ALWAYS);
        grid.add(label, 0, row);
        grid.add(control, 1, row);
    }

    private static Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #172033;");
        return label;
    }

    private static TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    private static <T> ComboBox<T> createComboBox(List<T> values) {
        ComboBox<T> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }

    private static String requireText(TextField field, String fieldName) {
        String value = field.getText() == null ? "" : field.getText().trim();
        if (value.isEmpty()) {
            throw new InventoryException(fieldName + " cannot be empty.");
        }
        return value;
    }

    private static <T> T requireSelection(ComboBox<T> comboBox, String fieldName) {
        T value = comboBox.getValue();
        if (value == null) {
            throw new InventoryException(fieldName + " must be selected.");
        }
        return value;
    }

    private static int parsePositiveInteger(TextField field, String fieldName) {
        int value = parseInteger(field, fieldName);
        if (value <= 0) {
            throw new InventoryException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    private static int parseNonNegativeInteger(TextField field, String fieldName) {
        int value = parseInteger(field, fieldName);
        if (value < 0) {
            throw new InventoryException(fieldName + " cannot be negative.");
        }
        return value;
    }

    private static int parseInteger(TextField field, String fieldName) {
        String value = requireText(field, fieldName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new InventoryException(fieldName + " must be a whole number.");
        }
    }

    private static double parsePositiveDouble(TextField field, String fieldName) {
        String value = requireText(field, fieldName);
        try {
            double number = Double.parseDouble(value);
            if (!Double.isFinite(number) || number <= 0) {
                throw new InventoryException(fieldName + " must be greater than 0.");
            }
            return number;
        } catch (NumberFormatException exception) {
            throw new InventoryException(fieldName + " must be a valid number.");
        }
    }

    private record CommonProductData(
            String name,
            int itemNumber,
            double price,
            int quantity
    ) {
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Product");
        alert.setHeaderText("Please correct the product information");
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
