package edu.towson.cis.cosc442.project1.monopoly;

public class UtilityCell extends Cell {

	public static final String COLOR_GROUP = "UTILITY";
	private static int PRICE;

	/**
	 * Sets the purchase price for all UtilityCell properties.
	 * @param price the new price to set for UtilityCell
	 */
	public static void setPrice(int price) {
		UtilityCell.PRICE = price;
	}

	/**
	 * Returns the purchase price of the UtilityCell property.
	 * @return the purchase price of the UtilityCell
	 */
	public int getPrice() {
		return UtilityCell.PRICE;
	}

	/**
	 * Calculates the rent owed based on the dice roll and number of utilities owned by the owner.
	 * @param diceRoll the value rolled on the dice used to calculate rent
	 * @return the rent amount to be paid
	 */
	public int getRent(int diceRoll) {
		if(theOwner.numberOfUtil() == 1) {
			return diceRoll * 4;
		} else if (theOwner.numberOfUtil() >= 2) {
			return diceRoll * 10;
		}
		return 0;
	}

	/**
	 * Executes the action when a player lands on this UtilityCell, including rent payment if owned by another player.
	 */
	public void playAction() {
		Player currentPlayer = null;
		if(!isAvailable()) {
			currentPlayer = GameMaster.instance().getCurrentPlayer();
			if(theOwner != currentPlayer) {
				GameMaster.instance().utilRollDice();
				int diceRoll = GameMaster.instance().getUtilDiceRoll();
				currentPlayer.payRentTo(theOwner, getRent(diceRoll));
			}
		}
	}
}
