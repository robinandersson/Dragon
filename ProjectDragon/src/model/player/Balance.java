package model.player;

import model.game.Card;

/**
 * A class for balance. 
 * 
 * @author lisastenberg
 *
 */

public class Balance {
	private int balance;
	
	public Balance() {
		this(0);
	}
	
	public Balance(int bal) {
		balance = bal;
	}
	
	/**
	 * 
	 * @return the value of the balance.
	 */
	public int getBalance() {
		return balance;
	}
	
	/**
	 * Add x to the balance.
	 * @param x	the value you want to add to the balance.
	 */
	public void addToBalance(int x) {
		balance = balance + x;
	}
	
	/**
	 * Remove x from balance. 
	 * @param x the value you want to remove from the balance. 
	 */
	public void removeFromBalance(int x) {
		if(x > balance) {
			throw new IllegalArgumentException("You do not have enough money to remove " + x  + " from your balance");
		}
		balance = balance - x;
	}
	
	/**
	 * Equals-method for a balance
	 * @author lisastenberg
	 * @param o is the object you will compare with
	 * @return true if balance is the same.
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
			Balance tmp = (Balance)o;
			return this.balance == tmp.balance;
		}
	}
}
