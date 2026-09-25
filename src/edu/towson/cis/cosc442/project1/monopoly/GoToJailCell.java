package edu.towson.cis.cosc442.project1.monopoly;

public class GoToJailCell extends Cell {
	
	/**
	 * Constructs a GoToJailCell object and sets its name to 'Go to Jail'.
	 */
	public GoToJailCell() {
		setName("Go to Jail");
	}

	/**
	 * Performs the action of sending the current player to jail by invoking the jail cell and updating the game state accordingly.
	 */
	public void playAction() {
		Player currentPlayer = GameMaster.instance().getCurrentPlayer();
		GameMaster.instance().getGameBoard().queryCell("Jail");
		GameMaster.instance().sendToJail(currentPlayer);
	}
}
