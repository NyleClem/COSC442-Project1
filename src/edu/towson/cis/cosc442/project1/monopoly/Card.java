package edu.towson.cis.cosc442.project1.monopoly;

public abstract class Card {

    public static int TYPE_CHANCE = 1;
    public static int TYPE_CC = 2;

    /**
     * Returns the label or description text of this card.
     * @return the label text of the card
     */
    public abstract String getLabel();
    /**
     * Executes the action associated with this card when drawn.
     */
    public abstract void applyAction();
    /**
     * Returns the type identifier of this card, such as chance or community chest.
     * @return an integer representing the card type
     */
    public abstract int getCardType();
}
