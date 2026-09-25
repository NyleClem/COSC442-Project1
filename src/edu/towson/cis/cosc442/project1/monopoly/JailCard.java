package edu.towson.cis.cosc442.project1.monopoly;


public class JailCard extends Card {
    int type;
    
    /**
     * Constructs a JailCard with the specified card type.
     * @param cardType the integer representing the card type
     */
    public JailCard(int cardType) {
        type = cardType;
    }

    /**
     * Applies the jail action by sending the current player to jail.
     */
    public void applyAction() {
        Player currentPlayer = GameMaster.instance().getCurrentPlayer();
		GameMaster.instance().getGameBoard().queryCell("Jail");
		GameMaster.instance().sendToJail(currentPlayer);
    }

    /**
     * Retrieves the type of this jail card.
     * @return the integer representing the card type
     */
    public int getCardType() {
        return type;
    }

    /**
     * Returns the description of the jail card's action.
     * @return a string describing the jail card's action
     */
    public String getLabel() {
        return "Go to Jail immediately without collecting" +
        		" $200 when passing the GO cell";
    }
}
