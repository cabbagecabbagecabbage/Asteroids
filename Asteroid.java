import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid {
    public static final int BIG = 0, MID = 1, SMALL = 2; // for types
    private static final double rotateAngle = Math.PI / 108;
    private static final Random rand = new Random();
    private final double speed = 2 * Math.pow(1.1, GamePanel.level);
    private final int type;
    //maintain arrays of double coordinates to prevent distortion
    private final ArrayList<Double> xpointsDouble = new ArrayList<>();
    private final ArrayList<Double> ypointsDouble = new ArrayList<>();
    private Polygon asteroid = new Polygon();
    //location of center
    private int x, y;
    //size
    private int size;
    //movement
    private int dx, dy;
    private double angle;

    public Asteroid() {
        //constructor for largest asteroid
        //smaller ones have specified location (see next constructor)
        this.type = BIG;
        size = 32;

        //generate location outside the screen (casework)
        int startLocationCase = rand.nextInt(4);
        int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3; // for cases
        if (startLocationCase == TOP) {
            //come from top
            y = -size;
            x = rand.nextInt(GamePanel.WIDTH);
        } else if (startLocationCase == LEFT) {
            //come from left
            y = rand.nextInt(GamePanel.HEIGHT);
            x = -size;
        } else if (startLocationCase == BOTTOM) {
            //come from bottom
            y = GamePanel.HEIGHT + size;
            x = rand.nextInt(GamePanel.WIDTH);
        } else {
            //come from right
            y = rand.nextInt(GamePanel.HEIGHT);
            x = GamePanel.WIDTH + size;
        }
        angle = rand.nextDouble() * 2 * Math.PI;
        //movement variables
        dx = (int) Math.round(speed * Math.cos(angle));
        dy = (int) Math.round(speed * Math.sin(angle));
        //make sure it comes onto the screen
        if (dx == 0) dx = 1;
        if (dy == 0) dy = 1;
        genPolygon();
    }

    public Asteroid(int type, int x, int y) {
        //omit the location generate part, direction is random
        this.type = type;
        this.x = x;
        this.y = y;
        //initialize properties based on type
        if (type == MID) {
            size = 24;
        } else if (type == SMALL) {
            size = 18;
        }
        //generate angle
        angle = rand.nextDouble() * Math.PI * 2;
        //movement variables
        dx = (int) Math.round(speed * Math.cos(angle));
        dy = (int) Math.round(speed * Math.sin(angle));
        genPolygon();
    }

    private void genPolygon() {
        //generate the polygon's points
        //each point is defined by an angle and a magnitude, relative to the original point
        //generate the angle and magnitude within a range
        double genAngle = 0;
        int idx = 0;
        while ((genAngle += Math.PI / 6 + rand.nextDouble() * Math.PI / 4) < Math.PI * 2) {
            xpointsDouble.add(x + Math.cos(genAngle) * (size + (rand.nextInt(2) * 4 - 2)));
            ypointsDouble.add(y + Math.sin(genAngle) * (size + (rand.nextInt(2) * 4 - 2)));
            asteroid.addPoint((int) Math.round(xpointsDouble.get(idx)), (int) Math.round(ypointsDouble.get(idx)));
            ++idx;
        }
    }

    public int getType() {
        return type;
    }

    public void move() {
        //move the position and all the points by dx and dy
        translate(dx, dy);
        //rotate the points about the centre
        for (int i = 0; i < asteroid.npoints; ++i) {
            double newx = xpointsDouble.get(i) - x, newy = (ypointsDouble.get(i) - y);
            double angle = Math.atan2(newy, newx) + rotateAngle;
            double magnitude = Math.sqrt(newx * newx + newy * newy);
            xpointsDouble.set(i, magnitude * Math.cos(angle) + x);
            ypointsDouble.set(i, magnitude * Math.sin(angle) + y);
            asteroid.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
            asteroid.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
        }
        //wrap around the screen
        if (x < -size) {
            translate(GamePanel.WIDTH + size, 0);
        }
        if (x > GamePanel.WIDTH + size) {
            translate(-(GamePanel.WIDTH + size), 0);
        }
        if (y < -size) {
            translate(0, GamePanel.HEIGHT + size);
        }
        if (y > GamePanel.HEIGHT + size) {
            translate(0, -(GamePanel.HEIGHT + size));
        }
        asteroid = new Polygon(asteroid.xpoints, asteroid.ypoints, asteroid.npoints); //collisions wont work unless a new one is created; hypothesis: modifying xpoints and ypoints does not move the actual polygon (but .draw() somehow does not present this behaviour, only .contains())
    }

    private void translate(double dx, double dy) {
        //function to translate points by a agiven dx and dy
        for (int i = 0; i < asteroid.npoints; ++i) {
            xpointsDouble.set(i, xpointsDouble.get(i) + dx);
            asteroid.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
            ypointsDouble.set(i, ypointsDouble.get(i) + dy);
            asteroid.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
        }
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawPolygon(asteroid);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHitBy(Bullet b) {
        return asteroid.contains(b.getX(), b.getY());
    }

    public boolean contains(Point p) {
        return asteroid.contains(p);
    }

    public boolean intersects(Rectangle rect) {
        return asteroid.intersects(rect);
    }
}