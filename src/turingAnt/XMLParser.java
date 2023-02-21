package turingAnt;

import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
	private boolean def = false;
	private Object[][] data = new Object[4][5];
	private HexAntLogic hal = new HexAntLogic();
	private int num = 0;
	private ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<>();
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
	      if (qName.equalsIgnoreCase("cfg")) {
	          def = attributes.getValue("def").equals("1")?true:false;
	      }

	      if (qName.equalsIgnoreCase("data")) {
	          String sk = attributes.getValue(0);
	          String cmd = attributes.getValue(1);
	          
	          Integer[] statekey = new Integer[2];
	          Integer[] command = new Integer[3];
	          
	          for(int i = 0; i < 2; i++) {
	        	  statekey[i] = Character.digit(sk.charAt(i),10);
	        	  data[num][i] = Character.digit(sk.charAt(i),10);
	          }
	          for(int i = 0; i < 3; i++) {
	        	  command[i] = Character.digit(cmd.charAt(i),10);
	        	  data[num][i+2] = Character.digit(cmd.charAt(i),10);
	          }
	          num++;
	          hm.put(new StateKey(statekey[0],statekey[1]), new HexCommand(command[0],command[1],command[2]));
	      }
	 }
	 @Override
	public void endDocument() {
		 hal = new HexAntLogic(hm);
	 }
	 
	public boolean getDefault() {
		 return def;
	 }
	public Object[][] getData() {
		 return data;
	 }
	public HexAntLogic getLogic() {
		 return hal;
	 }
}
