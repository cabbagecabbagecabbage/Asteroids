import java.awt.*;
import java.util.Random;

public class Asteroid {
	/*
	to do:
	generate random polygon by generating (r,theta) pairs
	big asteroids move slow
	collision with bullet
	turn into small asteroids
	small asteroids move fast

	always detect asteroid collision with player
	*/


	public static int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3; // for cases
	public static int BIG = 0, MID = 1, SMALL = 2; // for types
	private static Random rand = new Random();

	private Polygon asteroid = new Polygon();

	//location of center
	private int x,y;

	//size
	private int size;

	//movement
	private int dx,dy;
	private double speed;
	private double angle;

	//other properties
	private boolean dead = false;
	private final int type;

	public Asteroid() {
		//constructor for largest asteroid
		//smaller ones have specified location (see next constructor)
		this.type = BIG;

		size = 32;
		speed = 3;

		//generate location outside the screen (casework)
		int startLocationCase = rand.nextInt(4);
		if (startLocationCase == TOP){
			//come from top
			y = -size;
			x = rand.nextInt(GamePanel.WIDTH);
			angle = rand.nextDouble()*Math.PI;
			
		}
		else if (startLocationCase == LEFT){
			//come from left
			y = rand.nextInt(GamePanel.HEIGHT);
			x = -size;
			angle = rand.nextDouble() * Math.PI - Math.PI / 2;
		}
		else if (startLocationCase == BOTTOM){
			//come from bottom
			y = GamePanel.HEIGHT + size;
			x = rand.nextInt(GamePanel.WIDTH);
			angle = rand.nextDouble()*Math.PI + Math.PI;
		}
		else if (startLocationCase == RIGHT){
			//come from right
			y = rand.nextInt(GamePanel.HEIGHT);
			x = GamePanel.WIDTH + size;
			angle = rand.nextDouble()*Math.PI + Math.PI / 2;
		}

		//movement variables
		dx = (int) (speed * Math.cos(angle));
		dy = (int) (speed * Math.sin(angle));

		genPolygon();
	}

	public Asteroid(int type, int x, int y){
		//omit the location generate part, direction is random
		this.type = type;
		this.x = x;
		this.y = y;

		//initialize properties based on type
		if (type == MID){
			size = 16;
			speed = 6;
		}
		else if (type == SMALL){
			size = 8;
			speed = 9;
		}

		//generate angle
		angle = rand.nextDouble()*Math.PI*2;

		//movement variables
		dx = (int) (speed * Math.cos(angle));
		dy = (int) (speed * Math.sin(angle));

		genPolygon();
	}

	private void genPolygon(){
		//generate the polygon's points
		double genAngle = 0;
		while (genAngle < Math.PI * 2){
			genAngle += Math.PI / 3 * rand.nextDouble() * Math.PI / 3;
			asteroid.addPoint(x + (int) (Math.cos(genAngle)*size),y + (int) (Math.sin(genAngle)*size));
		}
	}

	public boolean outOfBounds(){
		return (x < -size || x > GamePanel.WIDTH + size || y < -size || y > GamePanel.HEIGHT + size);
	}

	public int getType(){ return type; }

	public void move(){
		x += dx; y += dy;
		for (int i = 0; i < asteroid.xpoints.length; ++i){
			asteroid.xpoints[i] += dx;
			asteroid.ypoints[i] += dy;
		}
		asteroid = new Polygon(asteroid.xpoints,asteroid.ypoints,asteroid.npoints); //wont work unless a new one is created; hypothesis: modifying xpoints and ypoints does not move the actual polygon (but drawing somehow does not present this behaviour, only .contains())
	}

	public void draw(Graphics g){
		g.setColor(Color.GRAY);
		g.fillPolygon(asteroid);
	}

	public boolean isHitBy(Bullet b){
		return asteroid.contains(b.getx(),b.gety());
	}

	public boolean contains(Point p){
		return asteroid.contains(p);
	}
}