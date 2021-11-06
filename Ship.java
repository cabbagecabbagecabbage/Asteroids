import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Ship {
	private int posx, posy;
	private double angle = 0; //radians
	private final int npoints = 3;
	private int[] xPoints = {400,390,410};
	private int[] yPoints = {290,310,310};
	private Polygon ship;

	private double vx = 0, vy = 0;

	public Ship(){
		ship = new Polygon(xPoints,yPoints,npoints);
	}

	public void move(boolean[] keys){
		if(keys[KeyEvent.VK_RIGHT]){
			vx += 1;
		}
		if(keys[KeyEvent.VK_LEFT]){
			vx -= 1;
		}
		if(keys[KeyEvent.VK_UP]){
			vy -= 1;
		}
		if(keys[KeyEvent.VK_DOWN]){
			vy += 1;
		}
		vx *= 0.95;
		vy *= 0.95;
		ship.translate((int) vx, (int) vy);
	}

	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.fillPolygon(ship);
	}
}