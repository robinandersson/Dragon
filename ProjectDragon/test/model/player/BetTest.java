package model.player;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * A test for Bet.
 * @author lisastenberg
 *
 */
public class BetTest {

	@Test
	public void testGetValue() {
		Bet b = new Bet(new User(), 10);
		assertTrue(b.getValue() == 10);
		
	}
	
	@Test
	public void testGetOwner() {
		User u = new User();
		Bet b = new Bet(u, 10);
		assertTrue(b.getOwner() == u);
	}	
	
	@Test
	public void testEquals() {
		fail("not yet implemented");
	}
	
	@Test
	public void testToString() {
		fail("not yet implemented");
	}

}
