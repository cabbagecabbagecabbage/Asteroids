import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EndScreen {
    /*
    to do: add title
    */
    public Button playButton;
    private final Image title = new ImageIcon("text/title.png").getImage();

    public EndScreen() {
        playButton = new Button(330, 270, 140, 70);
    }

    public void draw(Graphics g, Point mousePosition, int playerScore) {
        g.drawImage(title, 280, 100, 240, 72, null);

        //play
        playButton.draw(g, mousePosition);
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        g.drawString("Play Again", 358, 310);

		//score
		g.drawString("Your Score: "+playerScore,15,25);

        //high score
        g.drawString("High Score: "+ HighScore.getHighScore(),15,45);
    }
}