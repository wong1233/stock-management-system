package com.wong.stockmanagement;

/**
 * Identifies every product category supported by the inventory system.
 */
public enum ProductType {
    REFRIGERATOR("Refrigerator"),
    TV("TV"),
    LAPTOP("Laptop"),
    WASHING_MACHINE("Washing Machine");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public static ProductType fromProduct(Product product) {
        if (product instanceof Refrigerator) {
            return REFRIGERATOR;
        }
        if (product instanceof TV) {
            return TV;
        }
        if (product instanceof Laptop) {
            return LAPTOP;
        }
        if (product instanceof WashingMachine) {
            return WASHING_MACHINE;
        }
        throw new InventoryException(
                "Unsupported product class: " + product.getClass().getSimpleName()
        );
    }

    @Override
    public String toString() {
        return displayName;
    }
}
