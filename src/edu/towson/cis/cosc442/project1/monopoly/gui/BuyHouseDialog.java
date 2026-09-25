
package edu.towson.cis.cosc442.project1.monopoly.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import edu.towson.cis.cosc442.project1.monopoly.Player;


public class BuyHouseDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<?> cboMonopoly;
	private JComboBox<?> cboNumber;

	private Player player;

	/**
	 * Initializes the BuyHouseDialog for the given player, setting up the GUI components for selecting a monopoly and number of houses.
	 * @param player The player who will purchase houses
	 */
	public BuyHouseDialog(Player player) {
		this.player = player;
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(3, 2));
		c.add(new JLabel("Select monopoly"));
		c.add(buildMonopolyComboBox());
		c.add(new JLabel("Number of houses"));
		c.add(buildNumberComboBox());
		c.add(buildOKButton());
		c.add(buildCancelButton());
		c.doLayout();
		this.pack();
	}

	/**
	 * Creates and returns the Cancel button with an action listener that triggers cancellation.
	 * @return The JButton instance representing the Cancel button
	 */
	private JButton buildCancelButton() {
		JButton btn = new JButton("Cancel");
		btn.addActionListener(new ActionListener(){
			/**
			 * Handles action events; this method is implemented anonymously for handling OK button clicks.
			 * @param e The action event triggered by user interaction
			 */
			public void actionPerformed(ActionEvent e) {
				cancelClicked();
			}
		});
		return btn;
	}

	/**
	 * Builds and returns a combo box populated with the player's monopolies for selection.
	 * @return The JComboBox populated with the player's monopolies
	 */
	private JComboBox<?> buildMonopolyComboBox() {
		cboMonopoly = new JComboBox<Object>(player.getMonopolies());
		return cboMonopoly;
	}
	
	/**
	 * Builds and returns a combo box with numbers 1 to 5 for selecting the number of houses to purchase.
	 * @return The JComboBox containing the number options
	 */
	private JComboBox<?> buildNumberComboBox() {
		cboNumber = new JComboBox<Object>(new Integer[]{
				new Integer(1),
				new Integer(2),
				new Integer(3),
				new Integer(4),
				new Integer(5)});
		return cboNumber;
	}

	/**
	 * Creates and returns the OK button with an action listener that triggers the purchase action.
	 * @return The JButton instance representing the OK button
	 */
	private JButton buildOKButton() {
		JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener(){
			/**
			 * Handles action events; this method is implemented anonymously for handling OK button clicks.
			 * @param e The action event triggered by user interaction
			 */
			public void actionPerformed(ActionEvent e) {
				okClicked();
			}
		});
		return btn;
	}
	
	/**
	 * Handles the cancellation of the house purchase by closing the dialog.
	 */
	private void cancelClicked() {
		this.dispose();
	}
	
	/**
	 * Processes the house purchase based on selected monopoly and number, then closes the dialog.
	 */
	private void okClicked() {
		String monopoly = (String)cboMonopoly.getSelectedItem();
		int number = cboNumber.getSelectedIndex() + 1;
		player.purchaseHouse(monopoly, number);
		this.dispose();
	}
}
