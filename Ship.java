import java.awt.*;
import java.awt.event.*;

public class Ship {
	/*
	to do: 
	*/
	private int posx, posy;
	private double angle = 0; //radians
	private final double halfTipAngle = 0.32175;
	private final double sideLength = 32.0;
	private final int npoints = 3;
	private Polygon ship;

	private double vx = 0, vy = 0;

	public boolean shooting = false;
	public double lastShot = System.nanoTime();
	public final double interval = 100_000_000; //milliseconds

	private int score = 0;

	public Ship(){
		ship = new Polygon();
		ship.addPoint(400,290);
		double invAngle = angle - Math.PI;
		ship.addPoint(	400 + (int)(Math.cos(invAngle+halfTipAngle)*sideLength),
						290 - (int) (Math.sin(invAngle+halfTipAngle)*sideLength));
		ship.addPoint(	400 + (int)(Math.cos(invAngle-halfTipAngle)*sideLength),
						290 - (int) (Math.sin(invAngle-halfTipAngle)*sideLength));
	}

	public void move(boolean[] keys){
		final double accel = 0.6;
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
		vx *= 0.93;
		vy *= 0.93;

		//rotate
		angle = Math.atan2(vy,vx);
		ship.xpoints[0] += (Math.abs(vx) > 1 ? vx : 0);
		ship.ypoints[0] += (Math.abs(vy) > 1 ? vy : 0);
		double invAngle = angle - Math.PI;
		ship.xpoints[1] = ship.xpoints[0] + (int) (Math.cos(invAngle+halfTipAngle)*sideLength);
		ship.ypoints[1] = ship.ypoints[0] + (int) (Math.sin(invAngle+halfTipAngle)*sideLength);

		ship.xpoints[2] = ship.xpoints[0] + (int) (Math.cos(invAngle-halfTipAngle)*sideLength);
		ship.ypoints[2] = ship.ypoints[0] + (int) (Math.sin(invAngle-halfTipAngle)*sideLength);

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
		System.out.println(score);
	}

	public int getScore(){
		return score;
	}

	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.fillPolygon(ship);
	}
}