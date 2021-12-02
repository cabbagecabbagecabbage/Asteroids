import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Alien {
    private static final Random rand = new Random();
    private static final double speed = 2;
    private static final double shootInterval = 3000; //milliseconds
    private final int width, height;
    private final Rectangle rect;
    private final Image img;
    private int x, y;
    private double angle;
    private int vx, vy;
    private double lastShot = System.nanoTime(); //used for limiting shooting speed

    public Alien(int type) {
        if (type == 1) {
            //big saucer
            width = 66;
            height = 44;
            img = new ImageIcon("images/icon-saucer.png").getImage();
        } else {
            //small saucer
            width = 45;
            height = 30;
            img = new ImageIcon("images/icon-small-saucer.png").getImage();
        }
        //generate location outside the screen (casework)
        int startLocationCase = rand.nextInt(4);
        int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3; // for cases
        if (startLocationCase == TOP) {
            //come from top
            y = -height;
            x = rand.nextInt(GamePanel.WIDTH);
        } else if (startLocationCase == LEFT) {
            //come from left
            y = rand.nextInt(GamePanel.HEIGHT);
            x = -width;
        } else if (startLocationCase == BOTTOM) {
            //come from bottom
            y = GamePanel.HEIGHT;
            x = rand.nextInt(GamePanel.WIDTH);
        } else if (startLocationCase == RIGHT) {
            //come from right
            y = rand.nextInt(GamePanel.HEIGHT);
            x = GamePanel.WIDTH;
        }
        angle = rand.nextDouble() * 2 * Math.PI;
        //calculate vx and vy
        vx = (int) Math.round(speed * Math.cos(angle));
        vy = (int) Math.round(speed * Math.sin(angle));
        if (vx == 0) vx = 1;
        if (vy == 0) vy = 1;

        rect = new Rectangle(x, y, width, height);
    }

    public void move() {
        //randomized movement: change direction with 1/25 probability
        if (rand.nextDouble() < 0.04) {
            angle = rand.nextDouble() * Math.PI;
            vx = (int) Math.round(speed * Math.cos(angle));
            vy = (int) Math.round(speed * Math.sin(angle));
        }
        x += vx;
        y += vy;
        rect.setLocation(x, y);

        //wrap around the screen
        if (x < -width) {
            //left to right
            x += GamePanel.WIDTH + width;
            rect.setLocation(x, y);
        }
        if (x > GamePanel.WIDTH) {
            //right to left
            x -= GamePanel.WIDTH;
            rect.setLocation(x, y);
        }
        if (y < -height) {
            //top to bottom
            y += GamePanel.HEIGHT + height;
            rect.setLocation(x, y);
        }
        if (y > GamePanel.HEIGHT) {
            //bottom to top
            y -= GamePanel.HEIGHT;
            rect.setLocation(x, y);
        }

        //shooting bullets periodically
        if ((System.nanoTime() - lastShot) / 1000000 > shootInterval) {
            GamePanel.alienBullets.add(new Bullet(x + width / 2, y + height / 2, Math.atan2(GamePanel.ship.getY() - y, GamePanel.ship.getX() - x)));
            lastShot = System.nanoTime();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }
}
