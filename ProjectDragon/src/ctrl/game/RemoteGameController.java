/**
 * 
 */
package ctrl.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import event.Event;
import event.EventHandler;

import model.card.iCard;
import model.player.Bet;
import model.player.iPlayer;

import remote.iClientGame;
import remote.iServerGame;
import utilities.IllegalCallException;
import utilities.IllegalCheckException;
import utilities.IllegalRaiseException;

/**
 * A class that manages all communication to and from the server
 * 
 * @author robinandersson
 * @author lisastenberg
 *
 */
public class RemoteGameController extends UnicastRemoteObject implements iServerGame, EventHandler{

	GameController gameController;
	//TODO Ta bort den "clientGames" som inte beh�vs (n�r det blir tydligt vad som beh�vs)
	// A map containing all logged in players and another map containing their
	// active players and references to every respective player objects remote
	// game controller
//	Map<Account, Map<iPlayer, client.ctrl.game.RemoteGameController>> clientGames;
//	Map<RemoteGameController, Map<iPlayer, client.ctrl.game.RemoteGameController>> clientGames;
	Map<iPlayer, iClientGame> playerReferences;
	
	LinkedList<iPlayer> playerList;
	
	public RemoteGameController() throws RemoteException {
		this(new GameController());
	}
	
	public RemoteGameController(GameController gameController) throws RemoteException {
		super();
		playerList = new LinkedList<iPlayer>();
		playerReferences = new TreeMap<iPlayer, iClientGame>();
		this.gameController = gameController;
	}
	
	/**
	 * Adds a player to the game and a reference to the player's client
	 * 
	 * @param player The player to be added to the game
	 * @param clientGame The reference to the added player
	 */
	public void addPlayer(iPlayer player, iClientGame clientGame) {
		playerReferences.put(player, clientGame);
		playerList.add(player);
	}
	
	/**
	 * Removes a player from the game and the reference to the player's client
	 * 
	 * @param player The player to be removed from the game
	 * @param clientGame The reference to the removed player
	 */
	public void removePlayer(iPlayer player, iClientGame clientGame) {
		playerReferences.remove(player);
		playerList.remove(player);
	}
	
	@Override
	public LinkedList<iPlayer> getPlayers() throws IllegalCallException,
			RemoteException {
		return playerList;
	}

	@Override
	public boolean call(Bet bet) {
		return gameController.call(bet);
	}

	@Override
	public boolean check(Bet bet) throws IllegalCheckException {
		return gameController.check(bet);
	}

	@Override
	public boolean raise(Bet bet) throws IllegalRaiseException {
		return gameController.raise(bet);
	}

	@Override
	public boolean fold(iPlayer player) {
		return gameController.fold(player);
	}
	
	//TODO Javadoc
	public void onEvent(Event evt) {
		
		switch (evt.getTag()) {
		
			case SERVER_FOLD:
				
				iPlayer player = (iPlayer)evt.getValue();
				for(iClientGame client : playerReferences.values()) {
					client.fold(player);
				}

				break;
			case SERVER_UPDATE_BET:
				Bet bet = (Bet)evt.getValue();
				for(iClientGame client : playerReferences.values()) {
					client.updateBet(bet);
				}
				
				break;

				
			case SERVER_DISTRIBUTE_CARDS:

				//anropa setHand(parameter) i client.ctrl.game.GameController
				break;
				
			case SERVER_ADD_TABLE_CARDS:
				List<iCard> cards = (List<iCard>)evt.getValue();
				for(iClientGame client : playerReferences.values()) {
					client.addCommunityCards(cards);
				}
	
				break;
				
			case SERVER_CREATE_TABLE:
				
				//anrop newTable(parameter) i client.ctrl.game.GameController
				
				break;
				
			case SERVER_DISTRIBUTE_POT:
				
				//anrop balanceChanged(evt.getValue()) i client.ctrl.game.GameController
				
				break;
				
			case SERVER_UPDATE_POT:
				
				//anrop setPot(evt.getValue()) i client.ctrl.game.GameController
				
				break;
				
			case SERVER_NEW_ROUND:
				
				//anrop newRound() i client.ctrl.game.GameController
				
				break;
				
			case SERVER_SET_TURN:
				
				//anrop setTurn(evt.getValue()) i client.ctrl.game.GameController
				
				break;
				
			case SERVER_SET_PLAYER_UNACTIVE:
				
				//anrop setActive(evt.getValue(),false) i client.ctrl.game.GameController

				break;
				
			case SERVER_SET_OWN_CURRENT_BET:
				
				//anrop setPlayerOwnCurrentBet(evt.getValue) i client.ctrl.game.GameController
				
		
			default:
				break;
		}
	}

	@Override
	public void startGame() throws IllegalCallException, RemoteException {
		
		for(int i = 0; i < playerList.size() ; i++){
			gameController.addPlayer(playerList.get(i));
		}
		
		// TODO Handle start game scenario
		
	}

}

