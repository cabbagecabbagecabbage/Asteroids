import javax.swing.*;

/*
https://games.aarp.org/games/atari-asteroids
https://www.youtube.com/watch?v=w60sfReTsRA

to do:
collision animation
thrust animation
trail animation
shoot sound
lives/respawn?
pause button?
make a good end screen
thrust backwards when shooting (??)
instructions tab?
reconsider where each method should be
make asteroids nicer
tweak some speed values
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