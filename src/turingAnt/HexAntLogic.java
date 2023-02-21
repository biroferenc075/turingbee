package turingAnt;

import java.util.concurrent.ConcurrentHashMap;

public class HexAntLogic {
	private ConcurrentHashMap<StateKey, HexCommand> logic;
	
	HexAntLogic() {
		logic = new ConcurrentHashMap<StateKey, HexCommand>();
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 2; j++)
				logic.put(new StateKey(i,j), new HexCommand(0,0,0));
	}
	
	HexAntLogic(ConcurrentHashMap<StateKey, HexCommand> hm) {
		logic = hm;
	}
	
	public void setLogic(ConcurrentHashMap<StateKey, HexCommand> hm) {
		logic = hm;
	}
	
	public ConcurrentHashMap<StateKey, HexCommand> getLogic() {
		return logic;
	}
	
	public HexCommand getCommand(StateKey sk) {
		return logic.getOrDefault(sk, new HexCommand(0,0,0));
	}
}
