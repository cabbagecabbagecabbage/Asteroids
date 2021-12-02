/*
* Author: Rayton Chen
* (Game Template from Mr. McKenzie)
* ICS4U Simple Game Assignment - Asteroids
* */

import javax.swing.*;

public class Asteroids extends JFrame {
    GamePanel game = new GamePanel();

    public Asteroids() {
        super("Asteroids");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        setVisible(true);
        setResizable(false); //not allowing resize
        setLocationRelativeTo(null);
    }

    public static void main(String[] arguments) {
        Asteroids frame = new Asteroids();
    }
}