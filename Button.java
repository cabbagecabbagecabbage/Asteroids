import javax.swing.*;
import java.awt.*;

public class Button {
    private final Rectangle buttonRect;
    private final Image buttonImage, hoveredImage;

    public Button(int x, int y, int w, int h) {
        this.buttonRect = new Rectangle(x, y, w, h);
        this.buttonImage = new ImageIcon("buttons/button.png").getImage();
        this.hoveredImage = new ImageIcon("buttons/buttonhovered.png").getImage();
    }

    public void draw(Graphics g, Point mousePosition) {
        //draw depending on whether the mouse is hovering over it
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