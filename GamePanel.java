import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	public static final int MENU=0, GAME=1, END=2;
	private int screen = MENU;
	private Point mousePosition;
	private boolean []keys = new boolean[KeyEvent.KEY_LAST+1];
	private Timer timer;
	private Image backgroundImage = new ImageIcon("background/OuterSpace.jpg").getImage();
	
	Menu menu = new Menu();
	Ship ship = new Ship();

	public GamePanel(){
		setPreferredSize( new Dimension(800, 600));
        addKeyListener(this);
        addMouseListener(this);
		timer = new Timer(10, this);
		setFocusable(true);
        requestFocus();
		timer.start();
	}

	//game helper functions
	public void move(){
		ship.move(keys);
	}
	//end

	//update variables when action is performed
	public void update(){
		mousePosition = MouseInfo.getPointerInfo().getLocation();
		Point offset = getLocationOnScreen();
		mousePosition.translate(-offset.x, -offset.y);
		if(screen == GAME){
			move();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		update();
		repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent keyEvent){
		int key = keyEvent.getKeyCode();
		keys[key] = false;
	}	
	
	@Override
	public void keyPressed(KeyEvent keyEvent){
		int key = keyEvent.getKeyCode();
		keys[key] = true;
	}
	
	@Override
	public void keyTyped(KeyEvent keyEvent){}
	
	@Override
	public void	mouseClicked(MouseEvent event){}

	@Override
	public void	mouseEntered(MouseEvent event){}

	@Override
	public void	mouseExited(MouseEvent event){}

	@Override
	public void	mousePressed(MouseEvent event){
		if(screen == MENU){
			if (menu.hoverPlay(mousePosition)){
				screen = GAME;
			}	
		}
		else if(screen == GAME){
			//shoot bullets
		}	
	}

	@Override
	public void	mouseReleased(MouseEvent event){}

	@Override
	public void paint(Graphics g){
		g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),null);
		if(screen == MENU){
			menu.paintMenu(g,mousePosition);
		}
		else if(screen == GAME){
			ship.draw(g);
		}
    }
}