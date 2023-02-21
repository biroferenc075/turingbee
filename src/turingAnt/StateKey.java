package turingAnt;

public class StateKey {
	private int state1;
	private int state2;
	StateKey(int s1, int s2) {
		state1 = s1;
		state2 = s2;
	}
	StateKey(Integer[] st) {
		state1 = st[0];
		state2 = st[1];
	}
	
	@Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof StateKey) {
            StateKey s = (StateKey)obj;
            return state1==s.state1 && state2==s.state2;
        }
        return false;
    }
	
	@Override
    public int hashCode() {
		return state1 * 31 + state2;
    }
	
	public int getState1() {
		return state1;
	}
	public int getState2() {
		return state2;
	}
}
