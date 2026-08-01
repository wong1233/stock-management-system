package com.wong.stockmanagement;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Saves and loads inventory data using a small versioned binary format.
 */
public final class InventoryStorage {
    private static final int FILE_MAGIC = 0x534D5332;
    private static final int FILE_VERSION = 1;
    private static final int MAX_PRODUCT_COUNT = 100_000;

    private final Path storageFile;

    public InventoryStorage(Path storageFile) {
        this.storageFile = Objects.requireNonNull(storageFile, "storageFile");
    }

    public static InventoryStorage createDefault() {
        String userHome = System.getProperty("user.home");
        Path storageDirectory = Path.of(userHome, ".stock-management-system");
        return new InventoryStorage(storageDirectory.resolve("inventory.dat"));
    }

    public Path getStorageFile() {
        return storageFile;
    }

    public List<Product> loadProducts() {
        if (Files.notExists(storageFile)) {
            return List.of();
        }

        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(storageFile))
        )) {
            validateHeader(input);

            int productCount = input.readInt();
            if (productCount < 0 || productCount > MAX_PRODUCT_COUNT) {
                throw new IOException("Invalid product count: " + productCount);
            }

            List<Product> products = new ArrayList<>(productCount);
            for (int index = 0; index < productCount; index++) {
                products.add(readProduct(input));
            }
            return products;
        } catch (EOFException exception) {
            throw new InventoryException(
                    "The saved inventory file is incomplete or damaged.",
                    exception
            );
        } catch (IOException exception) {
            throw new InventoryException(
                    "Unable to load inventory from " + storageFile + ".",
                    exception
            );
        }
    }

    public void saveProducts(List<? extends Product> products) {
        Objects.requireNonNull(products, "products");

        Path absoluteFile = storageFile.toAbsolutePath();
        Path parentDirectory = absoluteFile.getParent();
        Path temporaryFile = absoluteFile.resolveSibling(
                absoluteFile.getFileName() + ".tmp"
        );

        try {
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            try (DataOutputStream output = new DataOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(temporaryFile))
            )) {
                output.writeInt(FILE_MAGIC);
                output.writeInt(FILE_VERSION);
                output.writeInt(products.size());

                for (Product product : products) {
                    writeProduct(output, product);
                }
            }

            moveIntoPlace(temporaryFile, absoluteFile);
        } catch (IOException exception) {
            deleteTemporaryFile(temporaryFile);
            throw new InventoryException(
                    "Unable to save inventory to " + absoluteFile + ".",
                    exception
            );
        }
    }

    private void validateHeader(DataInputStream input) throws IOException {
        int magic = input.readInt();
        if (magic != FILE_MAGIC) {
            throw new IOException("Unsupported inventory file format.");
        }

        int version = input.readInt();
        if (version != FILE_VERSION) {
            throw new IOException("Unsupported inventory file version: " + version);
        }
    }

    private Product readProduct(DataInputStream input) throws IOException {
        ProductType productType;
        try {
            productType = ProductType.valueOf(input.readUTF());
        } catch (IllegalArgumentException exception) {
            throw new IOException("Unsupported product type in inventory file.", exception);
        }

        String productName = input.readUTF();
        int itemNumber = input.readInt();
        double price = input.readDouble();
        int quantity = input.readInt();
        boolean status = input.readBoolean();

        Product product = switch (productType) {
            case REFRIGERATOR -> new Refrigerator(
                    productName,
                    itemNumber,
                    price,
                    quantity,
                    input.readUTF(),
                    input.readUTF(),
                    input.readDouble()
            );
            case TV -> new TV(
                    productName,
                    itemNumber,
                    price,
                    quantity,
                    input.readUTF(),
                    input.readUTF(),
                    input.readDouble()
            );
            case LAPTOP -> new Laptop(
                    productName,
                    itemNumber,
                    price,
                    quantity,
                    input.readUTF(),
                    input.readUTF(),
                    input.readUTF(),
                    input.readInt(),
                    input.readUTF(),
                    input.readUTF(),
                    input.readInt(),
                    input.readDouble()
            );
            case WASHING_MACHINE -> new WashingMachine(
                    productName,
                    itemNumber,
                    price,
                    quantity,
                    input.readUTF(),
                    input.readInt(),
                    input.readInt(),
                    input.readInt()
            );
        };

        product.setStatus(status);
        return product;
    }

    private void writeProduct(DataOutputStream output, Product product) throws IOException {
        ProductType productType = ProductType.fromProduct(product);
        output.writeUTF(productType.name());
        output.writeUTF(product.getProductName());
        output.writeInt(product.getItemNum());
        output.writeDouble(product.getPrice());
        output.writeInt(product.getQuantityAvailable());
        output.writeBoolean(product.getStatus());

        switch (productType) {
            case REFRIGERATOR -> writeRefrigerator(output, (Refrigerator) product);
            case TV -> writeTv(output, (TV) product);
            case LAPTOP -> writeLaptop(output, (Laptop) product);
            case WASHING_MACHINE -> writeWashingMachine(output, (WashingMachine) product);
        }
    }

    private void writeRefrigerator(
            DataOutputStream output,
            Refrigerator refrigerator
    ) throws IOException {
        output.writeUTF(refrigerator.getDoorDesign());
        output.writeUTF(refrigerator.getColor());
        output.writeDouble(refrigerator.getCapacity());
    }

    private void writeTv(DataOutputStream output, TV tv) throws IOException {
        output.writeUTF(tv.getScreenType());
        output.writeUTF(tv.getResolution());
        output.writeDouble(tv.getDisplaySize());
    }

    private void writeLaptop(DataOutputStream output, Laptop laptop) throws IOException {
        output.writeUTF(laptop.getColor());
        output.writeUTF(laptop.getProcessor());
        output.writeUTF(laptop.getOperatingSystem());
        output.writeInt(laptop.getStorageSize());
        output.writeUTF(laptop.getStorageType());
        output.writeUTF(laptop.getGraphicsCard());
        output.writeInt(laptop.getRamSize());
        output.writeDouble(laptop.getScreenSize());
    }

    private void writeWashingMachine(
            DataOutputStream output,
            WashingMachine washingMachine
    ) throws IOException {
        output.writeUTF(washingMachine.getLoadType());
        output.writeInt(washingMachine.getCapacity());
        output.writeInt(washingMachine.getSpinSpeed());
        output.writeInt(washingMachine.getEnergyRating());
    }

    private void moveIntoPlace(Path temporaryFile, Path destination) throws IOException {
        try {
            Files.move(
                    temporaryFile,
                    destination,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(
                    temporaryFile,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    private void deleteTemporaryFile(Path temporaryFile) {
        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException ignored) {
            // Preserve the original save failure.
        }
    }
}
