package com.wong.stockmanagement;

/**
 * Represents a validation or business-rule failure in the inventory workflow.
 */
public class InventoryException extends RuntimeException {
    public InventoryException(String message) {
        super(message);
    }
}
