import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
menu (title, buttons,etc)
player
asteroids
bullets

unsure:
how to add text? jlabel? drawstring? import image?

consider:
fading text of level 1 and level 2
make player not go off screen
instruction
text
reconsider where each method should be
score
levels
sound effects
add powerups
make asteroids nicer
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