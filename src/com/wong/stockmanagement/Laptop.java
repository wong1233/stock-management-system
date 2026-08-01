package com.wong.stockmanagement;
public class Laptop extends Product {
	private String color;
	private String processor;
	private String operatingSystem;
	private int storageSize;
	private String storageType;
	private String graphicsCard;
	private int ramSize;
	private double screenSize;
	
	public Laptop(String productName, int itemNum, double price, int quantityAvailable, String color, String processor, String operatingSystem, int storageSize, String storageType, String graphicsCard, int ramSize, double screenSize) {
		super(productName, itemNum, price, quantityAvailable);
		this.color = color;
		this.processor = processor;
		this.operatingSystem = operatingSystem;
		this.storageSize = storageSize;
		this.storageType = storageType;
		this.graphicsCard = graphicsCard;
		this.ramSize = ramSize;
		this.screenSize = screenSize;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}
	
	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getGraphicsCard() {
		return graphicsCard;
	}

	public void setGraphicsCard(String graphicsCard) {
		this.graphicsCard = graphicsCard;
	}

	public int getRamSize() {
		return ramSize;
	}

	public void setRamSize(int ramSize) {
		this.ramSize = ramSize;
	}

	public double getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(double screenSize) {
		this.screenSize = screenSize;
	}
	
	@Override
	public double calculateStockValue() {
		return getInventoryValue();
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"\nColor: " + color +
				"\nProcessor: " + processor +
				"\nOperating System: " + operatingSystem +
				"\nStorage Size (GB): " + storageSize +
				"\nStorage Type: " + storageType +
				"\nGraphic Card: " + graphicsCard +
				"\nRAM Size (GB): " + ramSize +
				"\nScreen Size (inch): " + screenSize;
	}
}