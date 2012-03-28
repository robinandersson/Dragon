package model.game;

/**
 * A class that simulates a betting-round.
 * 
 * A betting-round ends when all active players have either checked or called the current bet.
 * @author lisastenberg and forssenm
 *
 */
public class BettingRound {
	private int currentBet;
	
	/**
	 * Creates a new betting-round
	 */
	public BettingRound() {
		currentBet = 0;
	}
	
	/**
	 * 
	 * @return the current bet.
	 */
	public int getCurrentBet() {
		return currentBet;
	}
	
	/**
	 * Set the current bet to x.
	 * @param x	the value you want to set the current bet to.
	 */
	public void setCurrentBet(int x) {
		currentBet = x;
	}
	
	/**
	 * Equals method for the BettingRound class
	 * @author forssenm
	 * @param o is the object you will compare with
	 * @return true if o is the same object or if o has the same currentBet
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
		else {
			BettingRound br = (BettingRound)o;
			return (this.currentBet == br.currentBet);
		}
	}
	
	/**
	 * toString method for the BettingRound class
	 * @author forssenm
	 * @return a string with the form "Current bet is ..."
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Current bet is " + this.currentBet);
		return result.toString();
	}
	
	//Since we at the current state aren't planning on using any hashtables this code was added
	//for the cause of good practice
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 42; // any arbitrary constant will do
	}
}
