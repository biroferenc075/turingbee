package turingAnt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class SimWindow extends JFrame {
	private static int sWIDTH = 1400;
    private static int sHEIGHT = 1010;
    private static double ZOOM = 1;
    private final static double maxZOOM = 4.0;
    private final static double minZOOM = 0.25;
    private static int offsetX = 0;
    private static int offsetY = 0;
	private static final long serialVersionUID = 1L;
	private static Simulation sim;
	
	private final Color colorAntState0 = new Color(0x000000);
	private final Color colorAntState1 = new Color(0xFFFFFF);
	private final Color colorTile = new Color(0xFCA311);
	private final Color colorGrid = new Color(0xFFFFFF);
	private final Color colorBGR = new Color(0x15223F);

	public SimWindow() {
		Canvas c = new Canvas(this);
		setContentPane(c);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Turing Bee Simulation");
        setLocation(100,0);
        
        sim = new Simulation();
	}
	
	public class Canvas extends JPanel {
		private static final long serialVersionUID = 1L;
		
	    private static BufferedImage img = new BufferedImage(sWIDTH, sHEIGHT, BufferedImage.TYPE_INT_ARGB);
	    
	    public Canvas(JFrame f) {
	        setPreferredSize(new Dimension(sWIDTH, sHEIGHT));
	        setBackground(Color.black);
	        MouseListener mouseListener = new MouseListener();
	        addMouseListener(mouseListener);
	        addMouseMotionListener(mouseListener);
	        addMouseWheelListener(mouseListener);
	        
	        f.addComponentListener(new ComponentAdapter() {
	            public void componentResized(ComponentEvent e) {
	                sWIDTH = e.getComponent().getWidth();
	                sHEIGHT = e.getComponent().getHeight();
	               
	                img = new BufferedImage(sWIDTH, sHEIGHT, BufferedImage.TYPE_INT_ARGB);
	                repaint();
	            }
	        });
	    }
	    
	    private class MouseListener extends MouseInputAdapter {
	    	private Point mousePt = new Point(sWIDTH/2,sHEIGHT/2);
	    	@Override
	        public void mousePressed(MouseEvent e) {
	    		mousePt = e.getPoint();
	    		
	    		repaint();
	        }

	    	@Override
	        public void mouseDragged(MouseEvent e) {
	    		int dx = e.getX() - mousePt.x;
	            int dy = e.getY() - mousePt.y;
	            offsetX+=dx;
	            offsetY+=dy;
	            mousePt = e.getPoint();
	            
	            repaint();
	        }
	    	
	    	@Override
	    	public void mouseWheelMoved(MouseWheelEvent e) {
	    		double rot = e.getPreciseWheelRotation();
	    		if(ZOOM > minZOOM && rot > 0) {
	    			offsetX/=2;
	    			offsetY/=2;
	    			ZOOM/=Math.pow(2.0, rot);
	    		}
	    		else if(ZOOM < maxZOOM && rot < 0) {
	    			offsetX*=2;
	    			offsetY*=2;
	    			ZOOM/=Math.pow(2.0, rot);
	    		}
	    		
	    	}
	    }
	    

	    @Override
	    public void paintComponent(Graphics g) {
	    	Graphics2D g2d = (Graphics2D) g;
	    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	
	    	super.paintComponent(g);
	    	
	    	drawBackground(img.getGraphics());
	    	drawSim(img.getGraphics());
		    drawGrid(img.getGraphics());
		    
		    g.drawImage(img, 0, 0, this);
		    
		    img.getGraphics().clearRect(0, 0, img.getWidth(), img.getHeight());
		    
	        this.repaint();
	   }
	    
	    public void drawBackground(Graphics graphics) {
	    	Graphics2D g = (Graphics2D) graphics;
	        Color tmpC = g.getColor();

	        g.setColor(colorBGR);
	    	g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    	
	    	g.setColor(tmpC);
	    }
	    public void drawGrid(Graphics graphics) {
	    	Graphics2D g = (Graphics2D) graphics;
	    	Stroke tmpS = g.getStroke();
	        Color tmpC = g.getColor();
	    	
	        g.setStroke(new BasicStroke((float) (ZOOM*2+1.5), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
	        g.setColor(colorGrid);
	    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		       
	    	int gwidth = (int) Math.ceil((double)sWIDTH/(75*ZOOM)+6);
	    	int gheight = (int) Math.ceil((double)sHEIGHT/(43*ZOOM)+6);
	    	int goffsetX = (int) (offsetX%(150*ZOOM));
	    	int goffsetY = (int) (offsetY%(86*ZOOM));
			for(int j = 0; Math.abs(j) < gwidth/2; j=j<=0?-j+1:-j) {
				for(int k = 0; Math.abs(k) < gheight/2; k=k<=0?-k+1:-k) {
					int x = (int) (sWIDTH/2+goffsetX+Math.pow(-1,j)*75*(double)(j*ZOOM));
			        int y = (int) (sHEIGHT/2+goffsetY+Math.pow(-1,k)*(double)(86*k+43*j)*ZOOM);
			        
			        int xpoints[] = {(int)(x-(double)50*ZOOM)
	        				,(int)(x-(double)25*ZOOM)
	        				,(int)(x+(double)25*ZOOM)
	        				,(int)(x+(double)50*ZOOM)
	        				,(int)(x+(double)25*ZOOM)
	        				,(int)(x-(double)25*ZOOM)};
			        int ypoints[] = {y,
	        				(int)(y+(double)43*ZOOM),
	        				(int)(y+(double)43*ZOOM),
	        				y,
	        				(int)(y-(double)43*ZOOM),
	        				(int)(y-(double)43*ZOOM)};
			     
			        int npoints = 6;
			        g.drawPolygon(xpoints, ypoints, npoints);
				}
			}
			
			g.setColor(tmpC);
	        g.setStroke(tmpS);
		}
	    public void drawSim(Graphics graphics) {
	    	
	    	HexAnt ant = sim.getAnt();
	    	ConcurrentHashMap<HexTile, Integer> tiles = sim.getTiles();
	    	HexAntLogic logic = sim.getLogic();
	    	
			StateKey key = new StateKey(ant.getState(),sim.getTileState(ant.getTile()));
			HexCommand cmd = logic.getCommand(key);
			Graphics2D g = (Graphics2D) graphics;
			
			for(HexTile t: tiles.keySet()) {
				drawTile(g, t);
			}
			drawAnt(g, ant, cmd);
		}
	    
	    public void drawAnt(Graphics graphics, HexAnt ant, HexCommand cmd) {
	    	Graphics2D g = (Graphics2D) graphics;
	    	HexTile antTile = ant.getTile();
			
			Stroke tmpS = g.getStroke();
	        Color tmpC = g.getColor();
	        
	        if(ant.getState() == 1)
	        	g.setColor(colorAntState0);
	        else if(ant.getState() == 0)
	        	g.setColor(colorAntState1);
	        g.setStroke(new BasicStroke((float) (ZOOM*2+1.5), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        int x1 = antTile.getX();
	        int y1 = antTile.getY();
	        DirectionHex dir = DirectionHex.posX;
			for(int i = 0; i < 6; i++)  {
				if(DirectionHex.values()[i].getVal() == (ant.getDir().getVal()+cmd.getRotation())%6)
				dir = DirectionHex.values()[(ant.getDir().getVal()+cmd.getRotation())%6]; // !!!!
			}
	        double sin = Math.sin(dir.getVal()*Math.PI/3);
	        double cos = Math.cos(dir.getVal()*Math.PI/3+Math.PI);
	        int x = (int)(sWIDTH/2+ZOOM*75*(double)x1+offsetX);
	        int y = (int)(sHEIGHT/2+ZOOM*(double)(86*y1+43*x1)+offsetY);
	        
	        
	        double side = 45;
	        
	        
	        int xpoints[] = {(int)(-side/2*ZOOM),0,(int)(side/2*ZOOM),0};
	        int ypoints[] = {(int)((-Math.sqrt(3)*side/6)*ZOOM),(int)((Math.sqrt(3)*side/3)*ZOOM),(int)((-Math.sqrt(3)*side/6)*ZOOM),0};
	        
	        int xrot[] = {0,0,0,0};
	        int yrot[] = {0,0,0,0};
	        
	        //x'=cos*x+sin*y
	        //y'=-sin*x+cos*y
	        for(int i = 0; i < 4; i++) {
	        	xrot[i]=(int)(cos*xpoints[i]+sin*ypoints[i]+x);
	        	yrot[i]=(int)(-sin*xpoints[i]+cos*ypoints[i]+y);
	        }
	        g.fillPolygon(xrot, yrot, 4);
	        g.setColor(colorGrid);
	        g.drawPolygon(xrot, yrot, 4);

	        g.setColor(tmpC);
	        g.setStroke(tmpS);
	    	
	    }
	    
	    public void drawTile(Graphics graphics, HexTile t) {
	    	Graphics2D g = (Graphics2D) graphics;
			Stroke tmpS = g.getStroke();
	        Color tmpC = g.getColor();
	        
	        g.setColor(colorTile);
	        g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

	        int x1 = t.getX();
	        int y1 = t.getY();
	        int x = (int) (sWIDTH/2+offsetX+75*(double)x1*ZOOM);
	        int y = (int) (sHEIGHT/2+offsetY+(double)(86*y1+43*x1)*ZOOM);

	        int xpoints[] = {(int)(x-(double)50*ZOOM)
	        				,(int)(x-(double)25*ZOOM)
	        				,(int)(x+(double)25*ZOOM)
	        				,(int)(x+(double)50*ZOOM)
	        				,(int)(x+(double)25*ZOOM)
	        				,(int)(x-(double)25*ZOOM)};
	        int ypoints[] = {y,
	        				(int)(y+(double)43*ZOOM),
	        				(int)(y+(double)43*ZOOM),
	        				y,
	        				(int)(y-(double)43*ZOOM),
	        				(int)(y-(double)43*ZOOM)};
	     
	            g.fillPolygon(xpoints, ypoints, 6);

	        g.setColor(tmpC);
	        g.setStroke(tmpS);
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 }
	}
	
	public static int getWindowWidth() {
		return sWIDTH;
	}
	public static int getWindowHeight() {
		return sHEIGHT;
	}
	public static double getZoom() {
		return ZOOM;
	}
	public static int getOffsetX() {
		return offsetX;
	}
	public static int getOffsetY() {
		return offsetY;
	}
	public static Simulation getSim() {
		return sim;
	}
	public static void setWindowWidth(int w) {
		sWIDTH = w;
	}
	public static void setWindowHeight(int h) {
		sHEIGHT = h;
	}
	public static void setZoom(double z) {
		ZOOM = z;
	}
	public static  void  setOffsetX(int x) {
		offsetX = x;
	}
	public static  void  setOffsetY(int y) {
		offsetY = y;
	}
	public static void setSim(Simulation s) {
		sim = s;
	}

}
