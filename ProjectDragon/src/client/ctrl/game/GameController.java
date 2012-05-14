package client.ctrl.game;

import java.util.List;

import client.model.game.Table;

import model.card.Card;
import model.card.InvisibleCard;
import model.card.iCard;
import model.game.Pot;
import model.player.Bet;
import model.player.iPlayer;
import model.player.hand.*;

/**
 * This class contains methods that handles the application during game mode.
 * 
 * @author mattiashenriksson
 * @author robinandersson
 * 
 */

public class GameController {

	Table table;

	public GameController() {
		// TODO: Request for newTable?

		// TODO Constructor without parameters plz!
		// this(new Table(players, meIndex));
	}

	public void newTable(List<iPlayer> players, int meIndex) {
		table = new Table(players, meIndex);
	}

	public GameController(Table table) {
		this.table = table;
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
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.CURRENT_BET_CHANGED, bet.getValue()));
	}

	public void setPot(Pot pot) {
		table.getRound().getPot().setValue(pot.getValue());
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.POT_CHANGED, pot.getValue()));
	}

	public boolean fold(iPlayer player) {
		if (!table.getCurrentPlayer().equals(player)) {
			return false;
		}
		iPlayer p = table.getCurrentPlayer();
		p.getHand().discard();
		p.setActive(false);
		p.setDoneFirstTurn(true);
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.HAND_DISCARDED, player));
		return true;
	}

	public boolean betOccured(Bet bet) {
		iPlayer p = table.getCurrentPlayer();
		int tmp = bet.getValue() - p.getOwnCurrentBet();
		p.getBalance().removeFromBalance(tmp);
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.BALANCE_CHANGED, new Bet(p, p
						.getBalance().getValue())));
		p.setOwnCurrentBet(bet.getValue());
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.OWN_CURRENT_BET_CHANGED, new Bet(p, bet.getValue())));

		/*
		 * kolla s� att inte bigblind �r mindre �n smallblind men blir inskickad
		 * efter smallblind
		 */
		if (bet.getValue() >= table.getRound().getBettingRound()
				.getCurrentBet().getValue()) {
			table.getRound().getBettingRound().setCurrentBet(bet);
			client.event.EventBus.publish(new client.event.Event(
					client.event.Event.Tag.CURRENT_BET_CHANGED, bet.getValue()));
		}

		table.getRound().getPot().addToPot(bet.getValue());
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.POT_CHANGED, table.getRound().getPot().getValue()));
		return true;
	}

	public boolean nextTurn(iPlayer nextPlayer) {
		Boolean tmp = table.nextPlayer().equals(nextPlayer);
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.TURN_CHANGED, table.getCurrentPlayer()));
		return tmp;
	}

	public void setTurn(int indexOfCurrentPlayer) {
		table.setIndexOfCurrentPlayer(indexOfCurrentPlayer);
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.TURN_CHANGED, table.getCurrentPlayer()));
	}
	
	public void setHand(iPlayer player, iHand hand) {
		// TODO Modify method to set the hand to the player directly
		iPlayer me = table.getPlayers().get(table.getMeIndex());
		iHand myHand = me.getHand();
		for (iPlayer p : table.getActivePlayers()) {
			if (p.equals(me)) {
				for (iCard c : hand.getCards()) {
					myHand.addCard(c);
				}
			} else {
				p.getHand().addCard(new InvisibleCard());
				p.getHand().addCard(new InvisibleCard());
			}
		}
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.HANDS_CHANGED, me));
	}

	/**
	 * Add cards to the table.
	 * 
	 * @param cards
	 *            The cards you want to add.
	 */
	public void addCommunityCards(List<iCard> cards) {
		for (iCard c : cards) {
			table.addTableCard(c);
		}
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.COMMUNITY_CARDS_CHANGED, cards));
	}

	public void newRound() {
		table.getTableCards().clear();
		for (iPlayer p : table.getActivePlayers()) {
			p.getHand().discard();
			client.event.EventBus.publish(new client.event.Event(
					client.event.Event.Tag.HAND_DISCARDED, p));
			if (p.getBalance().getValue() != 0) {
				p.setActive(true);
			}
		}
	}

	public void setActive(iPlayer p, boolean b) {
		p.setActive(b);
	}

	public void setPlayerOwnCurrentBet(Bet bet) {
		iPlayer p = bet.getOwner();
		p.setOwnCurrentBet(bet.getValue());
		client.event.EventBus.publish(new client.event.Event(
				client.event.Event.Tag.OWN_CURRENT_BET_CHANGED, bet));
	}
}
