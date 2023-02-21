package turingAnt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HexAntTest {
	
	HexAnt ant;
	@Before
	public void init() {
		ant = new HexAnt();
	}
	
	
	@Test
	public void ctorTest() {
		int[] a = {ant.getTile().getX(),ant.getTile().getY(),ant.getTile().getZ(),ant.getState()};
		int[] b = {0,0,0,0};
		assertArrayEquals(a, b);
	}
	
	@Test
	public void dirTest() {
		assertEquals(ant.getDir(), DirectionHex.posX);
	}

}
