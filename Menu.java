import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu {
	/*
	to do: add title
	*/
	public Button playButton, playButtonHovered;
	private Image title = new ImageIcon("text/title.png").getImage();

	public Menu(){
		playButton = new Button(350,270,100,50);

		// intructionsButton = new Button(350,270,100,50,"buttons/button.png");
		// intructionsButtonHovered = new Button(350,270,100,50,"buttons/buttonHovered.png");

		// playButton = new Button(350,270,100,50,"buttons/button.png");
		// playButtonHovered = new Button(350,270,100,50,"buttons/buttonHovered.png");
	}

	public void draw(Graphics g, Point mousePosition){
		g.drawImage(title,280,100,240,72,null);

		//play
		playButton.draw(g,mousePosition);
		g.setColor(Color.WHITE);
		g.setFont(GamePanel.f);
		g.drawString("Play",383,300);
	}
}