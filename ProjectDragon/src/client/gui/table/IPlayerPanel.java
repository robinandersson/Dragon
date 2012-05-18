package client.gui.table;

import java.awt.Color;

import common.model.player.IPlayer;
import common.model.player.hand.IHand;


/**
 * Interface for handling all the PlayerPanels
 * @author forssenm
 *
 */
public interface IPlayerPanel {
	
	
	/**
	 * Sets the name of the player
	 */
	public void setName(IPlayer p);
	
	/**
	 * Discards the cards in the GUI for the given player
	 */
	public boolean discard();
	
	/**
	 * Sets the balance for the given player to the amount specified
	 */
	public void setBalance(String s);
	
	/**
	 * Shows the cards of the remaining players
	 */
	public void showCards(IHand h);
	
	/**
	 * Sets the background
	 */
	public void setTheBackground(Color h);
}
