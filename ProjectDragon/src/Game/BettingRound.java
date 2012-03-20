package Game;

/**
 * A class that simulates a betting-round.
 * 
 * A betting-round ends when all active players have either checked or called the current bet.
 * @author lisastenberg
 *
 */
public class BettingRound {
	private int currentBet;
	
	/**
	 * Creates a new betting-round
	 */
	public BettingRound() {
		
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
}
