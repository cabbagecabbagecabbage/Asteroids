import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener {
    public static final int MENU = 0, GAME = 1, ENDSCREEN = 2;
    public static final int WIDTH = 800, HEIGHT = 600;
    public static int level = 1;
    public static Menu menu = new Menu();
    public static EndScreen endscreen = new EndScreen();
    public static Ship ship = new Ship();
    public static LinkedList<Bullet> bullets = new LinkedList<>();
    public static LinkedList<Asteroid> asteroids = new LinkedList<>();
    public static LinkedList<Alien> aliens = new LinkedList<>();
    public static LinkedList<Bullet> alienBullets = new LinkedList<>();
    public static Font f = new Font("Berlin Sans FB", Font.PLAIN, 18);
    private final int asteroidsPerLevel = 2;
    private final double alienGenInterval = 10000; //milliseconds
    private int screen = MENU;
    private Point mousePosition = new Point();
    private final boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
    private final Timer timer;
    private final Image backgroundImage = new ImageIcon("background/OuterSpace.jpg").getImage();
    private int curAsteroidCount = 0;
    private double lastAlienGen = System.nanoTime();
    private boolean sReleased = true;
    private int playerScore, prevPlayerScore;
    private int lives = 3;
    private final String name = JOptionPane.showInputDialog("Enter a nickname (Cancel to play anonymously)");

    private final SoundEffect[] asteroidBangs = {new SoundEffect("sounds/bangLarge.wav"),
            new SoundEffect("sounds/bangMedium.wav"), new SoundEffect("sounds/bangSmall.wav")};

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocus();
        timer = new Timer(15, this);
        timer.start();
    }

    private void genAsteroid() {
        while (curAsteroidCount < level * asteroidsPerLevel) {
            asteroids.add(new Asteroid());
            ++curAsteroidCount;
        }
    }

    private void genAlien() {
        if ((System.nanoTime() - lastAlienGen) / 1000000 > alienGenInterval && aliens.size() < level / 2) {
            aliens.add(new Alien(1));
            lastAlienGen = System.nanoTime();
        }
    }

    public void moveObjects() {
        if (ship.move(keys)) {
            System.out.println("Self destruct");
            resetGame();
        }
        //move bullets;
        for (int i = bullets.size() - 1; i >= 0; --i) {
            bullets.get(i).move();
            if (bullets.get(i).isExpired()) {
                bullets.remove(i);
            }
        }
        //move alien bullets;
        for (int i = alienBullets.size() - 1; i >= 0; --i) {
            alienBullets.get(i).move();
            if (alienBullets.get(i).isExpired()) {
                alienBullets.remove(i);
            }
        }
        //move asteroids
        for (int i = asteroids.size() - 1; i >= 0; --i) {
            asteroids.get(i).move();
        }
        //move alien
        for (Alien alien : aliens) {
            alien.move();
        }
    }

    private void resetGame() {
        bullets.clear();
        asteroids.clear();
        aliens.clear();
        alienBullets.clear();
        ship = new Ship();
        curAsteroidCount = 0;
        lives = 3;
        level = 1;
        screen = ENDSCREEN;
        HighScore.updateHighScore(playerScore, name);
        prevPlayerScore = playerScore;
        playerScore = 0;
    }

    private boolean respawn() {
        --lives;
        if (lives == 0) {
            //dead
            return true;
        }
        ship = new Ship();
        return false;
    }

    private void asteroidBreak(Asteroid asteroid, int curx, int cury){
        if (asteroid.getType() != Asteroid.SMALL) {
            //break down into 2 smaller asteroids
            //spawn at contact point
            asteroids.add(new Asteroid(asteroid.getType() + 1, curx, cury));
            asteroids.add(new Asteroid(asteroid.getType() + 1, curx, cury));
        }
        asteroids.remove(asteroid);
    }

    public boolean checkCollisions() {
        //check player - asteroid collision, returns true if collided
        for (int i = 0; i < asteroids.size(); ++i) {
            for (Point p: ship.getPoints()) {
                if (asteroids.get(i).contains(p)) {
                    asteroidBangs[asteroids.get(i).getType()].play();
                    playerScore += asteroids.get(i).getType() * 10 + 10;
                    asteroidBreak(asteroids.get(i--),p.x,p.y); //removed -> go back
                    if (respawn()) return true;
                    break;
                }
            }
        }
        //check player - alien collision, returns true if collided
        for (Alien alien : aliens) {
            for (Point p : ship.getPoints()) {
                if (alien.contains(p.x, p.y)) {
                    if (respawn()) {
                        asteroidBangs[0].play();
                        return true;
                    }
                }
                break;
            }
        }
        //check player - alien bullet collision, returns true if collided
        for (Bullet alienBullet : alienBullets) {
            if (ship.contains(alienBullet.getX(), alienBullet.getY())) {
                if (respawn()) {
                    asteroidBangs[0].play();
                    return true;
                }
            }
        }
        //check alien - asteroid collision
        for (int i = asteroids.size() - 1; i >= 0; --i) {
            for (int j = aliens.size() - 1; j >= 0; --j) {
                if (asteroids.get(i).intersects(aliens.get(j).getRect())) {
                    asteroidBangs[asteroids.get(i).getType()].play();
                    asteroidBreak(asteroids.get(i),asteroids.get(i).getX(),asteroids.get(i).getY());
                    aliens.remove(j);
                    break;
                }
            }
        }

        //check alien bullet - asteroid
        for (int i = 0; i < asteroids.size(); ++i) {
            for (int j = 0; j < alienBullets.size(); ++j) {
                if (asteroids.get(i).isHitBy(alienBullets.get(j))) {
                    asteroidBangs[asteroids.get(i).getType()].play();
                    asteroidBreak(asteroids.get(i--),alienBullets.get(j).getX(),alienBullets.get(j).getY());
                    break;
                }
            }
        }

        //check bullet - alien collision
        for (int i = bullets.size() - 1; i >= 0; --i) {
            for (int j = aliens.size() - 1; j >= 0; --j) {
                if (aliens.get(j).contains(bullets.get(i).getX(), bullets.get(i).getY())) {
                    asteroidBangs[2].play();
                    bullets.remove(i);
                    aliens.remove(j);
                    playerScore += 200;
                    break;
                }
            }
        }
        //check bullet - asteroid collision
        //since we replace destroyed with 2 more, just move the iterator
        for (int i = 0; i < asteroids.size(); ++i) {
            for (int j = 0; j < bullets.size(); ++j) {
                if (asteroids.get(i).isHitBy(bullets.get(j))) {
                    asteroidBangs[asteroids.get(i).getType()].play();
                    playerScore += asteroids.get(i).getType() * 10 + 10;
                    asteroidBreak(asteroids.get(i--),bullets.get(j).getX(),bullets.get(j).getY());
                    bullets.remove(j);
                    break;
                }
            }
        }
        return false;
    }

    //update variables when action is performed
    public void updateVars() {
        //get mouse position
        mousePosition = MouseInfo.getPointerInfo().getLocation();
        try {
            Point offset = getLocationOnScreen();
            mousePosition.translate(-offset.x, -offset.y);
        } catch (Exception ignored) {
        }

        if (screen == GAME) {
            if (checkCollisions()) {
                resetGame();
                return;
            }
            if (curAsteroidCount == level * asteroidsPerLevel && asteroids.size() == 0) {
                if (level == 2) {
                    resetGame();
                    return;
                }
                curAsteroidCount = 0;
                ++level;
            }

            moveObjects();
            genAsteroid();
            if (level >= 2) {
                genAlien();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        updateVars();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if (key == KeyEvent.VK_S) {
            sReleased = true;
        }
        if (key == KeyEvent.VK_W) {
            ship.isThrusting = false;
        }
        keys[key] = false;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        keys[key] = true;
        if (sReleased && key == KeyEvent.VK_S) {
            ship.canHyperSpace = true;
            sReleased = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (screen == MENU) {
            if (menu.playButton.hovered(mousePosition)) {
                screen = GAME;
            }
        } else if (screen == GAME) {
            ship.canShoot = true;
        } else if (screen == ENDSCREEN) {
            if (endscreen.playButton.hovered(mousePosition)) {
                screen = GAME;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        if (screen == MENU) {
            menu.draw(g, mousePosition);
        } else if (screen == GAME) {
            ship.draw(g);
            for (Bullet bullet : bullets) {
                bullet.draw(g, Color.YELLOW);
            }
            for (Bullet alienBullet : alienBullets) {
                alienBullet.draw(g, Color.RED);
            }
            for (Asteroid asteroid : asteroids) {
                asteroid.draw(g);
            }
            for (Alien alien : aliens) {
                alien.draw(g);
            }

            //text at the top
            g.setFont(f);
            g.setColor(Color.WHITE);
            g.drawString("Score: " + playerScore + "  ("+ (name == null ? "Anonymous Player" : name)+")", 15, 25);
            g.drawString("High Score: " + HighScore.getHighScore() + "  ("+ HighScore.getHighScorer()+")", 15, 45);
            g.drawString("Level " + level, 740, 25);
            g.drawString("Lives Remaining: " + lives, 340, 25);
        } else if (screen == ENDSCREEN) {
            endscreen.draw(g, mousePosition, prevPlayerScore, name);
        }
    }
}