package turingAnt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class SimulationTest {
	StateKey sk;
	HexCommand cmd;
	Object[] e;
	Simulation sim;
	
	public SimulationTest(int a, int b, int c, int d, int e, Object[] exp) {
		sk = new StateKey(a,b);
		cmd = new HexCommand(c,d,e);
		this.e = exp;
		
	}
	@Before
	public void init() {
		sim = new Simulation();	
	}
	
	@Test
	public void defaultTest() {	
		DirectionHex d = sim.getAnt().getDir();
		int antSt = sim.getAnt().getState();
		int tileSt = sim.getTileState(sim.getAnt().getTile());
		int prevTileSt = sim.getTileState(new HexTile(0,0,0));
		int x = sim.getAnt().getTile().getX();
		int y = sim.getAnt().getTile().getY();
		int z = sim.getAnt().getTile().getZ();
		
		Object[] a = {d,antSt,tileSt,x,y,z,prevTileSt};
		Object[] e = {DirectionHex.posX,0,0,0,0,0,0};
		assertArrayEquals(e,a);		
	}
	@Test
	public void iterTest() {
		ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<StateKey, HexCommand>();
		hm.put(sk, cmd);
		
		sim.setLogic(hm);
		sim.iterate();
		
		DirectionHex d = sim.getAnt().getDir();
		int antSt = sim.getAnt().getState();
		int tileSt = sim.getTileState(sim.getAnt().getTile());
		int prevTileSt = sim.getTileState(new HexTile(0,0,0));
		int x = sim.getAnt().getTile().getX();
		int y = sim.getAnt().getTile().getY();
		int z = sim.getAnt().getTile().getZ();
		
		Object[] a = {d,antSt,tileSt,x,y,z,prevTileSt};
		assertArrayEquals(e,a);
	}
	@Parameters
	public static List<Object[]> parameters() {
	List<Object[]> params = new ArrayList<Object[]>();
	params.add(new Object[] {0,0,0,0,0, new Object[] {DirectionHex.posX,0,0,0,-1,1,0}});
	params.add(new Object[] {0,0,3,1,1, new Object[] {DirectionHex.negX,1,0,0,1,-1,1}});
	params.add(new Object[] {0,0,1,0,1, new Object[] {DirectionHex.posZ,1,0,1,-1,0,0}});
	params.add(new Object[] {0,0,5,1,1, new Object[] {DirectionHex.negY,1,0,-1,0,1,1}});
	params.add(new Object[] {0,0,2,1,1, new Object[] {DirectionHex.posY,1,0,1,0,-1,1}});
	params.add(new Object[] {0,0,4,1,1, new Object[] {DirectionHex.negZ,1,0,-1,1,0,1}});
	return params;
	}

}