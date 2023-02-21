package turingAnt;

public class HexCommand {
	private int rotation;
	private int nextTileState;
	private int nextAntState;
	HexCommand(int r, int ts, int as) {
		rotation = r;
		nextTileState = ts;
		nextAntState = as;
	}
	
	HexCommand(Integer[] hexcmd) {
		rotation = hexcmd[0];
		nextTileState = hexcmd[1];
		nextAntState = hexcmd[2];
	}
	public int getRotation() {
		return rotation;
	}
	public int getNextTileState() {
		return nextTileState;
	}
	public int getNextAntState() {
		return nextAntState;
	}
}