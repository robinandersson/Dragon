package client.model.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.card.*;
import model.game.*;
import model.player.*;
import utilities.*;

/**
 * A class that represent table at which a poker game takes place. This class 
 * has a central role in Dragon. It has access to all the other classes in the 
 * application and is the class through which the game is controlled.
 * 
 * @author Mattias Henriksson
 * @author lisastenberg
 * @author robinandersson
 * 
 */

public class Table {
	private Round round;
	private List<Card> tableCards;
	private List<iPlayer> players;
	private int meIndex;
	private int indexOfCurrentPlayer;
	private int indexOfDealerButton; //TODO: Vill vi se vem som har dealerbutton?
	
	/**
	 * Creates a new Table.
	 */
	public Table(List<iPlayer> players, int meIndex) {
		this.meIndex = meIndex;
		this.players = players;
		round = new Round();
		tableCards = new LinkedList<Card>();
		indexOfCurrentPlayer = 0;
		indexOfDealerButton = 0;
	}
	
	/**
	 * Adds a player to the table.
	 * @param p The player that will be added to the list of players
	 * @throws IllegalArgumentException if there are already ten players at the table
	 */
	public void addPlayer(iPlayer p) {
		if (players.size() < 10) {
			players.add(p);
		} else {
			throw new PlayersFullException();
		}
	}
	
	/**
	 * Set the turn to the next player in order, and returns that player.
	 * 
	 * @return the next (active) player
	 * @author lisastenberg
	 */
	public iPlayer nextPlayer() {
		
		/* if none is active at the table, do nothing */
		if (getActivePlayers().size() == 0) {
			return getCurrentPlayer();
		}
		
		indexOfCurrentPlayer = (indexOfCurrentPlayer + 1) % players.size();

		if (getCurrentPlayer().isActive()) {
			return getCurrentPlayer();
		}
		return nextPlayer();
	}
	
	/**
	 * Increases the dealer button index to the next player still in the game
	 * 
	 * @return the next dealer button index. 
	 * @author robinandersson
	 * @author mattiashenriksson
	 */
	//TODO annat namn p� denna?
	//TODO Test nextDealerButtonPlayer()
	//TODO Discuss and implement a possible better solution to dealer button
	public int nextDealerButtonIndex() {
		do {
			indexOfDealerButton++;
			// Reset the index if it is at the end of the player-list
			if (indexOfDealerButton == players.size()) {
				indexOfDealerButton = 0;
			}
		} while (!players.get(indexOfDealerButton).isActive());
		
		// TODO Determine what happens if a player has lost recently.
		// If the dealer button only should be set to players still in the game
		// or if lost players should be "ghosts"
		
		// The dealer button is set to a player that is still in the game.
		/*while(!players.get(indexOfDealerButton).isStillInGame()){
			indexOfDealerButton++;
		}*/
		return indexOfDealerButton;
	}
	
	/**
	 * 
	 * @return The player who's turn it currently is to bet, fold, raise or check
	 */
	public iPlayer getCurrentPlayer() {
		return players.get(indexOfCurrentPlayer);
	}
	
	/**
	 * 
	 * @return The player who's turn it currently is
	 */
	public int getDealerButtonIndex() {
		return indexOfDealerButton;
	}
	
	
	/**
	 * Adds a card to the "table cards" 
	 * @param c The card which will be added
	 * @throws IllegalArgumentException if there are all ready five cards on the table 
	 */
	public void addTableCard(Card c) {
		if (tableCards.size() < 5) {
			tableCards.add(c);
		} else {
			throw new TableCardsFullException();
		}
	}
	
	/**
	 * Clears all "table cards" from the table.
	 */
	public void clearTableCards() {
		tableCards.clear();
	}
	
	/**
	 * 
	 * @return The current round
	 */
	public Round getRound() {
		return round;
	}
	
	/**
	 * This method is used only for testing of the class.
	 * @return A list of players at the table.
	 */
	public List<iPlayer> getPlayers() {
		return players;
	}
	
	/**
	 * 
	 * @return A list of the players at the table who are currently active
	 */
	public List<iPlayer> getActivePlayers() {
		List<iPlayer> activePlayers = new ArrayList<iPlayer>();
		for (iPlayer p : players) {
			if (p.isActive()) {
				activePlayers.add(p);
			}
		}
		return activePlayers;
	}
	
	/**
	 * This method is used only for testing of the class.
	 * @return The "table cards" represented as a list of cards.
	 */
	public List<Card> getTableCards() {
		return tableCards;
	}

	/**
	 * Tostring method for the Table class
	 * @author Mattias Forssen
	 * @author mattiashenriksson
	 * @return Returns a string containing the names of all players, cards, 
	 * who the current player is and what cards are shown.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Players at table:\n");
		for(iPlayer p : this.players) {
			result.append(p.toString() + "\n");
		}
		result.append("\n" + "Current player is " + getCurrentPlayer().getName() + "\n");
		result.append("Player with Dealer button is: " + 
				(players.get(getDealerButtonIndex())).getName() + "\n");
		result.append("Table cards are:" + "\n" + tableCards.toString() + "\n");
		result.append("Pot is: " + round.getPot().getValue() + "\n");
		result.append("Pre-betting pot is: " + round.getPreBettingPot().getValue() + "\n");
		result.append("Current bet is: " + 
				round.getBettingRound().getCurrentBet().getValue() + "\n");
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param index The index indexOfCurrentPlayer should be set to.
	 */
	public void setIndexOfCurrentPlayer(int index) {
		indexOfCurrentPlayer = index;
	}
	
	/**
	 * 
	 * @return The players list-index of the current player
	 */
	public int getIndexOfCurrentPlayer() {
		return indexOfCurrentPlayer;
	}

	/**
	 * Equals method for the Table class
	 * @author forssenm
	 * @param Object to compare with
	 * @return returns true if they are the same object otherwise false
	 */
	@Override
	public boolean equals(Object o) {
		return (o == this);
	}
	
	//Since we at the current state aren't planning on using any hashtables this code was added
	//for the cause of good practice
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 42; // any arbitrary constant will do
	}

	/**
	 * 
	 * @return the index of the client.
	 */
	public int getMeIndex() {
		return meIndex;
	}
}
