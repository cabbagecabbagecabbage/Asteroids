import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	public static final int MENU=0, GAME=1, ENDSCREEN=2;
	public static int level = 1;
	private int screen = MENU;
	private Point mousePosition = new Point();
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
	private final double asteroidGenInterval = 500_000_000; //milliseconds
	private int curAsteroidCount = 0;
	private final int asteroidsPerLevel = 2;

	public static LinkedList<Alien> aliens = new LinkedList<Alien>();

	public static LinkedList<Bullet> alienBullets = new LinkedList<Bullet>();

	public static Font f = new Font("Berlin Sans FB", Font.PLAIN, 18);

	private boolean sReleased = true;

//	private int playerScore, prevPlayerScore;

	public GamePanel(){
		setPreferredSize( new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        addMouseListener(this);
		setFocusable(true);
        requestFocus();
		aliens.add(new Alien(1));
        timer = new Timer(10, this);
        timer.start();
	}
	
	private void genAsteroid(){
		if (System.nanoTime() - lastAsteroidGen > asteroidGenInterval && curAsteroidCount < level*asteroidsPerLevel){
			asteroids.add(new Asteroid());
			lastAsteroidGen = System.nanoTime();
			++curAsteroidCount;
		}
	}

	public void moveObjects(){
		if (ship.move(keys)){
			System.out.println("Self destruct");
			resetGame();
		}
		//move bullets;
		for (int i = bullets.size()-1; i >= 0; --i){
			bullets.get(i).move();
			if (bullets.get(i).isExpired()){
				bullets.remove(i);
			}
		}
		//move alien bullets;
		for (int i = alienBullets.size()-1; i >= 0; --i){
			alienBullets.get(i).move();
			if (alienBullets.get(i).isExpired()){
				alienBullets.remove(i);
			}
		}
		//move asteroids
		for (int i = asteroids.size()-1; i >= 0; --i){
			asteroids.get(i).move();
		}
		//move alien
		for (int i = 0; i < aliens.size(); ++i){
			aliens.get(i).move();
		}
	}

	private void resetGame(){
		bullets.clear();
		asteroids.clear();
		aliens.clear();
		alienBullets.clear();
		ship = new Ship();
		curAsteroidCount = 0;
		level = 1;
		screen = ENDSCREEN;
//		prevPlayerScore = playerScore;
//		playerScore = 0;
	}

	public boolean checkCollisions(){
		//check player - asteroid collision, returns true if collided
		for (int i = 0; i < asteroids.size(); ++i){
			for (Point p: ship.getPoints()){
				if (asteroids.get(i).contains(p)) {
					return true;
				}
			}
		}
		//check player - alien collision, returns true if collided
		for (int i = 0; i < aliens.size(); ++i){
			for (Point p: ship.getPoints()){
				if (aliens.get(i).contains(p.x,p.y)) {
					return true;
				}
			}
		}
		//check player - alien bullet collision, returns true if collided
		for (int i = 0; i < alienBullets.size(); ++i){
			if (ship.contains(alienBullets.get(i).getx(),alienBullets.get(i).gety())){
				return true;
			}
		}
		//check alien - asteroid collision
		for (int i = asteroids.size()-1; i >= 0; --i){
			for (int j = aliens.size()-1; j >= 0; --j){
				if (asteroids.get(i).intersects(aliens.get(j).getRect())){
					asteroids.remove(i);
					aliens.remove(j);
					break;
				}
			}
		}

		//check alien bullet - asteroid
		for (int i = 0; i < asteroids.size(); ++i){
			for (int j = 0; j < alienBullets.size(); ++j){
				if (asteroids.get(i).isHitBy(alienBullets.get(j))){
					if (asteroids.get(i).getType() != Asteroid.SMALL){
						int curx = alienBullets.get(j).getx();
						int cury = alienBullets.get(j).gety();
						asteroids.add(new Asteroid(asteroids.get(i).getType()+1,curx,cury));
						asteroids.add(new Asteroid(asteroids.get(i).getType()+1,curx,cury));
						asteroids.remove(i--); //removed an object, go back
					}
					else {
						asteroids.remove(i);
					}
					alienBullets.remove(j);
					break;
				}
			}
		}

		//check bullet - alien collision
		for (int i = bullets.size()-1; i >= 0; --i){
			for (int j = aliens.size()-1; j >= 0; --j){
				if (aliens.get(j).contains(bullets.get(i).getx(),bullets.get(i).gety())){
					bullets.remove(i);
					aliens.remove(j);
					break;
				}
			}
		}
		//check bullet - asteroid collision
		//since we replace destroyed with 2 more, just move the iterator instead of doing the iterate backwards trick
		for (int i = 0; i < asteroids.size(); ++i){
			for (int j = 0; j < bullets.size(); ++j){
				if (asteroids.get(i).isHitBy(bullets.get(j))){
//					playerScore += asteroids.get(i).getType()*5+5;
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
		try {
			Point offset = getLocationOnScreen();
			mousePosition.translate(-offset.x, -offset.y);
		} catch(Exception e) {};
		if(screen == GAME){
			if(checkCollisions()){
				resetGame();
				return;
			}
			if (curAsteroidCount == level*asteroidsPerLevel && asteroids.size() == 0){
				if (level == 2){
					resetGame();
					return;
				}
				aliens.add(new Alien(1));
				++level;
			}

			moveObjects();
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
		if (key == KeyEvent.VK_S){
			sReleased = true;
		}
		keys[key] = false;
	}	
	
	@Override
	public void keyPressed(KeyEvent keyEvent){
		int key = keyEvent.getKeyCode();
		keys[key] = true;
		if (sReleased == true && key == KeyEvent.VK_S){
			ship.canHyperSpace = true;
			sReleased = false;
		}
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
			if (menu.playButton.hovered(mousePosition)){
				screen = GAME;
			}	
		}
		else if(screen == GAME){
			ship.canShoot = true;
		}
		else if (screen == ENDSCREEN){
			if (endscreen.playButton.hovered(mousePosition)){
				screen = GAME;
			}	
		}
	}

	@Override
	public void	mouseReleased(MouseEvent event){}

	@Override
	public void paint(Graphics g){
		g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),null);
		if(screen == MENU){
			menu.draw(g,mousePosition);
		}
		else if(screen == GAME){
			ship.draw(g);
			for (int i = 0; i < bullets.size(); ++i){
				bullets.get(i).draw(g,Color.YELLOW);
			}
			for (int i = 0; i < alienBullets.size(); ++i){
				alienBullets.get(i).draw(g,Color.RED);
//				System.out.println(alienBullets.get(i).getx()+" "+alienBullets.get(i).gety());
			}
			for (int i = 0; i < asteroids.size(); ++i){
				asteroids.get(i).draw(g);
			}
			for (int i = 0; i < aliens.size(); ++i){
				aliens.get(i).draw(g);
			}
			g.setFont(f);
			g.setColor(Color.WHITE);
//			g.drawString("Score: "+ playerScore,15,25);
			g.drawString("Level "+ level,740,25);
		}
		else if (screen == ENDSCREEN){
//			endscreen.draw(g,mousePosition,prevPlayerScore);
			endscreen.draw(g,mousePosition);
		}
    }
}