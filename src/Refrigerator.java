package src;
public class Refrigerator extends Product {
	private String doorDesign;
	private String color;
	private double capacity;
	
	public Refrigerator(String productName, int itemNum, double price, int quantityAvailable, String doorDesign, String color, double capacity) {
		super(productName, itemNum, price, quantityAvailable);
		this.doorDesign = doorDesign;
		this.color = color;
		this.capacity = capacity;
	}
	
	public String getDoorDesign() {
		return doorDesign;
	}
	
	public String getColor() {
		return color;
	}
	
	public double getCapacity() {
		return capacity;
	}
	
	public void setDoorDesign(String doorDesign) {
		this.doorDesign = doorDesign;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public double calculateStockValue() {
		return getInventoryValue();
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"\nDoor Design: " + doorDesign +
				"\nColor: " + color +
				"\nCapacity (in Litres): " + capacity;
	}
}