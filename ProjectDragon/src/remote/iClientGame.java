/**
 * 
 */
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.card.iCard;
import model.game.Pot;
import model.player.Bet;
import model.player.iPlayer;
import model.player.hand.iHand;

/**
 * @author robinandersson
 * @author lisastenberg
 */
public interface iClientGame extends Remote {
	
	/**
	 * Creates a new table.
	 * 
	 * @param players The players at the table.
	 * @param meIndex The index in the list of players that is the user.
	 */
	public void newTable(List<iPlayer> players, int meIndex) throws RemoteException;
	
	/**
	 * Set the pot.
	 * 
	 * @param pot The value you want to set the pot to.
	 * @throws RemoteException
	 */
	public void setPot(Pot pot) throws RemoteException;
	
	/**
	 * A method for handling when a player has folded. 
	 * 
	 * @param player	The player who folded.
	 * @return	true if the fold went through.
	 * @throws RemoteException
	 */
	public boolean fold(iPlayer player) throws RemoteException;
	
	/**
	 * A method for handling when a bet has occurred.
	 * 
	 * @param b The bet.
	 * @return true if the method went through successfully.
	 * @throws RemoteException
	 */
	public boolean betOccurred(Bet bet) throws RemoteException;
	
	/**
	 * A method that transfer the turn to the nextPlayer. Does a check that
	 * nextPlayer is the same on the clients table as it should be.
	 * 
	 * @param nextPlayer
	 * @return true if nextPlayer is the player that should have the turn.
	 * @throws RemoteException
	 */
	public boolean nextTurn(iPlayer nextPlayer) throws RemoteException;
	
	/**
	 * Set turn to indexOfCurrentPlayer. This method should only be used then
	 * the table is created.
	 * 
	 * @param indexOfCurrentPlayer
	 * @throws RemoteException
	 */
	public void setTurn(int indexOfCurrentPlayer) throws RemoteException;
	
	/**
	 * Add communitycards to the table.
	 * 
	 * @param cards The cards you want to add.
	 * @throws RemoteException
	 */
	public void addCommunityCards(List<iCard> cards) throws RemoteException;
	
	/**
	 * Set a players hand.
	 * 
	 * @param player The players hand you want to set.
	 * @param hand	The hand
	 * @throws RemoteException
	 */
	public void setHand(iPlayer player, iHand hand) throws RemoteException;
		
	/**
	 * A method for setting a player active or inactive.
	 * 
	 * @param player The player.
	 * @param b	The boolean you want to set.
	 * @throws RemoteException
	 */
	public void setActive(iPlayer player, boolean b) throws RemoteException;
	
	/**
	 * Set a players ownCurrentBet
	 * 
	 * @param bet	The bet
	 * @throws RemoteException
	 */
	public void setPlayerOwnCurrentBet(Bet bet) throws RemoteException;
	
	/**
	 * Set up a new round.
	 * 
	 * @throws RemoteException
	 */
	public void newRound() throws RemoteException;
	
	/**
	 * Changes a persons balance.
	 * 
	 * @param bet Holds a player and how much the player should add to his 
	 * balance.
	 */
	public void balanceChanged(Bet bet) throws RemoteException;

}
