package turingAnt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HexTileTest {
	HexTile t1, t2, t3, t4;
	
	@Before
	public void init() {
		t1 = new HexTile();
		t2 = new HexTile(0,0,0);
		t3 = new HexTile(1,0,1);
		t4 = new HexTile(1,1,0);
	}
	
	@Test
	public void equalsTest() {
		assertEquals(t1,t2);
	}
	
	@Test
	public void notEqualsTest() {
		assertNotEquals(t2,t3);
	}
	
	@Test
	public void hashCodeEqualsTest() {
		assertEquals(t1.hashCode(),t2.hashCode());
	}
	
	@Test
	public void hashCodeNotEqualsTest() {
		assertNotEquals(t3.hashCode(),t4.hashCode());
	}
	@Test
	public void getterTest() {
		int[] a = {t3.getX(),t3.getY(),t3.getZ()};
		int[] b = {1,0,1};
		assertArrayEquals(a, b);
	}
	
	@Test
	public void setterTest() {
		t2.setX(1);
		t2.setY(2);
		t2.setZ(3);
		int[] a = {t2.getX(),t2.getY(),t2.getZ()};
		int[] b = {1,2,3};
		assertArrayEquals(a, b);
	}


}
