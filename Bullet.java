import java.awt.*;

public class Bullet {
    private final double scalar = 8;
    private final int size = 3;
    //movement
    private final double vx, vy;
    private final double maxDist = 500;
    private int x, y;
    //whether it should be removed
    private boolean expired = false;
    private double curDist = 0;

    public Bullet(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.vx = Math.cos(angle) * scalar;
        this.vy = Math.sin(angle) * scalar;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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