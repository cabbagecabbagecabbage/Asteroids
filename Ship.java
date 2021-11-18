import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Ship {
	/*
	to do: 
	*/
	private double posx = 400, posy = 300;
	private double angle = 0; //radians
	private final double halfTipAngle = 0.32175;
	private final double sideLength = 32.0;
	private final int npoints = 3;
	private Polygon ship;
	//maintain arrays of double coordinates to maintain accuracy
	ArrayList<Double> xpointsDouble = new ArrayList<Double>();
	ArrayList<Double> ypointsDouble = new ArrayList<Double>();

	private double vx = 0, vy = 0;

	public boolean shooting = false;
	public double lastShot = System.nanoTime();
	public final double interval = 100_000_000; //milliseconds

	private int score = 0;

	public Ship(){
		ship = new Polygon();
		xpointsDouble.add(posx + Math.cos(angle)*sideLength/4);
		ypointsDouble.add(posy - Math.sin(angle)*sideLength/4);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(0)),
			(int) Math.round(ypointsDouble.get(0))
		);
		double invAngle = angle - Math.PI;
		xpointsDouble.add(posx + Math.cos(invAngle+halfTipAngle)*sideLength);
		ypointsDouble.add(posy - Math.sin(invAngle+halfTipAngle)*sideLength);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(1)),
			(int) Math.round(ypointsDouble.get(1))
		);
		xpointsDouble.add(posx + Math.cos(invAngle-halfTipAngle)*sideLength);
		ypointsDouble.add(posy - Math.sin(invAngle-halfTipAngle)*sideLength);
		ship.addPoint(
			(int) Math.round(xpointsDouble.get(2)), 
			(int) Math.round(ypointsDouble.get(2))
		);
	}

	public void move(boolean[] keys){
		final double accel = 0.6, decel = 0.93;
		//change v based on key press
		if(keys[KeyEvent.VK_D]){
			vx += accel;
		}
		if(keys[KeyEvent.VK_A]){
			vx -= accel;
		}
		if(keys[KeyEvent.VK_W]){
			vy -= accel;
		}
		if(keys[KeyEvent.VK_S]){
			vy += accel;
		}
		//decelerate
		vx *= decel;
		vy *= decel;

		//rotate
		//find the angle we face using inverse tangent
		angle = Math.atan2(vy,vx);
		posx += vx;
		posy += vy;

		//for each point, obtain the double that it should be, then round to get the lattice coordinates
		xpointsDouble.set(0,posx + Math.cos(angle)*sideLength/4);
		ypointsDouble.set(0,posy + Math.sin(angle)*sideLength/4);

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

		if (shooting){
			if (lastShot + interval < System.nanoTime()){
				GamePanel.bullets.add(new Bullet(ship.xpoints[0],ship.ypoints[0],angle));
				lastShot = System.nanoTime();
			}
		}
	}

	public Point[] getPoints(){
		Point[] points = new Point[ship.npoints];
		for (int i = 0; i < ship.npoints; ++i){
			points[i] = new Point(ship.xpoints[i],ship.ypoints[i]);
		}
		return points;
	}

	public void addScore(int increment){
		score += increment;
	}

	public int getScore(){
		return score;
	}

	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.fillPolygon(ship);
	}
}