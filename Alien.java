import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Alien {
    private int x,y,width,height;

    private final double speed = 2;
    private double angle;
    private int dx,dy;

    private Rectangle rect;
    private Image img;

    private static Random rand = new Random();

    private double lastShot = System.nanoTime();
    private final double shootInterval = 1000_000_000; //milliseconds

    public Alien(int type){
        if (type == 1){
            //big saucer
            width = 66;
            height = 44;
            img = new ImageIcon("images/icon-saucer.png").getImage();
        }
        else {
            //small saucer
            width = 66;
            height = 44;
            img = new ImageIcon("images/icon-small-saucer.png").getImage();
        }
        //generate location outside the screen (casework)
        int startLocationCase = rand.nextInt(4);
        int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3; // for cases
        if (startLocationCase == TOP){
            //come from top
            y = -height;
            x = rand.nextInt(GamePanel.WIDTH);
            angle = rand.nextDouble()*Math.PI;
        }
        else if (startLocationCase == LEFT){
            //come from left
            y = rand.nextInt(GamePanel.HEIGHT);
            x = -width;
            angle = rand.nextDouble() * Math.PI - Math.PI / 2;
        }
        else if (startLocationCase == BOTTOM){
            //come from bottom
            y = GamePanel.HEIGHT;
            x = rand.nextInt(GamePanel.WIDTH);
            angle = rand.nextDouble()*Math.PI + Math.PI;
        }
        else if (startLocationCase == RIGHT){
            //come from right
            y = rand.nextInt(GamePanel.HEIGHT);
            x = GamePanel.WIDTH;
            angle = rand.nextDouble()*Math.PI + Math.PI / 2;
        }
        dx = (int) Math.round(speed * Math.cos(angle));
        dy = (int) Math.round(speed * Math.sin(angle));
        if (dx == 0) dx = 1;
        if (dy == 0) dy = 1;

        rect = new Rectangle(x,y,width,height);
    }

    public void move(){
        x += dx; y += dy;
        rect.setLocation(x,y);

        //wrap around
        if (x < -width){
            x += GamePanel.WIDTH + width;
            rect.setLocation(x,y);
        }
        if (x > GamePanel.WIDTH){
            x -= GamePanel.WIDTH;
            rect.setLocation(x,y);
        }
        if (y < -height){
            y += GamePanel.HEIGHT + height;
            rect.setLocation(x,y);
        }
        if (y > GamePanel.HEIGHT){
            y -= GamePanel.HEIGHT;
            rect.setLocation(x,y);
        }
        rect = new Rectangle(x,y,width,height);
//        System.out.println(x+" "+y);
        if (lastShot + shootInterval < System.nanoTime()){
//            System.out.println(x+"::"+y);
//            System.out.println(GamePanel.ship.getX()+";;"+GamePanel.ship.getY());
            GamePanel.alienBullets.add(new Bullet(x + width / 2, y + height / 2, Math.atan2(GamePanel.ship.getY()-y,GamePanel.ship.getX()-x)));
            lastShot = System.nanoTime();
        }
    }
    public void draw(Graphics g){
        g.drawImage(img,x,y,width,height,null);
    }

    public Rectangle getRect(){ return rect; }

    public boolean contains(int x, int y){ return rect.contains(x,y); }
}