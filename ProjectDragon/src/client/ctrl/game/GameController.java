package client.ctrl.game;

import java.util.List;

import remote.iClientGame;

import client.model.game.Table;

import model.card.Card;
import model.card.iCard;
import model.game.Pot;
import model.player.Bet;
import model.player.iPlayer;
import model.player.hand.*;
import client.event.*;

/**
 * This class contains methods that handles the application during game mode.
 * 
 * @author mattiashenriksson
 * @author lisastenberg
 * @author robinandersson
 * 
 */

public class GameController implements iClientGame {

	Table table;

	public GameController() {
		// TODO: Request for newTable?

		// TODO Constructor without parameters plz!
		// this(new Table(players, meIndex));
	}

	public GameController(Table table) {
		this.table = table;
		distributeInvisibleCards();
	}
	
	@Override
	public void newTable(List<iPlayer> players, int meIndex) {
		table = new Table(players, meIndex);
		distributeInvisibleCards();
	}

	/**
	 * Adds a player to the game table
	 * 
	 * @param player
	 *            The player to be added
	 * @author robinandersson
	 */
	public void addPlayer(iPlayer player) {
		this.table.addPlayer(player);
	}

	public void setCurrentBet(Bet bet) {
		table.getRound().getBettingRound().setCurrentBet(bet);
		EventBus.publish(new Event(Event.Tag.CURRENT_BET_CHANGED, bet
				.getValue()));
	}

	@Override
	public void setPot(Pot pot) {
		table.getRound().getPot().setValue(pot.getValue());
		EventBus.publish(new Event(Event.Tag.POT_CHANGED, pot.getValue()));
	}

	@Override
	public boolean fold(iPlayer player) {
		if (!table.getCurrentPlayer().equals(player)) {
			return false;
		}
		iPlayer p = table.getCurrentPlayer();
		p.getHand().discard();
		p.setActive(false);
		p.setDoneFirstTurn(true);
		EventBus.publish(new Event(Event.Tag.HAND_DISCARDED, player));
		return true;
	}

	@Override
	public boolean betOccurred(Bet bet) {
		if (!table.getCurrentPlayer().equals(bet.getOwner())) {
			return false;
		}
		iPlayer p = table.getCurrentPlayer();
		int tmp = bet.getValue() - p.getOwnCurrentBet();
		p.getBalance().removeFromBalance(tmp);
		EventBus.publish(new Event(Event.Tag.BALANCE_CHANGED, new Bet(p, p
				.getBalance().getValue())));
		p.setOwnCurrentBet(bet.getValue());
		EventBus.publish(new Event(Event.Tag.OWN_CURRENT_BET_CHANGED, new Bet(
				p, bet.getValue())));

		/*
		 * kolla s� att inte bigblind �r mindre �n smallblind men blir inskickad
		 * efter smallblind
		 */
		if (bet.getValue() >= table.getRound().getBettingRound()
				.getCurrentBet().getValue()) {
			table.getRound().getBettingRound().setCurrentBet(bet);
			EventBus.publish(new Event(Event.Tag.CURRENT_BET_CHANGED, bet
					.getValue()));
		}

		table.getRound().getPot().addToPot(bet.getValue());
		EventBus.publish(new Event(Event.Tag.POT_CHANGED, table.getRound()
				.getPot().getValue()));
		return true;
	}

	@Override
	//TODO: on�digt med b�de nextTurn och setTurn? 
	// om vi tar bort nextTurn kan man ta bort nextPlayer i klientens table med.
	public boolean nextTurn(iPlayer nextPlayer) {
		Boolean tmp = table.nextPlayer().equals(nextPlayer);
		if(tmp) {
			EventBus.publish(new Event(Event.Tag.TURN_CHANGED, table
					.getCurrentPlayer()));
			return true;
		} 
		return false;
	}

	@Override
	public void setTurn(int indexOfCurrentPlayer) {
		table.setIndexOfCurrentPlayer(indexOfCurrentPlayer);
		EventBus.publish(new Event(Event.Tag.TURN_CHANGED, table
				.getCurrentPlayer()));
	}
	
	@Override
	public void setHand(iPlayer player, iHand hand) {
		for(iPlayer tmp : table.getActivePlayers()) {
			if(tmp.equals(player)) {
				iHand playerHand = tmp.getHand();
				for(iCard card : hand.getCards()) {
					playerHand.addCard(card);
				}
				EventBus.publish(new Event(Event.Tag.HANDS_CHANGED, tmp));
				break;
			}
		}
	}

	@Override
	public void addCommunityCards(List<iCard> cards) {
		for (iCard c : cards) {
			table.addTableCard(c);
		}
		EventBus.publish(new Event(Event.Tag.COMMUNITY_CARDS_CHANGED, cards));
	}

	@Override
	public void newRound() {
		table.getTableCards().clear();
		for (iPlayer p : table.getActivePlayers()) {
			p.getHand().discard();
			EventBus.publish(new Event(Event.Tag.HAND_DISCARDED, p));
			if (p.getBalance().getValue() != 0) {
				p.setActive(true);
			}
		}
	}

	@Override
	public void setActive(iPlayer player, boolean b) {
		player.setActive(b);
	}

	@Override
	public void setPlayerOwnCurrentBet(Bet bet) {
		iPlayer p = bet.getOwner();
		p.setOwnCurrentBet(bet.getValue());
		EventBus.publish(new Event(Event.Tag.OWN_CURRENT_BET_CHANGED, bet));
	}
	
	@Override
	public void balanceChanged(Bet bet) {
		bet.getOwner().getBalance().addToBalance(bet.getValue());
		EventBus.publish(new Event(Event.Tag.BALANCE_CHANGED, bet));
	}
	
	private void distributeInvisibleCards() {
		iHand hand;
		for(iPlayer player : table.getActivePlayers()) {
			hand = player.getHand();
			for(int i = 0; i < 2; i++) {
				hand.addCard(new Card(Card.Suit.NO_SUIT, Card.Rank.NO_RANK));
			}
		}
	}
}
