package com.wong.stockmanagement;
public abstract class Product {
	private String productName;
	private int itemNum;
	private double price;
	private int quantityAvailable;
	private boolean status = true;
	
	public Product() {
		this.productName = "";
		this.price = 0.0;
		this.quantityAvailable = 0;
		this.itemNum = 0;
		this.status = true;
	}
	
	public Product(String productName, int itemNum, double price, int quantityAvailable) {
		this.productName = productName;
		this.itemNum = itemNum;
		this.price = price;
		this.quantityAvailable = quantityAvailable;
	}
	
	public String getProductName () {
		return productName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
	
	public int getItemNum() {
		return itemNum;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public double getInventoryValue() {
		return price * quantityAvailable;
	}
	
	public void addStock(int quantity) {
		if (!status)
			System.out.println("Cannot add stock to a discontinued product!");
		
		else {
			if (quantity > 0) {
				this.quantityAvailable += quantity;
				System.out.println(quantity + " units added successfully! New quantity available: " + this.quantityAvailable);
			}
			else
				System.out.println("The quantity must be greater than 0!");
		}
	}
	
	public void deductStock(int quantity) {
		if (status) {
			if (quantity > 0) {
				if (quantity <= this.quantityAvailable) {
					quantityAvailable -= quantity;
					System.out.println(quantity + " units deducted successfully! New quantity available: " + this.quantityAvailable);
				}
				else
					System.out.println("Not enough stock! Current quantity available: " + this.quantityAvailable);	
			}
			else
				System.out.println("The quantity must be greater than 0!");
		}
		else
			System.out.println("Cannot deduct stock from a discontinued product!");
	}
	
	public void discontinueProduct() {
	    if (!status) {
	        System.out.println("The product is already discontinued.");
	    }
	    else {
	        status = false;
	        System.out.println("The product has been discontinued.");
	    }
	}
	
	public abstract double calculateStockValue();
	
	@Override
	public String toString() {
		return "Item Number: " + itemNum +
				"\nProduct Name: " + productName +
				"\nQuantity Available: " + quantityAvailable +
				"\nPrice (RM): " + price +
				"\nInventory Value (RM): " + getInventoryValue() +
				"\nProduct Status: " + (status ? "Active" : "Discontinued");
	}
}