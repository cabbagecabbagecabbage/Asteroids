import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class Asteroid {
	/*
	to do:
	*/

	public static int BIG = 0, MID = 1, SMALL = 2; // for types
	private static Random rand = new Random();

	private Polygon asteroid = new Polygon();
	//maintain arrays of double coordinates to prevent distortion
	ArrayList<Double> xpointsDouble = new ArrayList<Double>();
	ArrayList<Double> ypointsDouble = new ArrayList<Double>();

	//location of center
	private int x,y;

	//size
	private int size;

	//movement
	private int dx,dy;
	private final double speed = 2*Math.pow(1.1,GamePanel.level);
	private double angle;
	private static final double rotateAngle = Math.PI / 108;

	//other properties
	private boolean dead = false;
	private final int type;

	public Asteroid() {
		//constructor for largest asteroid
		//smaller ones have specified location (see next constructor)
		this.type = BIG;

		size = 32;

		//generate location outside the screen (casework)
		int startLocationCase = rand.nextInt(4);
		int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3; // for cases
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
		dx = (int) Math.round(speed * Math.cos(angle));
		dy = (int) Math.round(speed * Math.sin(angle));
		if (dx == 0) dx = 1;
		if (dy == 0) dy = 1;

		genPolygon();
	}

	public Asteroid(int type, int x, int y){
		//omit the location generate part, direction is random
		this.type = type;
		this.x = x;
		this.y = y;

		//initialize properties based on type
		if (type == MID){
			size = 24;
		}
		else if (type == SMALL){
			size = 18;
		}

		//generate angle
		angle = rand.nextDouble()*Math.PI*2;

		//movement variables
		dx = (int) Math.round(speed * Math.cos(angle));
		dy = (int) Math.round(speed * Math.sin(angle));

		genPolygon();
	}

	private void genPolygon(){
		//generate the polygon's points
		double genAngle = 0;
		while (genAngle < Math.PI * 2){
			genAngle += Math.PI / 3 * rand.nextDouble() * Math.PI / 3;
			asteroid.addPoint(x + (int) Math.round(Math.cos(genAngle)*size),y + (int) Math.round(Math.sin(genAngle)*size));
			xpointsDouble.add(x + Math.cos(genAngle)*size);
			ypointsDouble.add(y + Math.sin(genAngle)*size);
		}
	}

	public int getType(){ return type; }

	public void move(){
		x += dx; y += dy;
		for (int i = 0; i < asteroid.npoints; ++i){
			asteroid.xpoints[i] += dx;
			asteroid.ypoints[i] += dy;
			xpointsDouble.set(i,xpointsDouble.get(i) + dx);
			ypointsDouble.set(i,ypointsDouble.get(i) + dy);
		}
		for (int i = 0; i < asteroid.npoints; ++i){
			double newx = xpointsDouble.get(i)-x, newy = (ypointsDouble.get(i)-y);
			double angle = Math.atan2(newy,newx)+rotateAngle;
			double magnitude = Math.sqrt(newx*newx+newy*newy);
			xpointsDouble.set(i,magnitude*Math.cos(angle) + x);
			ypointsDouble.set(i,magnitude*Math.sin(angle) + y);
			asteroid.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
			asteroid.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
		}

		//wrap around
		if (x < -size){
			for (int i = 0; i < asteroid.npoints; ++i) {
				xpointsDouble.set(i, xpointsDouble.get(i) + GamePanel.WIDTH + size);
				asteroid.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
			}
			x += GamePanel.WIDTH + size;
		}
		if (x > GamePanel.WIDTH + size){
			for (int i = 0; i < asteroid.npoints; ++i) {
				xpointsDouble.set(i, xpointsDouble.get(i) - GamePanel.WIDTH - size);
				asteroid.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
			}
			x -= GamePanel.WIDTH + size;
		}
		if (y < -size){
			for (int i = 0; i < asteroid.npoints; ++i) {
				ypointsDouble.set(i, ypointsDouble.get(i) + GamePanel.HEIGHT + size);
				asteroid.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
			}
			y += GamePanel.HEIGHT + size;
		}
		if (y > GamePanel.HEIGHT + size){
			for (int i = 0; i < asteroid.npoints; ++i) {
				ypointsDouble.set(i, ypointsDouble.get(i) - GamePanel.HEIGHT - size);
				asteroid.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
			}
			y -= GamePanel.HEIGHT + size;
		}
		asteroid = new Polygon(asteroid.xpoints,asteroid.ypoints,asteroid.npoints); //collisions wont work unless a new one is created; hypothesis: modifying xpoints and ypoints does not move the actual polygon (but .draw() somehow does not present this behaviour, only .contains())
//		System.out.println(x+" "+y);
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

	public boolean intersects(Rectangle rect){return asteroid.intersects(rect);}
}