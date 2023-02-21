package turingAnt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

public class GUIWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int gWIDTH = 300;
	private static final int gHEIGHT = 300;
	
	public GUIWindow() {
		GUI gui = new GUI();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setContentPane(gui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocation(size.width-350,size.height/2);
        setResizable(false);
        setTitle("Settings");
	}
	
	public class GUI extends JPanel {
		
		private final Color colorBGR = new Color(0x5C80BC);
		private static final long serialVersionUID = 1L;
		
		private JButton start;
		private JButton stop;
		private JButton step;
		private JButton reset;
		private JButton save;
		private JButton load;
		private JSlider speed;
		private JTable rules;
		private JComboBox<String> presets;
		
		private Timer timer;
		private boolean timerrunning = false;
		
		class ButtonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
			    JButton source = (JButton) e.getSource();
			    
			    if(source.equals(start) && !timerrunning){
			    	timer = new Timer();
			        timer.scheduleAtFixedRate(new TimerTask() {
			        	@Override
			        	public void run() {
			        	SimWindow.getSim().iterate();     			  
			        	}
			        }, 0,(211-10*(int)speed.getValue())); //1000/val iterations/s
			        timerrunning = true;
			        
			    }
			    else if(source.equals(stop) && timerrunning){
			    	timer.cancel();
			    	timerrunning = false;
			    }
			    else if(source.equals(step)){
			    	SimWindow.getSim().iterate();  
			    }
			    else if(source.equals(reset)){
			    	if(timerrunning)
			    		timer.cancel();
			        SimWindow.setSim(new Simulation());
			        SimWindow.setOffsetX(0);
			        SimWindow.setOffsetY(0);
			        
			        HexAntLogic hal = getTableLogic();		
			        SimWindow.getSim().setLogic(hal);
			        
			        if(timerrunning) {
			        	timer = new Timer();
				        timer.scheduleAtFixedRate(new TimerTask() {
				        	@Override
				        	public void run() {
				        	SimWindow.getSim().iterate();     			  
				        	}
				        }, 0,(211-10*(int)speed.getValue())); //1000/val iterations/s
				        timerrunning = true;
			        }
			    }
			    else if(source.equals(save)){
			    	String sel = (String) presets.getSelectedItem();
			    	if (sel == null || sel.equals("")) {
			    		JFrame jFrame = new JFrame();
			        	JOptionPane.showMessageDialog(jFrame, "Configuration name cannot be empty!");
			    	}
			    	else if(FileManager.isDefaultConfig(sel)) {
			    		JFrame jFrame = new JFrame();
			        	JOptionPane.showMessageDialog(jFrame, "Sadly, this is a default configuration, therefore it cannot be rewritten. Try a different name!");
			    	}
			    	else {
			    		HexAntLogic hal = getTableLogic();		
			    		if(!FileManager.configExists(sel)) {
							presets.addItem(sel);
							presets.setSelectedItem(sel);
						}
							
						FileManager.saveConfig(hal, sel);
			    	}
			    }
			    else if(source.equals(load)){
			    	if(timerrunning) {
			        	timer.cancel();
			        	timerrunning = false;
			    	}
			    	SimWindow.setSim(new Simulation());
				    SimWindow.setOffsetX(0);
				    SimWindow.setOffsetY(0);
				        
			    	String sel = (String) presets.getSelectedItem();
			    	if(sel != null) {
			    		if(FileManager.configExists(sel)) {
				    	Object[][] data = FileManager.getConfigData(sel);
				        ((RulesTableModel) rules.getModel()).setData(data);
				        
				        SimWindow.getSim().setLogic(FileManager.getConfigLogic(sel)); 
			    		}
			    		else { 
			    			SimWindow.getSim().setLogic(getTableLogic());
			    			JFrame jFrame = new JFrame();
			            	JOptionPane.showMessageDialog(jFrame, "File cannot be found!");
			    		}
			    	}
			    }
			}	
		}
		class SliderListener implements ChangeListener {
			@Override
		    public void stateChanged(ChangeEvent e) {
		        JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		        	if(timerrunning == true) {
		        		timer.cancel();
		        		timer = new Timer();
		        		timer.scheduleAtFixedRate(new TimerTask() {
				        	@Override
				        	public void run() {
				        	SimWindow.getSim().iterate();     			  
				        	}
		        		}, 0, (211-10*(int)source.getValue()));
		        	}    
		        }
			}
		}
		
		class RulesTableModel extends AbstractTableModel {
			private static final long serialVersionUID = 1L;
			private String[] columnNames = {"TST","AST","ROT","TNS","ANS"};
		    private Object[][] data;
		    
		    public RulesTableModel() {
		    	String fcfg = FileManager.getFirstConfig();
				if(fcfg != null)
					data = FileManager.getConfigData(fcfg);
				else {
					Object[][] d = {{0,0,0,0,0},{0,1,0,0,0},{1,0,0,0,0},{1,1,0,0,0}};
					data = d;
					JFrame jFrame = new JFrame();
		        	JOptionPane.showMessageDialog(jFrame, "No saved configuration found.");
				}
		    }
		    
		    public void setData(Object[][] d) {
		    	data = d;
		    	fireTableDataChanged();
		    }
		    @Override
		    public int getColumnCount() {
		        return columnNames.length;
		    }
		    @Override
		    public int getRowCount() {
		        return data.length;
		    }
		    @Override
		    public String getColumnName(int col) {
		        return columnNames[col];
		    }
		    @Override
		    public Object getValueAt(int row, int col) {
		        return data[row][col];
		    }
		    @Override
			public Class<? extends Object> getColumnClass(int c) {
		    	return Integer.class; //return getValueAt(0, c).getClass();
		    }
		    @Override
		    public boolean isCellEditable(int row, int col) {
		        if (col < 2) {
		            return false;
		        } else {
		            return true;
		        }
		    }
		    @Override
		    public void setValueAt(Object value, int row, int col) {
		    	if(col == 2) {
		    		data[row][col] = (int)value%6;
		    	}
		    	else {
		    		data[row][col] = (int)value%2;
		    	}
		        
		        fireTableCellUpdated(row, col);
		    }
		}
		
		public GUI() {
		        setPreferredSize(new Dimension(gWIDTH, gHEIGHT));
		        setBackground(colorBGR);
		        start = new JButton("Start");
		        stop = new JButton("Stop");
		        step = new JButton("Step");
		        reset = new JButton("Reset");
		        save = new JButton("Save");
		        load = new JButton("Load");
		        
		        JPanel iterPanel = new JPanel();
		        iterPanel.setLayout(new BoxLayout(iterPanel, BoxLayout.LINE_AXIS));
		        iterPanel.add(start);
		        iterPanel.add(stop);
		        iterPanel.add(step);
		        iterPanel.add(reset);
		        add(iterPanel);
		        
		        JPanel savePanel = new JPanel();
		        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.LINE_AXIS));
		        savePanel.add(save);
		        savePanel.add(load);
		        add(savePanel);
		        
		        speed = new JSlider(JSlider.HORIZONTAL,1,21,11);
		        JLabel speedLabel = new JLabel("Iterations per second:");
		        speedLabel.setAlignmentX((float) 0.5);
		        JPanel speedPanel = new JPanel();
		        speedPanel.setLayout(new BoxLayout(speedPanel, BoxLayout.PAGE_AXIS));
		        speedPanel.add(speedLabel);
		        speedPanel.add(speed);
		        add(speedPanel);

		        rules = new JTable(new RulesTableModel());
		        rules.getColumnModel().getColumn(0).setPreferredWidth(30);
		        rules.getColumnModel().getColumn(1).setPreferredWidth(30);
		        rules.getColumnModel().getColumn(2).setPreferredWidth(30);
		        rules.getColumnModel().getColumn(3).setPreferredWidth(30);
		        rules.getColumnModel().getColumn(4).setPreferredWidth(30);
		        JPanel rulesPanel = new JPanel(new BorderLayout());
		        rulesPanel.add(rules, BorderLayout.CENTER);
		        rulesPanel.add(rules.getTableHeader(), BorderLayout.NORTH);
		        rules.getTableHeader().setReorderingAllowed(false);
		        
		        String[] pr = FileManager.removeExtensions(FileManager.listFiles());
		        presets = new JComboBox<String>(pr);
		        presets.setEditable(true);
		        JPanel presetsPanel = new JPanel(new BorderLayout());
		        JLabel presetsLabel = new JLabel("Presets:");
		      	presetsPanel.add(presetsLabel, BorderLayout.NORTH);
		        presetsPanel.add(presets, BorderLayout.SOUTH);
		        add(presetsPanel);
		        
		        JPanel configPanel = new JPanel();
		        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.PAGE_AXIS));
		        configPanel.add(presetsPanel);
		        configPanel.add(rulesPanel);
		        add(configPanel);
		        
		        ButtonListener bl = new ButtonListener();
		        SliderListener sl = new SliderListener();
		        start.addActionListener(bl);
		        stop.addActionListener(bl); 
		        step.addActionListener(bl); 
		        reset.addActionListener(bl); 
		        save.addActionListener(bl); 
		        load.addActionListener(bl); 
		        
		        speed.addChangeListener(sl);
		        speed.setMajorTickSpacing(10);
		        speed.setMinorTickSpacing(1);
		        speed.setPaintTicks(true);
		        
		        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		        labelTable.put( 1, new JLabel("10") );
		        labelTable.put( 21, new JLabel("1000") );
		        speed.setLabelTable( labelTable );

		        speed.setPaintLabels(true);
		        
		        String sel = (String) presets.getSelectedItem();
		        SimWindow.getSim().setLogic(FileManager.getConfigLogic(sel));
		 }
		 
		public HexAntLogic getTableLogic() {
				ConcurrentHashMap<StateKey, HexCommand> hm = new ConcurrentHashMap<>();
			 	hm.put(new StateKey(0,0), new HexCommand((int)rules.getValueAt(0, 2),(int)rules.getValueAt(0, 3),(int)rules.getValueAt(0, 4)));
				hm.put(new StateKey(0,1), new HexCommand((int)rules.getValueAt(1, 2),(int)rules.getValueAt(1, 3),(int)rules.getValueAt(1, 4)));
				hm.put(new StateKey(1,0), new HexCommand((int)rules.getValueAt(2, 2),(int)rules.getValueAt(2, 3),(int)rules.getValueAt(2, 4)));	
				hm.put(new StateKey(1,1), new HexCommand((int)rules.getValueAt(3, 2),(int)rules.getValueAt(3, 3),(int)rules.getValueAt(3, 4)));
		        HexAntLogic hal = new HexAntLogic(hm);		
		        return hal;
		 }
	}
}
