import javax.swing.*;
import java.awt.*;

public class Button {
    /*
    to do: add text to buttons
    */
    private Rectangle buttonRect = new Rectangle(0, 0, 0, 0);
    private Image buttonImage, hoveredImage;

    public Button(int x, int y, int w, int h) {
        this.buttonRect = new Rectangle(x, y, w, h);
        this.buttonImage = new ImageIcon("buttons/button.png").getImage();
        this.hoveredImage = new ImageIcon("buttons/buttonhovered.png").getImage();
    }

    public boolean contains(Point mousePosition) {
        return buttonRect.contains(mousePosition);
    }

    public void draw(Graphics g, Point mousePosition) {
        g.drawImage((hovered(mousePosition) ? hoveredImage : buttonImage),
                buttonRect.x,
                buttonRect.y,
                buttonRect.width,
                buttonRect.height,
                null);
    }

    public boolean hovered(Point mousePosition) {
        return buttonRect.contains(mousePosition);
    }
}