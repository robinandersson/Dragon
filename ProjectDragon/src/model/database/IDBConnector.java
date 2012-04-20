package model.database;

import java.util.Map;

import model.player.Account;
import model.player.Player;
/**
 * An interface that needs to be implemented to connect to the database.
 * 
 * @author lisastenberg
 *
 */
public interface IDBConnector {

	/**
	 * Loads information about an account in the database.
	 * 
	 * @param account the account you want to load information from
	 * @return the accountinfo.
	 */
	public Account loadAccount(String account);
	
	/**
	 * Saves accountinformation into the database.
	 * 
	 * @param account	The account you want to save information about.
	 */
	public void saveAccount(Account account);
	

	/**
	 * Inputs information about a game into the database, which contains
	 * the date the game was played at.
	 * 
	 * @param gameID	The gameID is unique for a game.
	 * @param date		The date the game was played. 
	 */
	public void saveGame(String gameID, String date);
	
	/**
	 * Input a placement into the database.
	 * 
	 * @param gameID	The gameID is unique for a game.
	 * @param userName	The player 
	 * @param placement	The placement the player got in the game.
	 */
	public void savePlacement(String gameID, Player player, int placement);
	
	/**
	 * A method that loads the statistics for a game from the database.
	 * 
	 * @param gameID	The gameID is unique for a game.
	 * @return	A map with the key as the placement and a String with the
	 * 			players userName.
	 */
	public Map<Integer, String> loadGamePlacement(String gameID);
}
