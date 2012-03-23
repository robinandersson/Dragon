package game;

import static org.junit.Assert.*;

import model.game.Pot;

import org.junit.Test;

public class PotTest {

	@Test
	public void testAddToPot() {
		Pot p = new Pot();
		p.addToPot(10);
		assertTrue(p.getPot() == 10);
	}
	
	@Test
	public void testEmptyPot() {
		Pot p = new Pot();
		p.addToPot(10);
		p.emptyPot();
		assertTrue(p.getPot() == 0);
	}

}
