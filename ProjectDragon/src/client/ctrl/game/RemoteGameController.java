/**
 * 
 */
package client.ctrl.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

import client.event.Event;
import client.event.EventBus;
import client.event.EventHandler;
import client.model.game.Table;

import model.card.ICard;
import model.game.Pot;
import model.player.Bet;
import model.player.IPlayer;
import model.player.hand.IHand;

import remote.IClientGame;
import remote.IServerGame;

/**
 * @author robinandersson
 * @author lisastenberg
 * 
 * This clientside class manages all game communication to and from the server.
 */

public class RemoteGameController extends UnicastRemoteObject
						implements IClientGame, IServerRequest, EventHandler {
	
	private IServerGame serverGame;
	private RemoteCommunicationController clientComm;
	
	private GameController gameController;
	
	private IPlayer user;
	
	public RemoteGameController(RemoteCommunicationController clientComm,
			IPlayer user) throws RemoteException{
		this.clientComm = clientComm;
		this.user = user;
		EventBus.register(this);
	}
	
	public RemoteGameController(RemoteCommunicationController clientComm,
			IPlayer user, Table table) throws RemoteException{
		this(clientComm, user, new GameController(table));
	}
	
	public RemoteGameController(RemoteCommunicationController clientComm, 
					IPlayer user, GameController gameController) throws RemoteException{
		this.clientComm = clientComm;
		this.gameController = gameController;
		this.user = user;
		this.gameController.addPlayer(user);
		
		EventBus.register(this);
	}
	
	/**
	 * Sets the reference to the controller that is handling this particular
	 * game, together with other client's instances of this class.  
	 * @param serverGame The reference to the server's instance controlling this
	 * particular game
	 */
	public void setServerGame(IServerGame serverGame){
		this.serverGame = serverGame;
	}
	
	/**
	 * Tells the server that the supplied Player instance ready or not ready to
	 * start the game
	 * @param player The player that is ready or not to start the game
	 * @param isReady True if the player is ready to start the game
	 * @return True if the request was successful
	 */
	public boolean setReadyToPlay(IPlayer player, boolean isReady) {
		
		try {
			return serverGame.isReadyToStart(
					clientComm.getAccount(), player,
					isReady);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds the player in the list to to the game
	 * 
	 * @param player The player to be added
	 * @author robinandersson
	 */
	public void addPlayers(IPlayer player) {
		gameController.addPlayer(player);
	}
	
	/**
	 * Adds the players in the list to to the game
	 * 
	 * @param players The players to be added
	 * @author robinandersson
	 */
	public void addPlayers(Collection<IPlayer> players) {
		gameController.addPlayers(players);
	}

	@Override
	public boolean requestCall(Bet bet) {
		
		if(serverGame != null){
			
			try {
				return serverGame.call(bet);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public boolean requestCheck(Bet bet) {
		
		if(serverGame != null){
			
			try {
				return serverGame.check(bet);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public boolean requestRaise(Bet bet) {
		
		if(serverGame != null){
			
			try {
				return serverGame.raise(bet);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public boolean requestFold(IPlayer player) {
		
		if(serverGame != null){

			try {
				return serverGame.fold(player);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public void setActive(IPlayer player, boolean b) {
		gameController.setActive(player, b);
	}
	
	@Override
	public void setPot(Pot pot) {
		gameController.setPot(pot);
	}

	@Override
	public boolean fold(IPlayer player) {
		return gameController.fold(player);
	}

	@Override
	public boolean nextTurn(IPlayer nextPlayer) {
		return gameController.nextTurn(nextPlayer);
	}

	@Override
	public boolean betOccurred(Bet bet) {
		return gameController.betOccurred(bet);
	}

	@Override
	public void addCommunityCard(ICard card) {
		gameController.addCommunityCard(card);

	}

	@Override
	public void setHand(IPlayer player, IHand hand) {
		gameController.setHand(player, hand);
	}

	@Override
	public void setTurn(int indexOfCurrentPlayer) {
		gameController.setTurn(indexOfCurrentPlayer);
	}

	@Override
	public void setPlayerOwnCurrentBet(Bet bet) {
		gameController.setPlayerOwnCurrentBet(bet);		
	}
	
	public void newRound() {
		gameController.newRound();
	}

	@Override
	public void balanceChanged(Bet bet) {
		gameController.balanceChanged(bet);
	}

	@Override
	public void newTable(List<IPlayer> players, int meIndex) {
		gameController.newTable(players, meIndex);
	}

	@Override
	public void onEvent(Event evt) {
		Bet bet;
		
		switch(evt.getTag()) {
		case REQUEST_CALL:
			if(!(evt.getValue() instanceof Bet)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag() + "\nYou sent: " + evt.getValue().getClass());
			} else {
				bet = (Bet) evt.getValue();
				requestCall(bet);
			}
			break;
		case REQUEST_CHECK:
			if(!(evt.getValue() instanceof Bet)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag() + "\nYou sent: " + evt.getValue().getClass());
			} else {
				bet = (Bet) evt.getValue();
				requestCheck(bet);
			}
			break;
		case REQUEST_FOLD:
			IPlayer player;
			if(!(evt.getValue() instanceof IPlayer)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag() + "\nYou sent: " + evt.getValue().getClass());
			} else {
				player = (IPlayer) evt.getValue();
				requestFold(player);
			}
			break;
		case REQUEST_RAISE:
			if(!(evt.getValue() instanceof Bet)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag() + "\nYou sent: " + evt.getValue().getClass());
			} else {
				bet = (Bet) evt.getValue();
				requestRaise(bet);
			}
			break;
		default:
			break;
		}
		
	}

	
}
