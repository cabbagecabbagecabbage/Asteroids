import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	public static final int MENU=0, GAME=1, ENDSCREEN=2;
	private int screen = MENU;
	private Point mousePosition;
	private boolean []keys = new boolean[KeyEvent.KEY_LAST+1];
	private Timer timer;
	private Image backgroundImage = new ImageIcon("background/OuterSpace.jpg").getImage();

	public static final int WIDTH = 800, HEIGHT = 600;
	
	public static Menu menu = new Menu();
	public static EndScreen endscreen = new EndScreen();
	public static Ship ship = new Ship();

	public static LinkedList<Bullet> bullets = new LinkedList<Bullet>();

	public static LinkedList<Asteroid> asteroids = new LinkedList<Asteroid>();
	private double lastAsteroidGen = System.nanoTime();
	private final double asteroidGenInterval = 300_000_000; //milliseconds

	public GamePanel(){
		setPreferredSize( new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        addMouseListener(this);
		setFocusable(true);
        requestFocus();
        timer = new Timer(10, this);
        timer.start();
	}
	
	private void genAsteroid(){
		if (System.nanoTime() - lastAsteroidGen > asteroidGenInterval){
			asteroids.add(new Asteroid());
			lastAsteroidGen = System.nanoTime();
		}
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
		//move asteroids
		for (int i = asteroids.size()-1; i >= 0; --i){
			asteroids.get(i).move();
			if (asteroids.get(i).outOfBounds()){
				asteroids.remove(i);
			}
		}
	}

	private void resetGame(){
		screen = ENDSCREEN;
		bullets.clear();
		asteroids.clear();
		ship = new Ship();
	}

	public boolean checkCollisions(){
		//check player - asteroid collision
		for (int i = 0; i < asteroids.size(); ++i){
			for (Point p: ship.getPoints()){
				if (asteroids.get(i).contains(p)){
					resetGame();
					return true;
				}
			}
		}
		//check bullet - asteroid collision
		//since we replace destroyed with 2 more, just move the iterator instead of doing the iterate backwards trick
		for (int i = 0; i < asteroids.size(); ++i){
			for (int j = 0; j < bullets.size(); ++j){
				if (asteroids.get(i).isHitBy(bullets.get(j))){
					ship.addScore(asteroids.get(i).getType()*5+5);
					if (asteroids.get(i).getType() != Asteroid.SMALL){
						//break down into 2 smaller asteroids
						//spawn at contact point
						int curx = bullets.get(j).getx();
						int cury = bullets.get(j).gety();
						asteroids.add(new Asteroid(asteroids.get(i).getType()+1,curx,cury));
						asteroids.add(new Asteroid(asteroids.get(i).getType()+1,curx,cury));
						asteroids.remove(i--); //removed an object, go back
					}
					else {
						asteroids.remove(i);
					}
					bullets.remove(j);
					break;
				}
			}
		}
		return false;
	}

	//update variables when action is performed
	public void updateVars(){
		mousePosition = MouseInfo.getPointerInfo().getLocation();
		Point offset = getLocationOnScreen();
		mousePosition.translate(-offset.x, -offset.y);
		if(screen == GAME){
			if(checkCollisions()){
				return;
			}
			move();
			genAsteroid();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		updateVars();
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
		else if (screen == ENDSCREEN){
			if (endscreen.hoverRetry(mousePosition)){
				screen = GAME;
			}	
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
			for (int i = 0; i < bullets.size(); ++i){
				bullets.get(i).draw(g);
			}
			for (int i = 0; i < asteroids.size(); ++i){
				asteroids.get(i).draw(g);
			}
		}
		else if (screen == ENDSCREEN){
			endscreen.paintMenu(g,mousePosition);
		}
    }
}