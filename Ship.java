import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Ship {
    private static final SoundEffect fireSound = new SoundEffect("sounds/fire.wav");
    public static SoundEffect thrustSound = new SoundEffect("sounds/thrust.wav");
    private static final double rotateAngle = Math.PI / 72;
    private static final double accel = 0.20, decel = 0.98;
    private static final double halfTipAngle = Math.PI / 6;
    private static final double sideLength = 20.0;
    private final double spawnTime;
    private static final double shootInterval = 300; //milliseconds
    private static final double hyperSpaceInterval = 1000; //milliseconds
    private static final double immunePeriod = 5000; //milliseconds
    //maintain arrays of double coordinates to maintain accuracy
    private final ArrayList<Double> xpointsDouble = new ArrayList<>();
    private final ArrayList<Double> ypointsDouble = new ArrayList<>();
    public boolean isThrusting = false;
    public boolean canShoot = false;
    public boolean canHyperSpace = false;
    private boolean isImmune = true;
    private double posx = 400, posy = 300;
    private double angle = 3 * Math.PI / 2; //radians
    private Polygon ship;
    private double vx = 0, vy = 0;
    private double lastShot = System.nanoTime();
    private double lastHyperSpace = System.nanoTime();

    public Ship() {
        thrustSound.stop();
        spawnTime = System.nanoTime();
        ship = new Polygon();
        xpointsDouble.add(posx + Math.cos(angle) * sideLength);
        ypointsDouble.add(posy + Math.sin(angle) * sideLength);
        ship.addPoint(
                (int) Math.round(xpointsDouble.get(0)),
                (int) Math.round(ypointsDouble.get(0))
        );
        double invAngle = angle - Math.PI;
        xpointsDouble.add(posx + Math.cos(invAngle + halfTipAngle) * sideLength);
        ypointsDouble.add(posy + Math.sin(invAngle + halfTipAngle) * sideLength);
        ship.addPoint(
                (int) Math.round(xpointsDouble.get(1)),
                (int) Math.round(ypointsDouble.get(1))
        );
        xpointsDouble.add(posx + Math.cos(invAngle - halfTipAngle) * sideLength);
        ypointsDouble.add(posy + Math.sin(invAngle - halfTipAngle) * sideLength);
        ship.addPoint(
                (int) Math.round(xpointsDouble.get(2)),
                (int) Math.round(ypointsDouble.get(2))
        );
    }

    public boolean move(boolean[] keys) {
        //return true if the ship dies

        //change v based on key press
        if (keys[KeyEvent.VK_D]) {
            angle += rotateAngle;
        }
        if (keys[KeyEvent.VK_A]) {
            angle -= rotateAngle;
        }
        if (keys[KeyEvent.VK_W]) {
            if (!isThrusting) {
                thrustSound.loop();
            }
            isThrusting = true;
            vx += accel * Math.cos(angle);
            vy += accel * Math.sin(angle);
        } else {
            thrustSound.stop();
        }
        vx *= decel;
        vy *= decel;

        //move and rotate
        posx += vx;
        posy += vy;

        //for each point, obtain the double that it should be, then round to get the lattice coordinates

        //tip
        xpointsDouble.set(0, posx + Math.cos(angle) * sideLength);
        ypointsDouble.set(0, posy + Math.sin(angle) * sideLength);

        ship.xpoints[0] = (int) Math.round(xpointsDouble.get(0));
        ship.ypoints[0] = (int) Math.round(ypointsDouble.get(0));

        //base vertices
        double invAngle = angle - Math.PI;
        xpointsDouble.set(1, posx + Math.cos(invAngle + halfTipAngle) * sideLength);
        ypointsDouble.set(1, posy + Math.sin(invAngle + halfTipAngle) * sideLength);

        ship.xpoints[1] = (int) Math.round(xpointsDouble.get(1));
        ship.ypoints[1] = (int) Math.round(ypointsDouble.get(1));

        xpointsDouble.set(2, posx + Math.cos(invAngle - halfTipAngle) * sideLength);
        ypointsDouble.set(2, posy + Math.sin(invAngle - halfTipAngle) * sideLength);

        ship.xpoints[2] = (int) Math.round(xpointsDouble.get(2));
        ship.ypoints[2] = (int) Math.round(ypointsDouble.get(2));

        //wrap around
        if (posx < -sideLength) {
            translate(GamePanel.WIDTH + sideLength, 0);
        }
        if (posx > GamePanel.WIDTH + sideLength) {
            translate(-(GamePanel.WIDTH + sideLength), 0);
        }
        if (posy < -sideLength) {
            translate(0, GamePanel.HEIGHT + sideLength);
        }
        if (posy > GamePanel.HEIGHT + sideLength) {
            translate(0, -(GamePanel.HEIGHT + sideLength));
        }

        //shoot bullets: max 5 ono the screen at a time, max 1 shot per 0.3 seconds
        if (canShoot && GamePanel.bullets.size() < 5) {
            if ((System.nanoTime() - lastShot) / 1000000 > shootInterval) {
                fireSound.play();
                GamePanel.bullets.add(new Bullet(ship.xpoints[0], ship.ypoints[0], angle));
                lastShot = System.nanoTime();
                translate(Math.cos(invAngle) * 4, Math.sin(invAngle) * 4);
                canShoot = false;
            }
        }

        //hyperspace: max once per second
        if (canHyperSpace && (System.nanoTime() - lastHyperSpace) / 1000000 > hyperSpaceInterval) {
            vx = 0;
            vy = 0;
            posx = (int) (Math.random() * GamePanel.WIDTH);
            posy = (int) (Math.random() * GamePanel.HEIGHT);
            lastHyperSpace = System.nanoTime();
            canHyperSpace = false;
            if (Math.random() < 0.05) {
                //1 in 20 chance that hyperspace self-destructs the ship
                return true;
            }
        }
        ship = new Polygon(ship.xpoints, ship.ypoints, ship.npoints);

        //check if immunity expired
        if ((System.nanoTime() - spawnTime) / 1000000 > immunePeriod) {
            isImmune = false;
        }
        return false;
    }

    private void translate(double dx, double dy) {
        //translate the points and the centre by dx and dy
        for (int i = 0; i < ship.npoints; ++i) {
            xpointsDouble.set(i, xpointsDouble.get(i) + dx);
            ship.xpoints[i] = (int) Math.round(xpointsDouble.get(i));
            ypointsDouble.set(i, ypointsDouble.get(i) + dy);
            ship.ypoints[i] = (int) Math.round(ypointsDouble.get(i));
        }
        posx += dx;
        posy += dy;
    }

    public Point[] getPoints() {
        //returns the points as well as the center
        Point[] points = new Point[ship.npoints + 1];
        for (int i = 0; i < ship.npoints; ++i) {
            points[i] = new Point(ship.xpoints[i], ship.ypoints[i]);
        }
        points[ship.npoints] = new Point((int) posx, (int) posy);
        return points;
    }

    public int getX() {
        return (int) Math.round(posx);
    }

    public int getY() {
        return (int) Math.round(posy);
    }

    public boolean contains(int x, int y) {
        return ship.contains(x, y);
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void draw(Graphics g) {
        if (isThrusting) {
            //draw flame if thrusting
            g.setColor(Color.RED);
            g.fillOval((ship.xpoints[1] + ship.xpoints[2]) / 2 - 5, (ship.ypoints[1] + ship.ypoints[2]) / 2 - 5, 10, 10);
        }
        g.setColor(Color.GREEN);
        g.fillPolygon(ship);
        if (isImmune) {
            //draw red border if immune
            g.setColor(Color.RED);
            g.drawPolygon(ship);
        }
    }
}