package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Iterator;


public class GameMaster {

	private static GameMaster gameMaster;
	static final public int MAX_PLAYER = 8;	
	private Die[] dice;
	private GameBoard gameBoard;
	private MonopolyGUI gui;
	private int initAmountOfMoney;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int turn = 0;
	private int utilDiceRoll;
	private boolean testMode;

	/**
	 * Returns the singleton instance of the GameMaster class.
	 * @return The single GameMaster instance.
	 */
	public static GameMaster instance() {
		if(gameMaster == null) {
			gameMaster = new GameMaster();
		}
		return gameMaster;
	}

	/**
	 * Constructs a GameMaster object initializing default money and dice.
	 */
	public GameMaster() {
		initAmountOfMoney = 1500;
		dice = new Die[]{new Die(), new Die()};
	}

    /**
     * Handles the event when the buy house button is clicked by showing the buy house dialog for the current player.
     */
    public void btnBuyHouseClicked() {
        gui.showBuyHouseDialog(getCurrentPlayer());
    }

    /**
     * Handles the event when the draw card button is clicked by drawing a Community Chest or Chance card, applying its action, and enabling the end turn button.
     * @return The drawn Card object.
     */
    public Card btnDrawCardClicked() {
        gui.setDrawCardEnabled(false);
        CardCell cell = (CardCell)getCurrentPlayer().getPosition();
        Card card = null;
        if(cell.getType() == Card.TYPE_CC) {
            card = getGameBoard().drawCCCard();
            card.applyAction();
        } else {
            card = getGameBoard().drawChanceCard();
            card.applyAction();
        }
        gui.setEndTurnEnabled(true);
        return card;
    }

    /**
     * Handles the event when the end turn button is clicked by executing the current player's position action, checking bankruptcy, and switching turns if necessary.
     */
    public void btnEndTurnClicked() {
		setAllButtonEnabled(false);
		getCurrentPlayer().getPosition().playAction();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
			updateGUI();
		}
		else {
			switchTurn();
			updateGUI();
		}
    }

    /**
     * Processes the current player's attempt to get out of jail and updates GUI buttons according to the player's bankruptcy and jail status.
     */
    public void btnGetOutOfJailClicked() {
		getCurrentPlayer().getOutOfJail();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
		}
		else {
			gui.setRollDiceEnabled(true);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
			gui.setGetOutOfJailEnabled(getCurrentPlayer().isInJail());
		}
    }

    /**
     * Handles the event when the purchase property button is clicked by having the current player purchase their position and updating the GUI.
     */
    public void btnPurchasePropertyClicked() {
        Player player = getCurrentPlayer();
		player.purchase();
		gui.setPurchasePropertyEnabled(false);
		updateGUI();
    }
    
    /**
     * Handles the event when the roll dice button is clicked by rolling dice, moving the current player, and updating GUI buttons accordingly.
     */
    public void btnRollDiceClicked() {
		int[] rolls = rollDice();
		if((rolls[0]+rolls[1]) > 0) {
			Player player = getCurrentPlayer();
			gui.setRollDiceEnabled(false);
			StringBuffer msg = new StringBuffer();
			msg.append(player.getName())
					.append(", you rolled ")
					.append(rolls[0])
					.append(" and ")
					.append(rolls[1]);
			gui.showMessage(msg.toString());
			movePlayer(player, rolls[0] + rolls[1]);
			gui.setBuyHouseEnabled(false);
		}
    }

    /**
     * Handles the event when the trade button is clicked by opening a trade dialog, processing the trade deal, and updating the GUI.
     */
    public void btnTradeClicked() {
        TradeDialog dialog = gui.openTradeDialog();
        TradeDeal deal = dialog.getTradeDeal();
        if(deal != null) {
            RespondDialog rDialog = gui.openRespondDialog(deal);
            if(rDialog.getResponse()) {
                completeTrade(deal);
                updateGUI();
            }
        }
    }

    /**
     * Completes a trade deal by making the seller sell the property and the current player buy it at the agreed amount.
     * @param deal The TradeDeal object containing trade details.
     */
    public void completeTrade(TradeDeal deal) {
        Player seller = getPlayer(deal.getPlayerIndex());
        Cell property = gameBoard.queryCell(deal.getPropertyName());
        seller.sellProperty(property, deal.getAmount());
        getCurrentPlayer().buyProperty(property, deal.getAmount());
    }

    /**
     * Draws a Community Chest card from the game board.
     * @return The drawn Community Chest Card object.
     */
    public Card drawCCCard() {
        return gameBoard.drawCCCard();
    }

    /**
     * Draws a Chance card from the game board.
     * @return The drawn Chance Card object.
     */
    public Card drawChanceCard() {
        return gameBoard.drawChanceCard();
    }

	
	/**
	 * Retrieves the player whose turn it currently is.
	 * @return The current Player object.
	 */
	public Player getCurrentPlayer() {
		return getPlayer(turn);
	}
    
    /**
     * Gets the index of the current player in the players list.
     * @return Current player's index as an integer.
     */
    public int getCurrentPlayerIndex() {
        return turn;
    }

	/**
	 * Returns the game board instance used in the game.
	 * @return The GameBoard object.
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

    /**
     * Returns the GUI associated with the game master.
     * @return The MonopolyGUI object.
     */
    public MonopolyGUI getGUI() {
        return gui;
    }

	/**
	 * Returns the initial amount of money assigned to each player.
	 * @return Initial money amount as an integer.
	 */
	public int getInitAmountOfMoney() {
		return initAmountOfMoney;
	}
	
	/**
	 * Returns the total number of players in the current game.
	 * @return The number of players as an integer.
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}

    /**
     * Returns the number of players excluding the current player, representing potential sellers in a trade.
     * @return Number of sellers as an integer.
     */
    public int getNumberOfSellers() {
        return players.size() - 1;
    }

	/**
	 * Retrieves the Player object at the specified index.
	 * @param index The index of the player to retrieve.
	 * @return The Player object at the given index.
	 */
	public Player getPlayer(int index) {
		return (Player)players.get(index);
	}
	
	/**
	 * Finds the index of a given player in the players list.
	 * @param player The Player whose index is to be found.
	 * @return The index of the player as an integer, or -1 if not found.
	 */
	public int getPlayerIndex(Player player) {
		return players.indexOf(player);
	}

    /**
     * Returns a list of all players except the current player, representing potential sellers.
     * @return ArrayList of Player objects who are sellers.
     */
    public ArrayList<Player> getSellerList() {
        ArrayList<Player> sellers = new ArrayList<Player>();
        for (Iterator<Player> iter = players.iterator(); iter.hasNext();) {
            Player player = (Player) iter.next();
            if(player != getCurrentPlayer()) sellers.add(player);
        }
        return sellers;
    }

	/**
	 * Returns the current turn index.
	 * @return The current turn index as an integer.
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Returns the utility dice roll value stored in the game master.
	 * @return The utility dice roll as an integer.
	 */
	public int getUtilDiceRoll() {
		return this.utilDiceRoll;
	}

	/**
	 * Moves the player at the specified index by a given dice value.
	 * @param playerIndex Index of the player to move.
	 * @param diceValue The number of steps to move the player.
	 */
	public void movePlayer(int playerIndex, int diceValue) {
		Player player = (Player)players.get(playerIndex);
		movePlayer(player, diceValue);
	}
	
	/**
	 * Moves a specified player by a given dice value, handles passing 'Go', updates position and GUI, and triggers player moved actions.
	 * @param player The Player to be moved.
	 * @param diceValue Number of spaces to move the player.
	 */
	public void movePlayer(Player player, int diceValue) {
		Cell currentPosition = player.getPosition();
		int positionIndex = gameBoard.queryCellIndex(currentPosition.getName());
		int newIndex = (positionIndex+diceValue)%gameBoard.getCellNumber();
		if(newIndex <= positionIndex || diceValue > gameBoard.getCellNumber()) {
			player.setMoney(player.getMoney() + 200);
		}
		player.setPosition(gameBoard.getCell(newIndex));
		gui.movePlayer(getPlayerIndex(player), positionIndex, newIndex);
		playerMoved(player);
		updateGUI();
	}

	/**
	 * Performs actions and GUI updates after a player has moved, enabling appropriate buttons based on the new position.
	 * @param player The Player who has moved.
	 */
	public void playerMoved(Player player) {
		Cell cell = player.getPosition();
		int playerIndex = getPlayerIndex(player);
		if(cell instanceof CardCell) {
		    gui.setDrawCardEnabled(true);
		} else{
			if(cell.isAvailable()) {
				int price = cell.getPrice();
				if(price <= player.getMoney() && price > 0) {
					gui.enablePurchaseBtn(playerIndex);
				}
			}	
			gui.enableEndTurnBtn(playerIndex);
		}
        gui.setTradeEnabled(turn, false);
	}

	/**
	 * Resets all players to the starting position, removes cards from the game board, and resets the turn counter.
	 */
	public void reset() {
		for(int i = 0; i < getNumberOfPlayers(); i++){
			Player player = (Player)players.get(i);
			player.setPosition(gameBoard.getCell(0));
		}
		if(gameBoard != null) gameBoard.removeCards();
		turn = 0;
	}
	
	/**
	 * Rolls two dice and returns their values; in test mode, returns test dice rolls from the GUI.
	 * @return An array of two integers representing the dice roll results.
	 */
	public int[] rollDice() {
		if(testMode) {
			return gui.getDiceRoll();
		}
		else {
			return new int[]{
					dice[0].getRoll(),
					dice[1].getRoll()
			};
		}
	}
	
	/**
	 * Sends the specified player to jail, updates their position and jail status, and moves their piece in the GUI.
	 * @param player The Player to send to jail.
	 */
	public void sendToJail(Player player) {
	    int oldPosition = gameBoard.queryCellIndex(getCurrentPlayer().getPosition().getName());
		player.setPosition(gameBoard.queryCell("Jail"));
		player.setInJail(true);
		int jailIndex = gameBoard.queryCellIndex("Jail");
		gui.movePlayer(
		        getPlayerIndex(player),
		        oldPosition,
		        jailIndex);
	}
    
	/**
	 * Enables or disables all main GUI buttons according to the given boolean parameter.
	 * @param enabled True to enable buttons, false to disable.
	 */
	private void setAllButtonEnabled(boolean enabled) {
		gui.setRollDiceEnabled(enabled);
		gui.setPurchasePropertyEnabled(enabled);
		gui.setEndTurnEnabled(enabled);
        gui.setTradeEnabled(turn, enabled);
        gui.setBuyHouseEnabled(enabled);
        gui.setDrawCardEnabled(enabled);
        gui.setGetOutOfJailEnabled(enabled);
	}

	/**
	 * Sets the game board to be used by the game master.
	 * @param board The GameBoard object to set.
	 */
	public void setGameBoard(GameBoard board) {
		this.gameBoard = board;
	}
	
	/**
	 * Assigns the GUI interface to the game master.
	 * @param gui The MonopolyGUI instance to assign.
	 */
	public void setGUI(MonopolyGUI gui) {
		this.gui = gui;
	}

	/**
	 * Sets the initial amount of money each player starts with.
	 * @param money The amount of money to set as initial.
	 */
	public void setInitAmountOfMoney(int money) {
		this.initAmountOfMoney = money;
	}

	/**
	 * Initializes the player list with the specified number of players, giving each the initial amount of money.
	 * @param number The total number of players for the game.
	 */
	public void setNumberOfPlayers(int number) {
		players.clear();
		for(int i =0;i<number;i++) {
			Player player = new Player();
			player.setMoney(initAmountOfMoney);
			players.add(player);
		}
	}

	/**
	 * Sets the utility dice roll value used by the game master.
	 * @param diceRoll The dice roll value to set.
	 */
	public void setUtilDiceRoll(int diceRoll) {
		this.utilDiceRoll = diceRoll;
	}
	
	/**
	 * Starts the game by initializing the GUI and enabling the first player's turn and trade options.
	 */
	public void startGame() {
		gui.startGame();
		gui.enablePlayerTurn(0);
        gui.setTradeEnabled(0, true);
	}

	/**
	 * Advances the turn to the next player and updates the GUI to reflect the new player's status and enabled options.
	 */
	public void switchTurn() {
		turn = (turn + 1) % getNumberOfPlayers();
		if(!getCurrentPlayer().isInJail()) {
			gui.enablePlayerTurn(turn);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
            gui.setTradeEnabled(turn, true);
		}
		else {
			gui.setGetOutOfJailEnabled(true);
		}
	}
	
	/**
	 * Refreshes the GUI to reflect the current game state.
	 */
	public void updateGUI() {
		gui.update();
	}

	/**
	 * Prompts the GUI to show a utility dice roll and stores the result in the game master.
	 */
	public void utilRollDice() {
		this.utilDiceRoll = gui.showUtilDiceRoll();
	}

	/**
	 * Enables or disables test mode affecting dice roll behavior.
	 * @param b True to enable test mode, false to disable.
	 */
	public void setTestMode(boolean b) {
		testMode = b;
	}
}
