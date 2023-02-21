package turingAnt;

public class HexAnt {
	private HexTile tile;
	private DirectionHex dir;
	private int state;
	
	HexAnt() {
		tile = new HexTile();
		dir = DirectionHex.posX;
		state = 0;
	}
	
	public HexTile getTile() {
		return tile;
	}
	public DirectionHex getDir() {
		return dir;
	}
	public int getState() {
		return state;
	}
	public void setTile(HexTile t) {
		tile = t;
	}
	public void setDir(DirectionHex d) {
		dir = d;
	}
	public void setState(int s) {
		state = s;
	}
}
