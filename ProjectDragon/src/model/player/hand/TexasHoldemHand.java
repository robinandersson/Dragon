/**
 * 
 */
package model.player.hand;

import java.util.ArrayList;
import java.util.List;

import model.game.Card;

/**
 * A class representing a standard Texas Hold'em hand
 * 
 * @author robinandersson
 *
 */
public class TexasHoldemHand implements iHand {
	
	private List<Card> cards;
	private boolean isVisible;
	
	public TexasHoldemHand(boolean isVisible){
		 this.isVisible = isVisible;
		 cards = new ArrayList<Card>(7);
	}
	
	/**
	 * Throws away the hand's cards
	 */
	@Override
	public void discard() {
		cards.clear();
	}

	/**
	 * Returns the hand's cards
	 */
	@Override
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Adds a card to the hand
	 * @param card The card to be added
	 */
	@Override
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	 * Sets the visibility of the card
	 * @param isVisible True if visible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Returns the visibility of the Hand - if a player is able to see the hand
	 * 
	 * @return if the hand is visible (true) or not (false)
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Returns a string describing the hand
	 * 
	 * @return "'card1 value' of 'card1 suite', 'card2 value' of 'card2 suite' and so on
	 * ending with 'card3 value' of 'card3 suite'"
	 * 
	 */
	@Override
	public String toString() {
		
		StringBuilder stringB = new StringBuilder();
		
		for(int i = 0 ; i < cards.size() -1 ; i++){
			stringB.append(cards.get(i).toString());
		}
		
		stringB.append(" and " + cards.get(cards.size()-1));
		
		return stringB.toString();
		
	}	
	
	/**
	 * Determines if this hand is equal to another hand.
	 * 
	 * @return Two hands are equal if they are the same object or if their cards are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		else if(this == o) {
			return true;
		}
		else if (o.getClass() != this.getClass()) {
			return false;
		}
		else if (this.getCards().size() != ((TexasHoldemHand) o).getCards().size()){
			return false;
		}
		
		/*
		 * If the classes are the same the cards in each hand is compared
		 */
		else {
			
			/**TODO Improve equals method in TexasHoldemHand. Perhaps duplicate
			 * the lists and remove occurences from both lists if a match is
			 * found.
			 */
			
			List<Card> otherCards = ((TexasHoldemHand) o).getCards();
			
			//Every card in this hand is compared to the other Hand's cards
			for(Card card : cards){
				
				boolean sameCards = false;
				
				// The loop ends if a match is found or if no match exists
				int i = 0;
				while(i < otherCards.size() && !sameCards){
					
					// The boolean is set to true if 
					if(card.equals(otherCards.get(i))){
						sameCards = true;
					}
				
					i++;
				}
				
				/* If no match is found for this hand's cards then the two hands
				 * are not equal
				 */
				if(!sameCards){
					return false;
				}

			}
			return true;

		}
		
	}
	
	//Since we at the current state aren't planning on using any hashtables this code was added
	//for the cause of good practice
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 44; // any arbitrary constant will do
	}
	
}
