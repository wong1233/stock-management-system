# Stock Management System

A Java application for managing refrigerators, TVs, laptops, and washing machines. The project began as a console application and is currently being upgraded with a JavaFX user interface.

## Versions

### v1.0 — Console Version

- Add and view products.
- Add and deduct stock.
- Discontinue products.
- Calculate inventory value.
- Generate a user ID from the user's name.
- Validate console input.

### v2.0 — JavaFX Version (In Progress)

- Added Maven and JavaFX configuration.
- Refactored source files into the `com.wong.stockmanagement` package.
- Added `InventoryManager` for product and stock operations.
- Added duplicate item-number validation.
- Added the custom `InventoryException` for business-rule failures.
- Added the JavaFX inventory dashboard.
- Added the product `TableView`.
- Added inventory summary cards.
- Added selection-aware inventory action buttons.
- Product creation and stock-operation dialogs are under development.

## Technologies

- Java 25
- JavaFX 25.0.3
- Maven
- IntelliJ IDEA
- Git and GitHub

## Project Structure

```text
Product
├── Refrigerator
├── TV
├── Laptop
└── WashingMachine

StockManagement       Console application
StockManagementApp    JavaFX application
InventoryManager      Inventory business logic
InventoryException    Custom inventory exception
UserInfo              User name and ID generation
```

## Run the Project

### Requirements

- JDK 25
- Maven

### JavaFX Version

In IntelliJ IDEA, open the Maven tool window and run:

```text
Plugins → javafx → javafx:run
```

If Maven is available from the command line, run:

```bash
mvn javafx:run
```

### Console Version

Run the `StockManagement` main class from IntelliJ IDEA.

## Git Branches

- `main` — stable console version tagged as `v1.0`.
- `javafx-ui` — JavaFX `v2.0` development branch.
