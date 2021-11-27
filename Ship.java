import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Ship {
	/*
	to do: 
	*/
	private double posx = 400, posy = 300;
	private double angle = 0; //radians
	private final double rotateAngle = Math.PI / 72;
	private final double accel = 0.1, decel = 0.98;
	private final double halfTipAngle = Math.PI / 6;
	private final double sideLength = 20.0;
	private final int npoints = 3;
	private Polygon ship;
	//maintain arrays of double coordinates to maintain accuracy
	ArrayList<Double> xpointsDouble = new ArrayList<Double>();
	ArrayList<Double> ypointsDouble = new ArrayList<Double>();

	private double vx = 0, vy = 0;

	public boolean canShoot = false;
	private double lastShot = System.nanoTime();
	private final double shootInterval = 100_000_000; //milliseconds

	public boolean canHyperSpace = false;
	private double lastHyperSpace = System.nanoTime();
	private final double hyperSpaceInterval = 1000_000_000; //milliseconds

	public Ship(){
		ship = new Polygon();
		xpointsDouble.add(posx + Math.cos(angle)*sideLength);
		ypointsDouble.add(posy + Math.sin(angle)*sideLength);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(0)),
			(int) Math.round(ypointsDouble.get(0))
		);
		double invAngle = angle - Math.PI;
		xpointsDouble.add(posx + Math.cos(invAngle+halfTipAngle)*sideLength);
		ypointsDouble.add(posy + Math.sin(invAngle+halfTipAngle)*sideLength);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(1)),
			(int) Math.round(ypointsDouble.get(1))
		);
		xpointsDouble.add(posx + Math.cos(invAngle-halfTipAngle)*sideLength);
		ypointsDouble.add(posy + Math.sin(invAngle-halfTipAngle)*sideLength);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(2)), 
			(int) Math.round(ypointsDouble.get(2))
		);
	}

	public boolean move(boolean[] keys){
		//return true if the ship dies

		//change v based on key press
		if(keys[KeyEvent.VK_D]){
			angle += rotateAngle;
		}
		if(keys[KeyEvent.VK_A]){
			angle -= rotateAngle;
		}
		if(keys[KeyEvent.VK_W]) {
			vx += accel * Math.cos(angle);
			vy += accel * Math.sin(angle);
		}
		vx *= decel;
		vy *= decel;
		

		//rotate
		posx += vx;
		posy += vy;

		//for each point, obtain the double that it should be, then round to get the lattice coordinates
		xpointsDouble.set(0,posx + Math.cos(angle)*sideLength);
		ypointsDouble.set(0,posy + Math.sin(angle)*sideLength);

		ship.xpoints[0] = (int) Math.round(xpointsDouble.get(0));
		ship.ypoints[0] = (int) Math.round(ypointsDouble.get(0));

		double invAngle = angle - Math.PI;

		xpointsDouble.set(1,posx + Math.cos(invAngle+halfTipAngle)*sideLength);
		ypointsDouble.set(1,posy + Math.sin(invAngle+halfTipAngle)*sideLength);

		ship.xpoints[1] = (int) Math.round(xpointsDouble.get(1));
		ship.ypoints[1] = (int) Math.round(ypointsDouble.get(1));

		xpointsDouble.set(2,posx + Math.cos(invAngle-halfTipAngle)*sideLength);
		ypointsDouble.set(2,posy + Math.sin(invAngle-halfTipAngle)*sideLength);

		ship.xpoints[2] = (int) Math.round(xpointsDouble.get(2));
		ship.ypoints[2] = (int) Math.round(ypointsDouble.get(2));

		if (posx < -sideLength){
			for (int i = 0; i < ship.npoints; ++i) {
				xpointsDouble.set(i, xpointsDouble.get(i) + GamePanel.WIDTH + sideLength);
				ship.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
			}
			posx += GamePanel.WIDTH + sideLength;
		}
		if (posx > GamePanel.WIDTH + sideLength){
			for (int i = 0; i < ship.npoints; ++i) {
				xpointsDouble.set(i, xpointsDouble.get(i) - GamePanel.WIDTH - sideLength);
				ship.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
			}
			posx -= GamePanel.WIDTH + sideLength;
		}
		if (posy < -sideLength){
			for (int i = 0; i < ship.npoints; ++i) {
				ypointsDouble.set(i, ypointsDouble.get(i) + GamePanel.HEIGHT + sideLength);
				ship.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
			}
			posy += GamePanel.HEIGHT + sideLength;
		}
		if (posy > GamePanel.HEIGHT + sideLength){
			for (int i = 0; i < ship.npoints; ++i) {
				ypointsDouble.set(i, ypointsDouble.get(i) - GamePanel.HEIGHT - sideLength);
				ship.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
			}
			posy -= GamePanel.HEIGHT + sideLength;
		}

		//shoot bullets
		if (canShoot && GamePanel.bullets.size() < 5){
			//can't have more than 5 bullets on the screen
			if (lastShot + shootInterval < System.nanoTime()){
				GamePanel.bullets.add(new Bullet(ship.xpoints[0],ship.ypoints[0],angle));
				lastShot = System.nanoTime();
				canShoot = false;
			}
		}

		//hyperspace
		if (canHyperSpace && lastHyperSpace + hyperSpaceInterval < System.nanoTime()) {
			vx = 0;
			vy = 0;
			posx = (int) (Math.random()*GamePanel.WIDTH);
			posy = (int) (Math.random()*GamePanel.HEIGHT);
			lastHyperSpace = System.nanoTime();
			canHyperSpace = false;
			if (Math.random() < 0.05){
				//1 in 20 chance that hyperspace self-destructs the ship
				return true;
			}
		}
		return false;
	}

	public Point[] getPoints(){
		Point[] points = new Point[ship.npoints+1];
		for (int i = 0; i < ship.npoints; ++i){
			points[i] = new Point(ship.xpoints[i],ship.ypoints[i]);
		}
		points[ship.npoints] = new Point((int) posx, (int) posy);
		return points;
	}

	public int getX(){return (int) Math.round(posx);};
	public int getY(){return (int) Math.round(posy);};

	public boolean contains(int x, int y){ return ship.contains(x,y); }

	public void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillOval((ship.xpoints[1]+ship.xpoints[2])/2-5, (ship.ypoints[1]+ship.ypoints[2])/2-5, 10, 10);
		g.setColor(Color.GREEN);
		g.fillPolygon(ship);
	}
}