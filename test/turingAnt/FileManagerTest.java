package turingAnt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;



public class FileManagerTest {
	@BeforeClass
	public static void init() {
		ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<StateKey, HexCommand>();
		hm.put(new StateKey(0,0), new HexCommand(0,0,0));
		hm.put(new StateKey(0,1), new HexCommand(0,0,0));
		hm.put(new StateKey(1,0), new HexCommand(0,0,0));
		hm.put(new StateKey(1,1), new HexCommand(0,0,0));
		
		HexAntLogic hal = new HexAntLogic(hm);
		
		FileManager.saveConfig(hal, "test");
	}
	
	@Test
	public void addExtensionTest() {
		String str = "test";
		assertEquals(str+".xml",FileManager.addExtension(str));
	}
	
	@Test
	public void removeExtensionTest() {
		File[] f = {new File("test.xml")};
		String str = "test";
		assertEquals(str,FileManager.removeExtensions(f)[0]);
	}
	
	@Test
	public void saveTest() {
		boolean exists =  Arrays.asList(FileManager.removeExtensions(FileManager.listFiles())).contains("test") ? true : false;
		assertTrue(exists);
	}
	
	@Test
	public void existsTest() {
		boolean exists =  FileManager.configExists("test");
		assertTrue(exists);
	}
	
	
	@Test
	public void defaultTest() {
		boolean exists =  FileManager.isDefaultConfig("test");
		assertFalse(exists);
	}
	
	@Test
	public void configLogicTest() throws IOException {
		ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<StateKey, HexCommand>();
		hm.put(new StateKey(0,0), new HexCommand(0,0,0));
		hm.put(new StateKey(0,1), new HexCommand(0,0,0));
		hm.put(new StateKey(1,0), new HexCommand(0,0,0));
		hm.put(new StateKey(1,1), new HexCommand(0,0,0));
		
		HexAntLogic hal = new HexAntLogic(hm);
		HexAntLogic htest = FileManager.getConfigLogic("test");
		int[][] cmds1 = new int[4][3];
		int[][] cmds2 = new int[4][3];
		for(int i = 0; i < 4; i++) {
			HexCommand cmd1 = hal.getCommand(new StateKey(i/2,i%2));
			HexCommand cmd2 = htest.getCommand(new StateKey(i/2,i%2));
				cmds1[i][0] = cmd1.getNextAntState();
				cmds1[i][1] = cmd1.getNextTileState();
				cmds1[i][2] = cmd1.getRotation();
				cmds2[i][0] = cmd2.getNextAntState();
				cmds2[i][1] = cmd2.getNextTileState();
				cmds2[i][2] = cmd2.getRotation();
				
		}
		assertArrayEquals(cmds1, cmds2);
	}
	
	@Test
	public void configDataTest() throws IOException {
		
		Object[][] e = new Object[][] {{0,0,0,0,0},{0,1,0,0,0},{1,0,0,0,0},{1,1,0,0,0}};
		Object[][] a= FileManager.getConfigData("test");
		
		assertArrayEquals(e, a);
	}
	
	@Test
	public void nonexistentDataTest() throws FileNotFoundException {
		assertArrayEquals(FileManager.getConfigData("tutiranincsilyen"), new Object[4][5]);
	}
	@Test
	public void nonexistentLogicTest() throws FileNotFoundException {
		ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<StateKey, HexCommand>();
		hm.put(new StateKey(0,0), new HexCommand(0,0,0));
		hm.put(new StateKey(0,1), new HexCommand(0,0,0));
		hm.put(new StateKey(1,0), new HexCommand(0,0,0));
		hm.put(new StateKey(1,1), new HexCommand(0,0,0));
		
		HexAntLogic hal = new HexAntLogic(hm);
		HexAntLogic htest = FileManager.getConfigLogic("tutiranincsilyen");
		int[][] cmds1 = new int[4][3];
		int[][] cmds2 = new int[4][3];
		for(int i = 0; i < 4; i++) {
			HexCommand cmd1 = hal.getCommand(new StateKey(i/2,i%2));
			HexCommand cmd2 = htest.getCommand(new StateKey(i/2,i%2));
				cmds1[i][0] = cmd1.getNextAntState();
				cmds1[i][1] = cmd1.getNextTileState();
				cmds1[i][2] = cmd1.getRotation();
				cmds2[i][0] = cmd2.getNextAntState();
				cmds2[i][1] = cmd2.getNextTileState();
				cmds2[i][2] = cmd2.getRotation();
				
		}
		assertArrayEquals(cmds1, cmds2);
	}
	@Test
	public void nonexistentDefaultTest() {
		assertFalse(FileManager.isDefaultConfig("tutirahogynincsilyen"));
	}
	@Test
	public void doesNotExistTest() {
		boolean exists =  FileManager.configExists("tutirahogynincsilyen");
		assertFalse(exists);
	}
}