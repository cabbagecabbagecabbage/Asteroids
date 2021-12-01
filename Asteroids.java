import javax.swing.*;

/*
to do:
COMMENTS
getters and setters
reconsider where each method should be
*/

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