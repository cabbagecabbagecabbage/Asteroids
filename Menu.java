import javax.swing.*;
import java.awt.*;

public class Menu {
    private static final Image title = new ImageIcon("text/title.png").getImage();
    public static Button playButton, helpButton;

    public Menu() {
        playButton = new Button(330, 270, 140, 70);
        helpButton = new Button(330, 370, 140, 70);
    }

    public void draw(Graphics g, Point mousePosition) {
        //title
        g.drawImage(title, 280, 100, 240, 72, null);

        //play
        playButton.draw(g, mousePosition);
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        g.drawString("Play", 383, 310);

        //help button
        helpButton.draw(g, mousePosition);
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        g.drawString("Help", 383, 410);
    }
}