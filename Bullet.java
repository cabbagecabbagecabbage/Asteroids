import java.awt.*;

public class Bullet {
	//to be fixed: not firing going up or left
	private int x,y;
	private int radius = 2;

	//movement
	private double vx, vy;
	private double angle;
	private final double scalar = 10;

	public Bullet (int x, int y, double angle){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.vx = Math.cos(angle) * scalar;
		this.vy = Math.sin(angle) * scalar;
	}

	public boolean outOfBounds(){
		return (x < -radius || x > GamePanel.width + radius || y < -radius || y > GamePanel.height + radius);
	}

	public void move(){
		x += vx;
		y += vy;
	}

	public void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
	}
}