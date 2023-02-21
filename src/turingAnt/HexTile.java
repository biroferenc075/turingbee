package turingAnt;

public class HexTile {
	private int posX;
	private int posY;
	private int posZ;
	
	HexTile() {
		posX = 0;
		posY = 0;
		posZ = 0;
	}
	
	HexTile(int x, int y, int z) {
		posX = x;
		posY = y;
		posZ = z;
	}
	
	public void setX(int x) {
		posX = x;
	}
	public void setY(int y) {
		posY = y;
	}
	public void setZ(int z) {
		posZ = z;
	}
	public int getX() {
		return posX;
	}
	public int getY() {
		return posY;
	}
	
	public int getZ() {
		return posZ;
	}
	
	@Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof HexTile) {
            HexTile s = (HexTile)obj;
            return posX==s.posX && posY==s.posY && posZ==s.posZ;
        }
        return false;
    }
	
	@Override
    public int hashCode() {
		return posX * 229 + posY * 227 + posZ * 257; 
    }
}
