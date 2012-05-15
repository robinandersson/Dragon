package ctrl.game;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import event.Event;
import event.Event.Tag;
import event.EventBus;

import model.game.BettingRound;
import model.game.P;
import model.game.Pot;
import model.game.SidePotHandler;
import model.game.Table;
import model.player.Bet;
import model.player.OwnCurrentBetComparator;
import model.player.IPlayer;
import utilities.IllegalCallException;
import utilities.IllegalCheckException;
import utilities.IllegalRaiseException;

/**
 * This class contains methods that handles the application during game mode.
 * 
 * @author mattiashenriksson
 * @author robinandersson
 * @author lisastenberg
 * 
 */

public class GameController {

	private Table table;
	
	public GameController() {
		this(new Table());
	}

	public GameController(Table table) {
		this.table = table;
	}
	
	/**
	 * Adds a player to the game's table
	 * 
	 * @param player The player to be added
	 * @author robinandersson
	 */
	public void addPlayer(IPlayer player) {
		this.table.addPlayer(player);
	}
	
	/**
	 * Adds the players in the list to to the game
	 * 
	 * @param player The player to be added
	 * @author robinandersson
	 */
	public void addPlayers(Collection<IPlayer> player) {
		this.table.addPlayers(player);
	}

	/**
	 * Adds a new card to the "table cards"
	 */
	public void addCommunityCard() {
		table.addCommunityCard();
	}

	/**
	 * Calls the distributeCards() method in the table class where the actual
	 * distribution of the two "personal cards" to every remaining player is
	 * done 
	 * 
	 * @author robinandersson
	 */
	public void distributeCards() {
		/* 
		 * The  distribution is invoked by this controller class and redirected
		 * to the table class. This makes more sense and also eliminates a bunch
		 * of unnecessary method calls between the two classes
		 */
		table.distributeCards();
	}

	//TODO: Koden i call, raise, check, fold �r lite lik. refactor?
	/**
	 * Method for handling the call scenario
	 * 
	 * @author forssenm 
	 * @author mattiashenriksson
	 */
	private void doCall() {
		IPlayer currentPlayer = table.getCurrentPlayer();
		int playersOwnCurrentBet = currentPlayer.getOwnCurrentBet();

		int currentBetValue = 
				table.getRound().getBettingRound().getCurrentBet().getValue();
		
		/* if the player calls a bet which is bigger than his balance, the 
		 * player will call with as much as his balance allows and move all-in
		 */
		//TODO: �r det h�r all-in?
		if(currentPlayer.getBalance().getValue() < 
				currentBetValue - playersOwnCurrentBet) {
			currentBetValue = currentPlayer.getBalance().getValue() 
					+ playersOwnCurrentBet;
		}
		
		//TODO den h�r delen skulle kunna refactoreras kanske?
		/* arrange the player's balance, bet and the pot according to the call 
		 */
		table.getRound().getPot().addToPot(currentBetValue - playersOwnCurrentBet);
		currentPlayer.setOwnCurrentBet(currentBetValue);
		currentPlayer.getBalance().removeFromBalance(currentBetValue 
				- playersOwnCurrentBet);
		
		/* the player has now done a move */
		currentPlayer.setDoneFirstTurn(true);
		
		progressTurn();
	}
	
	/**
	 * Checks if a call is valid and does a call if it is.
	 * 
	 * @author lisastenberg
	 * @author robinandersson
	 * @param bet	The placed bet.
	 * @return true if call is a valid action.
	 * @throws IllegalCallException
	 */
	public boolean call(Bet bet) {
		int currentBetValue = 
				table.getRound().getBettingRound().getCurrentBet().getValue();

		if(!table.getCurrentPlayer().equals(bet.getOwner())) {
			return false;
		} else if (currentBetValue <= 0) {
			throw new IllegalCallException(
					"Not possible to call since no bet has been posted");
		} else {
			doCall();
			EventBus.publish(new Event(Event.Tag.SERVER_UPDATE_BET, bet));
		}
		return true;
	}
	
	/**
	 * Performs a raise.
	 * @param amount The amount to raise the pot with.
	 * @throws IllegalRaiseException 
	 */
	//TODO delvis otestad
	public boolean raise(Bet bet) {
		IPlayer currentPlayer = table.getCurrentPlayer();
		if(!currentPlayer.equals(bet.getOwner())) {
			return false;
		}
		
		//The amount the player wants to raise with
		int amount = bet.getValue() - currentPlayer.getOwnCurrentBet();
		BettingRound currentBettingRound = table.getRound().getBettingRound(); 

		if(amount > currentPlayer.getBalance().getValue()) {
			throw new IllegalRaiseException(
					"Not enough money on balance to make that raise");
		} else if(bet.getValue() <= currentBettingRound.getCurrentBet().getValue()
				+ P.INSTANCE.getBigBlindValue()) {
			throw new IllegalRaiseException("The raise have to be bigger" +
					"than the current bet plus big blind.");
		}
		
		/* arrange the player's balance, bet and the pot according to the raise 
		 */
		currentPlayer.getBalance().removeFromBalance(amount);
		table.getRound().getPot().addToPot(amount);
		currentBettingRound.setCurrentBet( new Bet(table.getCurrentPlayer(),
						amount + currentPlayer.getOwnCurrentBet()));
		currentPlayer.setOwnCurrentBet(
				amount + currentPlayer.getOwnCurrentBet());
		
		/* the player has now done a move */
		currentPlayer.setDoneFirstTurn(true);
		
		progressTurn();
		
		EventBus.publish(new Event(Event.Tag.SERVER_UPDATE_BET, bet));
		return true;
	}
	
	/**
	 * Performs a fold.
	 */
	//TODO Delvis otestad
	public boolean fold(IPlayer player) {
		//Check if the player is allowed to fold.
		if(!table.getCurrentPlayer().equals(player)) {
			return false;
		}
		player.getHand().discard();
		player.setActive(false);
		player.setDoneFirstTurn(true);
		EventBus.publish(new Event(Event.Tag.SERVER_FOLD, player));
		
		progressTurn();
		
		return true;
	}
	
	/**
	 * Performs a check.
	 * @throws IllegalCheckException 
	 */
	//TODO otestat: exception, setOwncurrentBet
	public boolean check(Bet bet) {
		IPlayer currentPlayer = table.getCurrentPlayer();
		//Check if the player is allowed to check

		if(!currentPlayer.equals(bet.getOwner())) {
			return false;
		}
		
		/* if there is a bet bigger than your own current bet you should not
		 * be able to check */
		if (currentPlayer.getOwnCurrentBet() < table.getRound()
				.getBettingRound().getCurrentBet().getValue()) {
			throw new IllegalCheckException(
				"Your own current bet is lesser than the global current bet");
		}
		
		currentPlayer.setDoneFirstTurn(true);
		
		EventBus.publish(new Event(Event.Tag.SERVER_UPDATE_BET,bet));
		progressTurn();
		
		return true;
	}
	
	/**
	 * Performs a showdown by laying out the rest of the cards.
	 * 
	 * @return a list of the winning players of the current round.
	 * @lisastenberg
	 */
	public void performShowdown(List<IPlayer> plrs, int potAmount) {

		/* put all the community cards out on the table */
		int cardsOnTable = table.getTableCards().size();
		for(int i = cardsOnTable; i < 5; i++) {
			addCommunityCard();
		}
		
		table.doShowdown(plrs, potAmount);
	}
	
	/**
	 * Performs actions required for starting a new round
	 */
	public void nextRound() {
		List<IPlayer> players = table.getPlayers();
		
		table.setShowdownDone(false);
		table.getRound().getPot().emptyPot();
		EventBus.publish(new Event(Event.Tag.SERVER_UPDATE_POT, table.getRound().getPot()));
		table.getRound().getPreBettingPot().emptyPot();
		table.clearTableCards();
		table.getSidePots().clear();
		for (IPlayer p : players) {
			p.getHand().discard();
			p.setOwnCurrentBet(0);
			p.setDoneFirstTurn(false);			
			if (p.getBalance().getValue() != 0) {
				p.setActive(true);
			}
		}
		
		EventBus.publish(new Event(Event.Tag.SERVER_NEW_ROUND,""));
		
		/* new cards for all active players*/
		table.getDealer().newDeck();
		distributeCards();

		/* give dealer-button to the right player*/
		table.nextDealerButtonIndex();
		
		postBlinds();
		
		/* set the turn to the right player */
		//TODO funkar f�r tv� spelare?, detta g�rs p� ett flertal st�llen = 
		//		refactor? 
		int indexOfCurrentPlayer = table.getDealerButtonIndex();
		for (int i = 0; i < 3; i++) {
			do {
				indexOfCurrentPlayer = (indexOfCurrentPlayer + 1) % table.getPlayers().size();
			} while (!players.get(indexOfCurrentPlayer).isActive());
		}
		table.setIndexOfCurrentPlayer(indexOfCurrentPlayer);
		EventBus.publish(new Event(Event.Tag.SERVER_SET_TURN, indexOfCurrentPlayer));
	}
	
	/**
	 * Makes the players who's turn it is to post the big and small blind to do 
	 * so.
	 */
	//TODO ska denna vara h�r? likt raise = refactor?
	private void postBlinds() {
		List<IPlayer> players = table.getPlayers();

		int dealerButtonIndex = table.getDealerButtonIndex();
		
		/* define the initial value of the blinds */ 
		int bigBlind = P.INSTANCE.getBigBlindValue();
		int smallBlind = bigBlind / 2;
		
		/* calculate smallBlindIndex */
		int smallBlindIndex;
		int count = 1;
		do {
			smallBlindIndex = (dealerButtonIndex + count) % table.getPlayers().size();
			count++;
		} while (!players.get(smallBlindIndex).isActive());
		
		/* calculate bigBlindIndex */
		int bigBlindIndex;
		count = 1;
		do {
			bigBlindIndex = (smallBlindIndex + count) % table.getPlayers().size();
			count++;
		} while (!players.get(bigBlindIndex).isActive());
		
		IPlayer smallBlindPlayer = players.get(smallBlindIndex);
		IPlayer bigBlindPlayer = players.get(bigBlindIndex);
		
		/* define the definite value on the blinds (a player migth have to 
		 * small balance to post the full blind) */
		if (smallBlindPlayer.getBalance().getValue() < smallBlind) {
			smallBlind = smallBlindPlayer.getBalance().getValue();
		}
		
		if (bigBlindPlayer.getBalance().getValue() < bigBlind) {
			bigBlind = bigBlindPlayer.getBalance().getValue();
		}
		
		/* post blinds */
		smallBlindPlayer.getBalance().removeFromBalance(smallBlind);
		smallBlindPlayer.setOwnCurrentBet(smallBlind);
		bigBlindPlayer.getBalance().removeFromBalance(bigBlind);
		bigBlindPlayer.setOwnCurrentBet(bigBlind);

		/* add blinds to pot and current bet */
		table.getRound().getPot().addToPot(smallBlind + bigBlind);
		if (bigBlind >= smallBlind) {
			table.getRound().getBettingRound().setCurrentBet(
				new Bet(bigBlindPlayer,bigBlind));
		} else {
			table.getRound().getBettingRound().setCurrentBet(
					new Bet(smallBlindPlayer,smallBlind));
		}
		
		EventBus.publish(new Event(Tag.SERVER_UPDATE_BET, new Bet(smallBlindPlayer,smallBlind)));
		EventBus.publish(new Event(Tag.SERVER_UPDATE_BET, new Bet(bigBlindPlayer,bigBlind)));
		
		/* if a player has gone all-in he shall not be able to act */
		//TODO ska denna vara h�r?
		if (bigBlindPlayer.getBalance().getValue() == 0) {
			bigBlindPlayer.setDoneFirstTurn(true);
		}
		if (smallBlindPlayer.getBalance().getValue() == 0) {
			smallBlindPlayer.setDoneFirstTurn(true);
		}
	}
	
	/**
	 * Performs actions required for starting a new betting round
	 */
	//TODO B�ttre javadoc
	//TODO �vergripande metod = annat namn?
	public void nextBettingRound() {
		List<IPlayer> players = table.getPlayers();
		
		/* the table should have the default settings for a new betting round */
		table.getRound().getBettingRound().setCurrentBet(new Bet());
		for (IPlayer p : players) {
			p.setOwnCurrentBet(0);
			EventBus.publish(new Event(Event.Tag.SERVER_SET_OWN_CURRENT_BET, new Bet(p,0)));
			p.setDoneFirstTurn(false);
		}
		
		/* give right player the turn */
		table.setIndexOfCurrentPlayer(table.getDealerButtonIndex());
		for (int i = 0; i < 2; i++) {
			table.nextPlayer();
		}
		
		/* check if it's time for showdown */
		if (table.getTableCards().size() == 5 || 
				table.getActivePlayers().size() == 1 || 
				table.getActivePlayers().size() == 0) {
			
			/* if so, first perform showdown for possible sidepots */
			List<SidePotHandler> sidePots = table.getSidePots();
			if (sidePots != null) {
				for (SidePotHandler sph : sidePots) {
					performShowdown(sph.getPlayers(),sph.getPot().getValue());
				}
			}
			
			/* then perfom showdown for the table's current state, unless all 
			 * players has gone all-in */
			if (table.getActivePlayers().size() != 0) { 
				performShowdown(table.getActivePlayers(), table.getRound().getPot().getValue());
			}
		
		/* else, if it's time for flop, show flop */
		} else if (table.getTableCards().size() == 0) {
			for(int i = 0; i < 3; i++) {
				addCommunityCard();
			}
			table.getRound().getPreBettingPot().setValue(table.getRound().getPot().getValue());
			
		// if its time for river or turn	
		} else {
			addCommunityCard();
			table.getRound().getPreBettingPot().setValue(table.getRound().getPot().getValue());
		}
	}
	
	/**
	 * Performs the actions which occur when a player has gone all-in 
	 */
	//TODO mkt f�rklaringar h�r. skulle koden varit mer sj�lvf�rklarande? JA!
	public void handleAllIn() {
		List<IPlayer> allInPlayers = table.getAllInPlayers();
		List<IPlayer> activePlayers = table.getActivePlayers();
		List<SidePotHandler> sidePots = table.getSidePots();
		
		/* sort allInPlayers so that the next task is performed in the correct
		 * order */
		Collections.sort(allInPlayers, new OwnCurrentBetComparator());
		
		/* for each player that has gone all-in a SidePotHandler containing '
		 * info regarding that all-in case is created */
		for (IPlayer p : allInPlayers) {
			
				/* calculate how big the all-in bet was and conduct neccesary 
				 * changes according to this bet */
				int allInAmount = p.getOwnCurrentBet();
				int sidePotValue = allInAmount * activePlayers.size() 
						+ table.getRound().getPreBettingPot().getValue();
				for (IPlayer ap : activePlayers) {
					ap.setOwnCurrentBet(ap.getOwnCurrentBet() - allInAmount);
				}
				table.getRound().getPreBettingPot().emptyPot();
				table.getRound().getPot().removeFromPot(sidePotValue);
				
				/* create the sidepot */
				Pot sidePot = new Pot(sidePotValue);
				sidePots.add(new SidePotHandler(table.getActivePlayers(), sidePot));
		
				/* controll prints */
	            System.out.println("\n\n-------------------------------\n" + 
	            "SIDEPOT ADDED\n");
	            System.out.println("sidePotValue: " + sidePotValue + "\n");
	            System.out.println("ADDED PLAYERS:");
	            for (IPlayer ap : table.getActivePlayers() )
	            	System.out.println(ap.getName());
	            System.out.println("\n-----------------------------------\n");
				
	            /* the all-in player should after this not longer be active */
	            p.setActive(false);
	            EventBus.publish(new Event(Event.Tag.SERVER_SET_PLAYER_UNACTIVE, p));
		} 
	}
	
	/**
	 * This method evaluates what measures has to be done to make the game 
	 * progress after a player has done a turn, and then perform these measures
	 */
	private void progressTurn() {
		
		/* if the betting is done possible all-in case must be handled and then
		 * a new betting round should take place */
		if (table.isBettingDone()) {
			handleAllIn();
			nextBettingRound();
		} 
		
		/* set the turn to the next player*/
		table.nextPlayer();
		
		/* if a showdown has been done a new round should take place */
		if (table.isShowdownDone()) {
			nextRound();
		}
		
	}

}
