package com.wong.stockmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;
import java.util.Optional;

/**
 * Owns the product collection and applies the inventory rules used by the UI.
 */
public class InventoryManager {
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Product> readOnlyProducts =
            FXCollections.unmodifiableObservableList(products);

    /**
     * Returns a live, read-only view for controls such as TableView.
     */
    public ObservableList<Product> getProducts() {
        return readOnlyProducts;
    }

    public void addProduct(Product product) {
        Objects.requireNonNull(product, "Product cannot be null.");
        validateNewProduct(product);
        products.add(product);
    }

    public Optional<Product> findProductByItemNumber(int itemNumber) {
        return products.stream()
                .filter(product -> product.getItemNum() == itemNumber)
                .findFirst();
    }

    public boolean isItemNumberInUse(int itemNumber) {
        return findProductByItemNumber(itemNumber).isPresent();
    }

    public void addStock(Product product, int quantity) {
        requireManagedProduct(product);
        requireActiveProduct(product);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to add must be greater than 0.");
        }

        product.addStock(quantity);
    }

    public void deductStock(Product product, int quantity) {
        requireManagedProduct(product);
        requireActiveProduct(product);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to deduct must be greater than 0.");
        }
        if (quantity > product.getQuantityAvailable()) {
            throw new IllegalArgumentException(
                    "Not enough stock. Current quantity is " + product.getQuantityAvailable() + "."
            );
        }

        product.deductStock(quantity);
    }

    public void discontinueProduct(Product product) {
        requireManagedProduct(product);
        requireActiveProduct(product);
        product.discontinueProduct();
    }

    public int getTotalProducts() {
        return products.size();
    }

    public int getTotalQuantity() {
        return products.stream()
                .mapToInt(Product::getQuantityAvailable)
                .sum();
    }

    public double getTotalInventoryValue() {
        return products.stream()
                .mapToDouble(Product::getInventoryValue)
                .sum();
    }

    public long getActiveProductCount() {
        return products.stream()
                .filter(Product::getStatus)
                .count();
    }

    private void validateNewProduct(Product product) {
        if (product.getItemNum() <= 0) {
            throw new IllegalArgumentException("Item number must be greater than 0.");
        }
        if (isItemNumberInUse(product.getItemNum())) {
            throw new IllegalArgumentException(
                    "Item number " + product.getItemNum() + " is already in use."
            );
        }
        if (product.getProductName() == null || product.getProductName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        if (product.getQuantityAvailable() < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be negative.");
        }
    }

    private void requireManagedProduct(Product product) {
        Objects.requireNonNull(product, "Product cannot be null.");

        if (!products.contains(product)) {
            throw new IllegalArgumentException("The selected product is not in the inventory.");
        }
    }

    private void requireActiveProduct(Product product) {
        if (!product.getStatus()) {
            throw new IllegalStateException("The selected product is discontinued.");
        }
    }
}
