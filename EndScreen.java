import javax.swing.*;
import java.awt.*;

public class EndScreen {
    private final Image title = new ImageIcon("text/title.png").getImage();
    /*
    to do: add title
    */
    public Button playButton, menuButton;

    public EndScreen() {
        playButton = new Button(330, 270, 140, 70);
        menuButton = new Button(330, 370, 140, 70);
    }

    public void draw(Graphics g, Point mousePosition, int playerScore, String name) {
        g.drawImage(title, 280, 100, 240, 72, null);

        //play button
        playButton.draw(g, mousePosition);
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        g.drawString("Play Again", 358, 310);

        //menu button
        menuButton.draw(g, mousePosition);
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        g.drawString("Back to Menu", 348, 410);

        //score
        g.drawString("Your Score: " + playerScore + "  (" + (name == null ? "Anonymous Player" : name) + ")", 15, 25);

        //high score
        g.drawString("High Score: " + HighScore.getHighScore() + "  (" + HighScore.getHighScorer() + ")", 15, 45);
    }
}