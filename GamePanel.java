import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	public static final int MENU=0, GAME=1, END=2;
	private int screen = MENU;
	private Point mousePosition;
	private boolean []keys = new boolean[KeyEvent.KEY_LAST+1];
	private Timer timer;
	private Image backgroundImage = new ImageIcon("background/OuterSpace.jpg").getImage();

	public static int width, height;
	
	public static Menu menu = new Menu();
	public static Ship ship = new Ship();

	public static LinkedList<Bullet> bullets = new LinkedList<Bullet>();


	public GamePanel(){
		setPreferredSize( new Dimension(800, 600));
		width = 800;
		height = 600;
        addKeyListener(this);
        addMouseListener(this);
		timer = new Timer(10, this);
		setFocusable(true);
        requestFocus();
		timer.start();
	}

	public void move(){
		ship.move(keys);
		//move bullets;
		for (int i = bullets.size()-1; i >= 0; --i){
			bullets.get(i).move();
			if (bullets.get(i).outOfBounds()){
				bullets.remove(i);
			}
		}
	}

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
			ship.shooting = true;
		}	
	}

	@Override
	public void	mouseReleased(MouseEvent event){
		if (screen == GAME){
			ship.shooting = false;
		}
	}

	@Override
	public void paint(Graphics g){
		g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),null);
		if(screen == MENU){
			menu.paintMenu(g,mousePosition);
		}
		else if(screen == GAME){
			ship.draw(g);
			for (int i = bullets.size()-1; i >= 0; --i){
				bullets.get(i).draw(g);
			}
		}
    }
}