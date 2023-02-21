package turingAnt;

import java.util.concurrent.ConcurrentHashMap;

public class Simulation {
	private ConcurrentHashMap<HexTile, Integer> tiles;
	private HexAnt ant;
	private HexAntLogic logic;
	
	public Simulation() {
		tiles = new ConcurrentHashMap<HexTile, Integer>();
		ant = new HexAnt();
		
		ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<>();
		
		//tile ant - rot tile ant
		hm.put(new StateKey(0,0), new HexCommand(0,0,0));
		hm.put(new StateKey(0,1), new HexCommand(0,0,0));
		hm.put(new StateKey(1,0), new HexCommand(0,0,0));
		hm.put(new StateKey(1,1), new HexCommand(0,0,0));
		logic = new HexAntLogic(hm);

	}
	
	public Simulation(ConcurrentHashMap<StateKey, HexCommand> hm) {
		tiles = new ConcurrentHashMap<HexTile, Integer>();
		ant = new HexAnt();
		logic = new HexAntLogic(hm);
	}
	
	public Simulation(HexAntLogic hal) {
		tiles = new ConcurrentHashMap<HexTile, Integer>();
		ant = new HexAnt();
		logic = hal;
	}
	
	public HexAnt getAnt() {
		return ant;
	}
	
	public ConcurrentHashMap<HexTile, Integer> getTiles() {
		return tiles;
	}
	
	public int getTileState(HexTile hexTile) {
		return tiles.getOrDefault(hexTile, 0);
	}
	public void setTileState(HexTile oldTile, int st) {
		tiles.put(oldTile, st);
	}
	public void iterate() {
		StateKey key = new StateKey(ant.getState(),getTileState(ant.getTile()));
		HexCommand cmd = logic.getCommand(key);
		
		// A hangya elfordulásának kiszámítása
		DirectionHex d = DirectionHex.posX;
		for(int i = 0; i < 6; i++)  {
			if(DirectionHex.values()[i].getVal() == (ant.getDir().getVal()+cmd.getRotation())%6)
			d = DirectionHex.values()[(ant.getDir().getVal()+cmd.getRotation())%6]; // !!!!
		}
		//System.out.println(d);
		ant.setDir(d);
		
		// Koordináta különbség kiszámítása
		int xPlus = 0;
		int yPlus = 0;
		int zPlus = 0;
		
		switch(d) {
			case posX:
				yPlus = -1;
				zPlus = 1;
				break;
			case posY:
				xPlus = 1;
				zPlus = -1;
				break;
			case posZ:
				xPlus = 1;
				yPlus = -1;
				break;
			case negX:
				yPlus = +1;
				zPlus = -1;
				break;
			case negY:
				xPlus = -1;
				zPlus = 1;
				break;
			case negZ:
				xPlus = -1;
				yPlus = +1;
				break;
		}
		
		// A cellába írás
		HexTile oldTile = ant.getTile();
		int nextTileState = cmd.getNextTileState();
		if (nextTileState == 0)
			tiles.remove(oldTile);
		else
			setTileState(oldTile, nextTileState);
		
		// A hangya mozgatása
		HexTile newTile = new HexTile(oldTile.getX()+xPlus,oldTile.getY()+yPlus,oldTile.getZ()+zPlus);
		ant.setTile(newTile);
		
		// A hangya állapotának beállítása
		ant.setState(cmd.getNextAntState());
	}
	
	public void setLogic(ConcurrentHashMap<StateKey, HexCommand> hm) {
		logic.setLogic(hm);
	}
	public void setLogic(HexAntLogic hal) {
		logic = hal;
	}

	public HexAntLogic getLogic() {
		return logic;
	}
}
