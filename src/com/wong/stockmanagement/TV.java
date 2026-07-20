package com.wong.stockmanagement;
public class TV extends Product {
	private String screenType;
	private String resolution;
	private double displaySize;
	
	public TV(String productName, int itemNum, double price, int quantityAvailable, String screenType, String resolution, double displaySize) {
		super(productName, itemNum, price, quantityAvailable);
		this.screenType = screenType;
		this.resolution = resolution;
		this.displaySize = displaySize;
	}

	public String getScreenType() {
		return screenType;
	}

	public String getResolution() {
		return resolution;
	}

	public double getDisplaySize() {
		return displaySize;
	}
	
	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setDisplaySize(double displaySize) {
		this.displaySize = displaySize;
	}
	
	@Override
	public double calculateStockValue() {
		return getInventoryValue();
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"\nScreen Type: " + screenType +
				"\nResolution: " + resolution +
				"\nDisplay Size: " + displaySize;
	}
}