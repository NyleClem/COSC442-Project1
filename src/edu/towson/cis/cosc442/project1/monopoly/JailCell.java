package edu.towson.cis.cosc442.project1.monopoly;

public class JailCell extends Cell {
	public static int BAIL = 50;
	
	/**
	 * Constructs a JailCell and sets its name to "Jail".
	 */
	public JailCell() {
		setName("Jail");
	}
	
	/**
	 * Defines the action to take when a player lands on the JailCell, currently with no effect.
	 */
	public void playAction() {
		
	}
}
