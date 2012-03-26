package model.game;


/**
 * A class that represents a Card. 
 * 
 * A card has a suite and a value, where value is between 1 and 13. 
 * Ace has the value 1.
 * 
 * @author lisastenberg
 * @author forssenm
 *
 */

public class Card {
	public enum Suite {
		SPADES, HEARTS, DIAMONDS, CLUBS;
	}
	
	private int value;
	private Suite suite;
	
	public Card(Suite suite, int value) {
		if(!validValue(value)) {
			throw new IllegalArgumentException("The value must be between 1 and 13.");
		}
		this.value = value;
		this.suite = suite;
	}
	
	/**
	 * @author lisastenberg
	 * The value is valid if it's between 1 and 13.
	 * 
	 * @param value
	 * @return true if the value is valid
	 */
	private boolean validValue(int value) {
		return value >= 1 && value <= 13;
	}
	
	/**
	 * @author lisastenberg
	 * @return the value of the card.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @author lisastenberg
	 * @return the suite of the card.
	 */
	public Suite getSuite() {
		return suite;
	}
	
	/**
	 * Equals-method for a card
	 * @author forssenm
	 * @param o is the object you will compare with
	 * @return true if suite and value are the same for both cards
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		else if (o.getClass() != this.getClass()) {
			return false;
		}
		else {
			Card card = (Card)o;
			return (this.suite == card.suite && this.value == card.value);
		}
	}
	
	/**
	 * @author forssenm
	 * toString method for the card class
	 * @return returns a string in the form of "3 of spades"
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.value + " of " + this.suite);
		return result.toString();
	}
	
	//Since we at the current state aren't planning on using any hashtables this code was added
	//for the cause of good practice
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 42; // any arbitrary constant will do
	}
}
