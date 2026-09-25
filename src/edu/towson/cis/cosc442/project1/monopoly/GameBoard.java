package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Hashtable;

public class GameBoard {

	private ArrayList<Cell> cells = new ArrayList<Cell>();
    private ArrayList<Card> chanceCards = new ArrayList<Card>();
	//the key of colorGroups is the name of the color group.
	private Hashtable<String, Integer> colorGroups = new Hashtable<String, Integer>();
	private ArrayList<Card> communityChestCards = new ArrayList<Card>();
	/**
	 * Constructs a new GameBoard with an initial 'Go' cell added.
	 */
	public GameBoard() {
		Cell go = new GoCell();
		addCell(go);
	}

    /**
     * Adds a Card to the appropriate deck based on its type.
     * @param card the Card to be added
     */
    public void addCard(Card card) {
        if(card.getCardType() == Card.TYPE_CC) {
            communityChestCards.add(card);
        } else {
            chanceCards.add(card);
        }
    }
	
	/**
	 * Adds a generic Cell to the game board.
	 * @param cell the Cell to add
	 */
	public void addCell(Cell cell) {
		cells.add(cell);
	}
	
	/**
	 * Adds a PropertyCell to the game board and updates the color group count.
	 * @param cell the PropertyCell to add
	 */
	public void addCell(PropertyCell cell) {
		String colorGroup = cell.getColorGroup();
		int propertyNumber = getPropertyNumberForColor(colorGroup);
		colorGroups.put(colorGroup, new Integer(propertyNumber + 1));
        cells.add(cell);
	}

    /**
     * Draws the top Community Chest card, cycles it to the bottom, and returns it.
     * @return the drawn Community Chest Card
     */
    public Card drawCCCard() {
        Card card = (Card)communityChestCards.get(0);
        communityChestCards.remove(0);
        addCard(card);
        return card;
    }

    /**
     * Draws the top Chance card, cycles it to the bottom, and returns it.
     * @return the drawn Chance Card
     */
    public Card drawChanceCard() {
        Card card = (Card)chanceCards.get(0);
        chanceCards.remove(0);
        addCard(card);
        return card;
    }

	/**
	 * Returns the Cell at the specified index on the game board.
	 * @param newIndex the index of the cell to retrieve
	 * @return the Cell at the specified index
	 */
	public Cell getCell(int newIndex) {
		return (Cell)cells.get(newIndex);
	}
	
	/**
	 * Returns the total number of cells on the game board.
	 * @return the number of cells on the board
	 */
	public int getCellNumber() {
		return cells.size();
	}
	
	/**
	 * Returns all PropertyCells belonging to a given color group.
	 * @param color the name of the color group
	 * @return an array of PropertyCells in the specified color group
	 */
	public PropertyCell[] getPropertiesInMonopoly(String color) {
		PropertyCell[] monopolyCells = 
			new PropertyCell[getPropertyNumberForColor(color)];
		int counter = 0;
		for (int i = 0; i < getCellNumber(); i++) {
			Cell c = getCell(i);
			if(c instanceof PropertyCell) {
				PropertyCell pc = (PropertyCell)c;
				if(pc.getColorGroup().equals(color)) {
					monopolyCells[counter] = pc;
					counter++;
				}
			}
		}
		return monopolyCells;
	}
	
	/**
	 * Returns the number of properties in a given color group.
	 * @param name the name of the color group
	 * @return the number of properties in that color group
	 */
	public int getPropertyNumberForColor(String name) {
		Integer number = (Integer)colorGroups.get(name);
		if(number != null) {
			return number.intValue();
		}
		return 0;
	}

	/**
	 * Finds and returns a Cell by its name.
	 * @param string the name of the cell to find
	 * @return the Cell with the specified name or null if not found
	 */
	public Cell queryCell(String string) {
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Finds and returns the index of a Cell by its name.
	 * @param string the name of the cell to find
	 * @return the index of the Cell or -1 if not found
	 */
	public int queryCellIndex(String string){
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return i;
			}
		}
		return -1;
	}

    /**
     * Clears all Community Chest cards from the game board.
     */
    public void removeCards() {
        communityChestCards.clear();
    }
}
