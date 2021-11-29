import java.awt.*;

public class Bullet {
    private final double scalar = 8;
    private int x, y;
    private int size = 3;
    //movement
    private double vx, vy;
    private double angle;
    //whether it should be removed
    private boolean expired = false;
    private double maxDist = 500;
    private double curDist = 0;

    public Bullet(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.vx = Math.cos(angle) * scalar;
        this.vy = Math.sin(angle) * scalar;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public void move() {
        x += vx;
        y += vy;
        if (x < -size) {
            x = GamePanel.WIDTH + size;
        }
        if (x > GamePanel.WIDTH + size) {
            x = -size;
        }
        if (y < -size) {
            y = GamePanel.HEIGHT + size;
        }
        if (y > GamePanel.HEIGHT + size) {
            y = -size;
        }
        curDist += Math.hypot(vx, vy);
        if (curDist > maxDist) {
            expired = true;
        }
    }

    public boolean isExpired() {
        return expired;
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillOval(x - size, y - size, 2 * size, 2 * size);
    }
}