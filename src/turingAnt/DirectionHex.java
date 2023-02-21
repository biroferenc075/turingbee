package turingAnt;

public enum DirectionHex {
	posX(0), posZ(1), posY(2), negX(3), negZ(4), negY(5); 
	private final int val;
	DirectionHex(int i) {
		val = i;
	}
	final public int getVal() {
		return val;
	}
}
