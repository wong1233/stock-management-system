# Stock Management System

A Java inventory-management application for refrigerators, TVs, laptops, and washing machines. The project began as a console application and is now being upgraded to a JavaFX desktop application with a visual dashboard and validated inventory workflows.

## Current Version

The active development branch is `javafx-ui`, which contains the JavaFX v2.0 application. The original console version is preserved on the `main` branch and tagged as `v1.0`.

## Features

### User Information

- Request the user's first name and surname before opening the dashboard.
- Generate a User ID using the existing `UserInfo` rules.
- Validate empty or incomplete names.
- Display the active user's name and User ID in the dashboard header.

### Inventory Dashboard

- View every product in a JavaFX `TableView`.
- Display item number, name, category, price, quantity, inventory value, and status.
- Automatically update total products, total units, active products, and total inventory value.
- Disable unavailable actions when no product is selected or a product is discontinued.

### Product Management

- Add refrigerators, TVs, laptops, and washing machines.
- Show different specification fields for each product category.
- Reject duplicate item numbers.
- View common information and category-specific specifications.
- Discontinue a product after confirmation.

### Stock Management

- Add stock to an active product.
- Deduct stock from an active product.
- Prevent quantities of zero or less.
- Prevent deductions greater than the available quantity.
- Prevent stock operations on discontinued products.

### Validation and Error Handling

- Validate required text fields and selections.
- Validate integer and decimal inputs.
- Reject invalid prices, quantities, capacities, and item numbers.
- Use `InventoryException` for inventory business-rule failures.
- Display validation failures through JavaFX error dialogs.

## Supported Product Categories

| Category | Additional information |
| --- | --- |
| Refrigerator | Door design, color, and capacity |
| TV | Screen type, resolution, and display size |
| Laptop | Color, processor, operating system, storage, graphics card, RAM, and screen size |
| Washing Machine | Load type, capacity, spin speed, and energy rating |

## Technologies

- Java 25
- JavaFX 25.0.3
- Maven
- IntelliJ IDEA
- Git and GitHub

## Project Structure

```text
src/com/wong/stockmanagement/
├── Product.java                 Base product class
├── Refrigerator.java            Refrigerator model
├── TV.java                      TV model
├── Laptop.java                  Laptop model
├── WashingMachine.java          Washing-machine model
├── ProductType.java             Supported product categories
├── InventoryManager.java        Inventory collection and business rules
├── InventoryException.java      Custom business exception
├── StockManagement.java         Original console application
├── StockManagementApp.java      JavaFX application and dashboard
├── ProductDialog.java           Product-creation form
├── StockQuantityDialog.java     Add-stock and deduct-stock form
├── ProductDetailsDialog.java    Read-only product details
├── UserInfoDialog.java          JavaFX user-information form
└── UserInfo.java                User-name and ID generation
```

## Requirements

- JDK 25
- IntelliJ IDEA with Maven support, or a Maven installation available from the command line

## Run with IntelliJ IDEA

1. Open the project folder in IntelliJ IDEA.
2. Select JDK 25 as the Project SDK.
3. Open the Maven tool window on the right side.
4. Reload the Maven project if necessary.
5. Expand `Plugins` and then `javafx`.
6. Double-click `javafx:run`.

The main JavaFX window should open with the title **Stock Management System**.

## Run from the Command Line

From the project directory, run:

```bash
mvn javafx:run
```

The JavaFX Maven plugin supplies the required JavaFX modules and native-access option.

## Basic Usage

1. Enter your first name and surname in the Welcome dialog.
2. Confirm the generated User ID and select **Continue**.
3. Select **Add Product** and choose a product category.
4. Complete the common information and category-specific fields.
5. Select a product from the inventory table.
6. Use **Add Stock**, **Deduct Stock**, **Discontinue**, or **View Details**.
7. Review the automatically updated summary cards at the bottom of the dashboard.

## Version History

### v1.0 — Console Version

- Add and display products through console menus.
- Add and deduct stock.
- Discontinue products.
- Calculate inventory values.
- Generate a user ID from the user's name.
- Validate console input.

### v2.0 — JavaFX Version (In Progress)

- Added Maven and JavaFX configuration.
- Refactored the source files into the `com.wong.stockmanagement` package.
- Added `InventoryManager` and `InventoryException`.
- Added the JavaFX dashboard, product table, and inventory summary cards.
- Added a validated Welcome dialog and User ID display.
- Added a validated product-creation dialog for all four categories.
- Added add-stock and deduct-stock dialogs.
- Added discontinue confirmation and a product-details dialog.

## Git Branches

- `main` — stable console application tagged as `v1.0`
- `javafx-ui` — JavaFX v2.0 development branch
