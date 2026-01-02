package entity;

import enums.*;

/**
 * The FlatType class represents a specific type of flat available in a housing project.
 * It encapsulates details about the flat type including its category (2-room, 3-room, etc.),
 * total number of units, available units, and selling price.
 * This class provides functionality to manage flat inventory and track availability.
 */
public class FlatType {

	/**
	 * Total number of units of this flat type in the project
	 */
	private int numUnits;
	/**
	 * Number of units currently available for booking
	 */
	private int availableUnits;
	/**
	 * The selling price of this flat type in SGD
	 */
	private double sellingPrice;
	/**
	 * The category of flat (TWO_ROOM, THREE_ROOM, etc.)
	 */
	private FlatTypeEnum type;

	/**
	 * Default constructor that initializes a FlatType with zero values and null type.
	 */
	public FlatType() {
		this.numUnits = 0;
		this.availableUnits = 0;
		this.sellingPrice = 0.0;
		this.type = null;
	}

	/**
	 * Full constructor that initializes a FlatType with all specified values.
	 * 
	 * @param numUnits Total number of units of this flat type
	 * @param availableUnits Number of units currently available for booking
	 * @param sellingPrice The selling price in SGD
	 * @param type The category of flat (TWO_ROOM, THREE_ROOM, etc.)
	 */
	public FlatType(int numUnits, int availableUnits, double sellingPrice, FlatTypeEnum type) {
		this.numUnits = numUnits;
		this.availableUnits = availableUnits;
		this.sellingPrice = sellingPrice;
		this.type = type;
	}

	/**
	 * Sets the total number of units of this flat type.
	 * 
	 * @param numUnits The new total number of units
	 */
	public void setNumUnits(int numUnits) {
		this.numUnits = numUnits;
	}

	/**
	 * Sets the number of units currently available for booking.
	 * 
	 * @param availableUnits The new number of available units
	 */
	public void setAvailableUnits(int availableUnits) {
		this.availableUnits = availableUnits;
	}

	/**
	 * Sets the selling price for this flat type.
	 * 
	 * @param sellingPrice The new selling price in SGD
	 */
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * Sets the category of this flat type.
	 * 
	 * @param type The new flat type category
	 */
	public void setType(FlatTypeEnum type) {
		this.type = type;
	}

	/**
	 * Gets the category of this flat type.
	 * 
	 * @return The flat type category
	 */
	public FlatTypeEnum getType() {
		return this.type;
	}

	/**
	 * Gets the total number of units of this flat type.
	 * 
	 * @return The total number of units
	 */
	public int getNumUnits() {
		return this.numUnits;
	}

	/**
	 * Gets the number of units currently available for booking.
	 * 
	 * @return The number of available units
	 */
	public int getAvailableUnits() {
		return this.availableUnits;
	}

	/**
	 * Gets the selling price of this flat type.
	 * 
	 * @return The selling price in SGD
	 */
	public double getSellingPrice() {
		return this.sellingPrice;
	}

	/**
	 * Decreases the available units count by one when a flat is booked.
	 * Only decreases if there are units available.
	 * 
	 * @return true if the count was decreased, false if no units are available
	 */
	public boolean decreaseAvailableUnits() {
		if (this.availableUnits > 0) {
			this.availableUnits--;
			return true;
		}
		return false;
	}

	/**
	 * Increases the available units count by one when a booking is cancelled.
	 * Only increases if the available count is less than the total count.
	 * 
	 * @return true if the count was increased, false if already at maximum
	 */
	public boolean increaseAvailableUnits() {
		if (this.availableUnits < this.numUnits) {
			this.availableUnits++;
			return true;
		}
		return false;
	}

	/**
	 * Creates a string representation of this flat type.
	 * 
	 * @return A string containing flat type details
	 */
	@Override()
	public String toString() {
		return "FlatType [type=" + type + ", numUnits=" + numUnits + ", availableUnits=" + availableUnits + ", sellingPrice=" + sellingPrice + "]";
	}
}