package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Game.Card.Suite;

/**
 * A class that represents a Deck.
 * 
 * @author lisastenberg
 *
 */

public class Deck {
	List<Card> cards = new ArrayList<Card>();
	
	/**
	 * Creates a new deck.
	 */
	public Deck() {
		createSuite(Card.Suite.CLUBS);
		createSuite(Card.Suite.DIAMONDS);
		createSuite(Card.Suite.HEARTS);
		createSuite(Card.Suite.SPADES);
		shuffle();
	}
	
	/**
	 * Shuffles the deck.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * A help method that creates a suite with all 13 cards in the deck.
	 * @param suite
	 */
	private void createSuite(Suite suite) {
		for(int i = 1; i <= 13; i++) {
			cards.add(new Card(suite, i));
		}
	}
}
