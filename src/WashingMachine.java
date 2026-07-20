package src;

public class WashingMachine extends Product {
	private String loadType;
	private int capacity;
	private int spinSpeed;
	private int energyRating;
	
	public WashingMachine(String productName, int itemNum, double price, int quantityAvailable, String loadType, int capacity, int spinSpeed, int energyRating) {
		super(productName, itemNum, price, quantityAvailable);
		this.loadType = loadType;
		this.capacity = capacity;
		this.spinSpeed = spinSpeed;
		this.energyRating = energyRating;
	}

	public String getLoadType() {
		return loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSpinSpeed() {
		return spinSpeed;
	}

	public void setSpinSpeed(int spinSpeed) {
		this.spinSpeed = spinSpeed;
	}

	public int getEnergyRating() {
		return energyRating;
	}

	public void setEnergyRating(int energyRating) {
		this.energyRating = energyRating;
	}
	
	@Override
	public double calculateStockValue() {
		return getInventoryValue();
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"\nLoad Type (Front Load / Top Load): " + loadType +
				"\nCapacity (kg): " + capacity +
				"\nSpin Speed (RPM): " + spinSpeed +
				"\nEnergy Rating (1-5 Stars): " + energyRating;
	}
}