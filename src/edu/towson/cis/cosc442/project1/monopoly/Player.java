package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class Player {
	//the key of colorGroups is the name of the color group.
	private Hashtable<String, Integer> colorGroups = new Hashtable<String, Integer>();
	private boolean inJail;
	private int money;
	private String name;

	private Cell position;
	private ArrayList<PropertyCell> properties = new ArrayList<PropertyCell>();
	private ArrayList<Cell> railroads = new ArrayList<Cell>();
	private ArrayList<Cell> utilities = new ArrayList<Cell>();
	
	/**
	 * Creates a new Player positioned at the 'Go' cell with default settings.
	 */
	public Player() {
		GameBoard gb = GameMaster.instance().getGameBoard();
		inJail = false;
		if(gb != null) {
			position = gb.queryCell("Go");
		}
	}

    /**
     * Assigns ownership of the specified property to the player and deducts the purchase amount from their money.
     * @param property The property cell to buy.
     * @param amount The amount of money to deduct for the purchase.
     */
    public void buyProperty(Cell property, int amount) {
        property.setTheOwner(this);
        if(property instanceof PropertyCell) {
            PropertyCell cell = (PropertyCell)property;
            properties.add(cell);
            colorGroups.put(
                    cell.getColorGroup(), 
                    new Integer(getPropertyNumberForColor(cell.getColorGroup())+1));
        }
        if(property instanceof RailRoadCell) {
            railroads.add(property);
            colorGroups.put(
                    RailRoadCell.COLOR_GROUP, 
                    new Integer(getPropertyNumberForColor(RailRoadCell.COLOR_GROUP)+1));
        }
        if(property instanceof UtilityCell) {
            utilities.add(property);
            colorGroups.put(
                    UtilityCell.COLOR_GROUP, 
                    new Integer(getPropertyNumberForColor(UtilityCell.COLOR_GROUP)+1));
        }
        setMoney(getMoney() - amount);
    }
	
	/**
	 * Determines if the player can buy houses based on owning any monopolies.
	 * @return True if the player owns at least one monopoly, false otherwise.
	 */
	public boolean canBuyHouse() {
		return (getMonopolies().length != 0);
	}

	/**
	 * Checks if the player owns a property with the specified name.
	 * @param property The name of the property to check.
	 * @return True if the player owns the property, false otherwise.
	 */
	public boolean checkProperty(String property) {
		for(int i=0;i<properties.size();i++) {
			Cell cell = (Cell)properties.get(i);
			if(cell.getName().equals(property)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Transfers all owned properties to the specified player or resets them if null.
	 * @param player The player to receive the properties; null to reset ownership.
	 */
	public void exchangeProperty(Player player) {
		for(int i = 0; i < getPropertyNumber(); i++ ) {
			PropertyCell cell = getProperty(i);
			cell.setTheOwner(player);
			if(player == null) {
				cell.setAvailable(true);
				cell.setNumHouses(0);
			}
			else {
				player.properties.add(cell);
				colorGroups.put(
						cell.getColorGroup(), 
						new Integer(getPropertyNumberForColor(cell.getColorGroup())+1));
			}
		}
		properties.clear();
	}
    
    /**
     * Retrieves all properties, railroads, and utilities owned by the player.
     * @return An array of all property cells owned by the player.
     */
    public Cell[] getAllProperties() {
        ArrayList<Cell> list = new ArrayList<Cell>();
        list.addAll(properties);
        list.addAll(utilities);
        list.addAll(railroads);
        return (Cell[])list.toArray(new Cell[list.size()]);
    }

	/**
	 * Returns the current amount of money the player has.
	 * @return The player's current money balance.
	 */
	public int getMoney() {
		return this.money;
	}
	
	/**
	 * Finds all color groups for which the player owns all properties.
	 * @return An array of color group names where the player has a monopoly.
	 */
	public String[] getMonopolies() {
		ArrayList<String> monopolies = new ArrayList<String>();
		Enumeration<String> colors = colorGroups.keys();
		while(colors.hasMoreElements()) {
			String color = colors.nextElement();
            if(isMonopolyColorGroup(color)) {
    			Integer num = colorGroups.get(color);
    			GameBoard gameBoard = GameMaster.instance().getGameBoard();
    			if(num.intValue() == gameBoard.getPropertyNumberForColor(color)) {
    				monopolies.add(color);
    			}
            }
		}
		return (String[])monopolies.toArray(new String[monopolies.size()]);
	}

	/**
	 * Checks if a color group qualifies as a monopoly color group, excluding railroads and utilities.
	 * @param color The color group name to check.
	 * @return True if the group is a valid monopoly color group, false otherwise.
	 */
	private boolean isMonopolyColorGroup(String color) {
		return !(color.equals(RailRoadCell.COLOR_GROUP)) && !(color.equals(UtilityCell.COLOR_GROUP));
	}

	/**
	 * Returns the player's name.
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Pays the bail to get out of jail and updates the player's status accordingly.
	 */
	public void getOutOfJail() {
		money -= JailCell.BAIL;
		if(isBankrupt()) {
			money = 0;
			exchangeProperty(null);
		}
		inJail = false;
		GameMaster.instance().updateGUI();
	}

	/**
	 * Gets the current position of the player on the game board.
	 * @return The cell where the player is currently located.
	 */
	public Cell getPosition() {
		return this.position;
	}
	
	/**
	 * Gets the player's property at the specified index.
	 * @param index The index of the property to retrieve.
	 * @return The PropertyCell at the specified index.
	 */
	public PropertyCell getProperty(int index) {
		return (PropertyCell)properties.get(index);
	}
	
	/**
	 * Returns the number of properties owned by the player.
	 * @return The count of properties the player owns.
	 */
	public int getPropertyNumber() {
		return properties.size();
	}

	/**
	 * Returns the count of properties owned in a specific color group.
	 * @param name The name of the color group.
	 * @return The number of properties owned in the specified color group.
	 */
	private int getPropertyNumberForColor(String name) {
		Integer number = (Integer)colorGroups.get(name);
		if(number != null) {
			return number.intValue();
		}
		return 0;
	}

	/**
	 * Checks if the player is bankrupt based on their money.
	 * @return True if the player's money is zero or less, false otherwise.
	 */
	public boolean isBankrupt() {
		return money <= 0;
	}

	/**
	 * Indicates whether the player is currently in jail.
	 * @return True if the player is in jail, false otherwise.
	 */
	public boolean isInJail() {
		return inJail;
	}

	/**
	 * Returns the number of railroad properties owned by the player.
	 * @return The count of railroads owned.
	 */
	public int numberOfRR() {
		return getPropertyNumberForColor(RailRoadCell.COLOR_GROUP);
	}

	/**
	 * Returns the number of utility properties owned by the player.
	 * @return The count of utilities owned.
	 */
	public int numberOfUtil() {
		return getPropertyNumberForColor(UtilityCell.COLOR_GROUP);
	}
	
	/**
	 * Pays rent to another player, adjusting both players' money and handling bankruptcy if necessary.
	 * @param owner The player to whom rent is paid.
	 * @param rentValue The amount of rent to pay.
	 */
	public void payRentTo(Player owner, int rentValue) {
		if(money < rentValue) {
			owner.money += money;
			money -= rentValue;
		}
		else {
			money -= rentValue;
			owner.money +=rentValue;
		}
		if(isBankrupt()) {
			money = 0;
			exchangeProperty(owner);
		}
	}
	
	/**
	 * Attempts to purchase the property on the current position if it is available.
	 */
	public void purchase() {
		if(getPosition().isAvailable()) {
			Cell c = getPosition();
			c.setAvailable(false);
			if(c instanceof PropertyCell) {
				PropertyCell cell = (PropertyCell)c;
				purchaseProperty(cell);
			}
			if(c instanceof RailRoadCell) {
				RailRoadCell cell = (RailRoadCell)c;
				purchaseRailRoad(cell);
			}
			if(c instanceof UtilityCell) {
				UtilityCell cell = (UtilityCell)c;
				purchaseUtility(cell);
			}
		}
	}
	
	/**
	 * Buys a specified number of houses for all properties in a selected monopoly if the player has enough money.
	 * @param selectedMonopoly The monopoly color group to buy houses in.
	 * @param houses The number of houses to purchase per property.
	 */
	public void purchaseHouse(String selectedMonopoly, int houses) {
		GameBoard gb = GameMaster.instance().getGameBoard();
		PropertyCell[] cells = gb.getPropertiesInMonopoly(selectedMonopoly);
		if((money >= (cells.length * (cells[0].getHousePrice() * houses)))) {
			for(int i = 0; i < cells.length; i++) {
				int newNumber = cells[i].getNumHouses() + houses;
				if (newNumber <= 5) {
					cells[i].setNumHouses(newNumber);
					this.setMoney(money - (cells[i].getHousePrice() * houses));
					GameMaster.instance().updateGUI();
				}
			}
		}
	}
	
	/**
	 * Purchases a specified property cell and updates ownership and money.
	 * @param cell The property cell to purchase.
	 */
	private void purchaseProperty(PropertyCell cell) {
        buyProperty(cell, cell.getPrice());
	}

	/**
	 * Purchases a specified railroad cell and updates ownership and money.
	 * @param cell The railroad cell to purchase.
	 */
	private void purchaseRailRoad(RailRoadCell cell) {
	    buyProperty(cell, cell.getPrice());
	}

	/**
	 * Purchases a specified utility cell and updates ownership and money.
	 * @param cell The utility cell to purchase.
	 */
	private void purchaseUtility(UtilityCell cell) {
	    buyProperty(cell, cell.getPrice());
	}

    /**
     * Sells a specified property and adds the given amount to the player's money.
     * @param property The property cell to sell.
     * @param amount The amount of money gained from selling.
     */
    public void sellProperty(Cell property, int amount) {
        property.setTheOwner(null);
        if(property instanceof PropertyCell) {
            properties.remove(property);
        }
        if(property instanceof RailRoadCell) {
            railroads.remove(property);
        }
        if(property instanceof UtilityCell) {
            utilities.remove(property);
        }
        setMoney(getMoney() + amount);
    }

	/**
	 * Sets the player's jail status.
	 * @param inJail True to set the player in jail, false otherwise.
	 */
	public void setInJail(boolean inJail) {
		this.inJail = inJail;
	}

	/**
	 * Sets the player's money to the specified amount.
	 * @param money The new money amount.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * Sets the player's name.
	 * @param name The new name of the player.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Updates the player's current position on the board.
	 * @param newPosition The new cell position of the player.
	 */
	public void setPosition(Cell newPosition) {
		this.position = newPosition;
	}

    /**
     * Returns the string representation of the player, which is their name.
     * @return The player's name as a string.
     */
    public String toString() {
        return name;
    }
    
    /**
     * Resets all property, railroad, and utility lists owned by the player to empty.
     */
    public void resetProperty() {
    	properties = new ArrayList<PropertyCell>();
    	railroads = new ArrayList<Cell>();
    	utilities = new ArrayList<Cell>();
	}
}
