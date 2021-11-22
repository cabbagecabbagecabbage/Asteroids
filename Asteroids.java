import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
https://www.youtube.com/watch?v=w60sfReTsRA

consider:
ships that shoot at u
hyperspace

thrust backwards when shooting (??)

sound and visual effects, trailing stuff
wrap
make player not go off screen
instruction
reconsider where each method should be
make asteroids nicer
tweek some speed values
*/

public class Asteroids extends JFrame{
	GamePanel game = new GamePanel();
	
    public Asteroids() {
		super("Asteroids");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game);
		pack();
		setVisible(true);
//		setResizable(false);
		setLocationRelativeTo(null);
    }
    
    public static void main(String[] arguments) {
		Asteroids frame = new Asteroids();
    }
}