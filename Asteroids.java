import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
Checklist
menu (buttons,etc)
player
asteroids
bullets

unsure:
should menu be a class if theres only going to be one instance?
	should the menu only have static fields and methods?
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