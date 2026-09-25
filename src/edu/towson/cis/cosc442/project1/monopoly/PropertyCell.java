package edu.towson.cis.cosc442.project1.monopoly;

public class PropertyCell extends Cell {
	private String colorGroup;
	private int housePrice;
	private int numHouses;
	private int rent;
	private int sellPrice;

	/**
	 * Returns the color group of this property.
	 * @return the color group associated with this property
	 */
	public String getColorGroup() {
		return colorGroup;
	}

	/**
	 * Returns the price to build a house on this property.
	 * @return the cost of purchasing a house for this property
	 */
	public int getHousePrice() {
		return housePrice;
	}

	/**
	 * Returns the current number of houses built on this property.
	 * @return the number of houses on the property
	 */
	public int getNumHouses() {
		return numHouses;
	}
    
    /**
     * Returns the selling price of this property.
     * @return the price at which this property can be sold
     */
    public int getPrice() {
		return sellPrice;
	}

	/**
	 * Calculates and returns the rent owed for this property considering monopolies and houses.
	 * @return the calculated rent amount to charge
	 */
	public int getRent() {
		int rentToCharge = rent;
		String [] monopolies = theOwner.getMonopolies();
		rentToCharge = calculateMonopoliesRent(rentToCharge, monopolies);
		if(numHouses > 0) {
			rentToCharge = rent * (numHouses + 1);
		}
		return rentToCharge;
	}

	/**
	 * Calculates adjusted rent if the property is part of a rent-enhancing monopoly group.
	 * @param rentToCharge the initial rent amount before monopoly adjustment
	 * @param monopolies an array of color groups that the owner holds as monopolies
	 * @return the rent amount potentially doubled if the property is in a monopoly group
	 */
	private int calculateMonopoliesRent(int rentToCharge, String[] monopolies) {
		for(int i = 0; i < monopolies.length; i++) {
			if(monopolies[i].equals(colorGroup)) {
				rentToCharge = rent * 2;
			}
		}
		return rentToCharge;
	}

	/**
	 * Executes the action when a player lands on this property, including paying rent if owned by another player.
	 */
	public void playAction() {
		Player currentPlayer = null;
		if(!isAvailable()) {
			currentPlayer = GameMaster.instance().getCurrentPlayer();
			if(theOwner != currentPlayer) {
				currentPlayer.payRentTo(theOwner, getRent());
			}
		}
	}

	/**
	 * Sets the color group for this property.
	 * @param colorGroup the color group to assign to this property
	 */
	public void setColorGroup(String colorGroup) {
		this.colorGroup = colorGroup;
	}

	/**
	 * Sets the price for purchasing a house on this property.
	 * @param housePrice the cost to build a house on this property
	 */
	public void setHousePrice(int housePrice) {
		this.housePrice = housePrice;
	}

	/**
	 * Sets the number of houses currently built on this property.
	 * @param numHouses the number of houses to set on this property
	 */
	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	/**
	 * Sets the selling price for this property.
	 * @param sellPrice the price at which this property can be sold
	 */
	public void setPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Sets the base rent amount for this property.
	 * @param rent the rent amount to assign to this property
	 */
	public void setRent(int rent) {
		this.rent = rent;
	}
}
