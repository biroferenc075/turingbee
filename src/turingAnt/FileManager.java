package turingAnt;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.SAXException;

public class FileManager {

	private FileManager() {}
	
	static public String addExtension(String str) {
		return str+".xml";
	}
	static public File[] listFiles() {
		File dir = new File(".");
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".xml");
		    }
		});
		
		return files;
	}
	
	static public String[] removeExtensions(File[] files) {
		String[] res = new String[files.length];
		int i = 0;
		for(File f: files) {
			String n = f.getName();
			res[i++] = n.substring(0, n.indexOf('.')); 
		}
		
		return res;
	}
	
	static public String getFirstConfig() {
		String[] lst = removeExtensions(listFiles());
		if(lst.length>0)
			return lst[0];
		else
			return null;
	}
	
	static public boolean configExists(String name) {
		String[] files = removeExtensions(listFiles());
		for(String file: files)
			if(name.equals(file)) {
				return true;
			}
		return false;
	}
	
	static public boolean isDefaultConfig(String name) {
		if(!configExists(name)) {
			return false;
		}
		XMLParser handler = new XMLParser();
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(addExtension(name), handler);

        } catch (FileNotFoundException e) {
            return false;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
        
        return handler.getDefault();
	}
	
	static public HexAntLogic getConfigLogic(String name) {
		XMLParser handler = new XMLParser();
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(addExtension(name), handler);

        } catch (FileNotFoundException e) {
        	JFrame jFrame = new JFrame();
        	JOptionPane.showMessageDialog(jFrame, "File cannot be found!");
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } 
        
        return handler.getLogic();
	}
	
	static public Object[][] getConfigData(String name) {
		XMLParser handler = new XMLParser();
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(addExtension(name), handler);

        } catch (FileNotFoundException e) {
        	JFrame jFrame = new JFrame();
        	JOptionPane.showMessageDialog(jFrame, "File cannot be found!");
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } 
        
        return handler.getData();
	}
	
	static public void saveConfig(HexAntLogic hal, String name) {	
		ConcurrentHashMap<StateKey, HexCommand> hm = hal.getLogic();
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
		XMLStreamWriter xsw = null;
		try{
		    xsw = xof.createXMLStreamWriter(new FileWriter(addExtension(name)));
		    xsw.writeStartDocument();
		    xsw.writeCharacters("\r\n"); 
		    xsw.writeStartElement("cfg");
		    xsw.writeAttribute("def","0");
		    xsw.writeCharacters("\r\n"); 
		    for(int i = 0; i < 2; i++)
				for(int j = 0; j < 2; j++) {
					StateKey sk = new StateKey(i,j);
					HexCommand cmd = hm.get(sk);
					xsw.writeCharacters("\t"); 
					xsw.writeStartElement("data");
			        xsw.writeAttribute("sk",""+Character.forDigit(sk.getState1(),10)+Character.forDigit(sk.getState2(),10));
					xsw.writeAttribute("cmd",""+Character.forDigit(cmd.getRotation(),10)+Character.forDigit(cmd.getNextTileState(),10)+Character.forDigit(cmd.getNextAntState(),10));
			        xsw.writeEndElement();
			        xsw.writeCharacters("\r\n"); 
				}
		    xsw.writeEndElement();
		    xsw.writeEndDocument();
		    xsw.flush();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
