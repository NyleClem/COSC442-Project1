package edu.towson.cis.cosc442.project1.monopoly;

public class CardCell extends Cell {
    private int type;
    
    /**
     * Constructs a CardCell with the specified type and name.
     * @param type the type identifier for this CardCell
     * @param name the name of this CardCell
     */
    public CardCell(int type, String name) {
        setName(name);
        this.type = type;
    }
    
    /**
     * Executes the action associated with this CardCell, currently with no effect.
     */
    public void playAction() {
    }
    
    /**
     * Returns the type identifier of this CardCell.
     * @return the integer type of this CardCell
     */
    public int getType() {
        return type;
    }
}
