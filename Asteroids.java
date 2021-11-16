import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
menu (title, buttons,etc)
player
asteroids
bullets

unsure:
should menu be a class if theres only going to be one instance?
	should the menu only have static fields and methods?
how to add text? jlabel? drawstring? import image?

consider:
changing rotations of player
rotation of asteroids
make player not go off screen
text
score
levels
add powerups


how the game works:
clicking play sends you to level 1
WASD to move, click/hold to shoot bullets
once you reach 100 points, you proceed automatically to level 2
which has faster asteroids
*/

public class Asteroids extends JFrame{
	GamePanel game = new GamePanel();
	
    public Asteroids() {
		super("Asteroids");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game);
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
    }
    
    public static void main(String[] arguments) {
		Asteroids frame = new Asteroids();
    }
}