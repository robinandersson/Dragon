package model.player.hand;

import java.util.List;

import model.card.iCard;

/**
 * An interface for a hand in a poker game. 
 * @author lisastenberg
 * @author robinandersson
 *
 */
public interface iHand {

	/**
	 * Removes all cards from a hand.
	 * @author lisastenberg
	 */
	public void discard();
	
	/**
	 * @author lisastenberg
	 * @author robinandersson
	 * @return a List of the hand's cards.
	 */
	public List<iCard> getCards();
	
	/**
	 * Add a new card to the hand.
	 * @author lisastenberg
	 * @author robinandersson
	 * @param card The card you want to add.
	 */
	public void addCard(iCard card);
	
	/**
	 * Makes the cards visible to other players.
	 * @author lisastenberg
	 * @author robinandersson
	 * @param isVisible The boolean that sets the visibility.
	 */
	public void setVisible(boolean isVisible);
	
	// TODO Remove isVisible()- and setVisible() methods from the iHand interf.
	// This means adapting other classes, table and player for example, to allow
	// for such a change. The visibility is a Texas-Holdem specific property
	// Remember to edit the Hand and HandTest classes to match these changes.
	/**
	 * Indicates if a card is visible or not
	 * @author mattiashenriksson
	 * @return A boolean that tells if the hand is visible
	 */
	public boolean isVisible();
	
	@Override
	public boolean equals(Object o);
	
	@Override
	public String toString();
}
