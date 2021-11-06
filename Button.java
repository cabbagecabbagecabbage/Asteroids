import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Button{
	private Rectangle buttonRect = new Rectangle(0,0,0,0);
	private Image buttonImage;

	public Button(int x, int y, int w, int h, String imagePath){
		this.buttonRect = new Rectangle(x,y,w,h);
		this.buttonImage = new ImageIcon(imagePath).getImage();
	}

	public boolean contains(Point mousePosition){
		return buttonRect.contains(mousePosition);
	}

	public void draw(Graphics g){
		g.drawImage(buttonImage,
					buttonRect.x,
					buttonRect.y,
					buttonRect.width,
					buttonRect.height,
					null);
	}
}