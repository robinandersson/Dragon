/**
 * 
 */
package client.ctrl.game;

import java.rmi.RemoteException;
import java.util.List;

import model.card.Card;
import model.card.iCard;
import model.game.Pot;
import model.player.Account;
import model.player.Bet;
import model.player.iPlayer;
import model.player.hand.iHand;

import remote.iClient;
import remote.iClientGame;
import remote.iServer;
import remote.iServerGame;

import utilities.IllegalCallException;

/**
 * @author robinandersson
 *
 */

public class RemoteGameController implements iClientGame, iServerRequest {
	
	private iServerGame serverGameController;
	private GameController gameController;
	
	// TODO Flytta "lagringen" av account till ett mer passande st�lle?
	private Account account;
	
	
	public RemoteGameController(){
		this(new GameController());
	}
	
	public RemoteGameController(GameController gameController){
		
		this.gameController = gameController;

	   
	    
	}
	
	@Override
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requestCall(Bet bet) {
		
		try {
			return serverGameController.call(bet);
		} catch (IllegalCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean requestCheck(Bet bet) {
		
		try {
			return serverGameController.check(bet);
		} catch (IllegalCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean requestRaise(Bet bet) {
		
		try {
			return serverGameController.raise(bet);
		} catch (IllegalCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean requestFold(iPlayer player) {
		
		try {
			return serverGameController.fold(player);
		} catch (IllegalCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}


	@Override
	public void setPot(Pot pot) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fold(iPlayer player) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void nextTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void betOccured(Bet b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distributeCards() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCommunityCards(List<iCard> cards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHand(iPlayer player, iHand hand) {
		// TODO Auto-generated method stub
		
	}

	
}
