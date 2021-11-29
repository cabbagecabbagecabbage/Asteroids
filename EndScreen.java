import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EndScreen {
	/*
	to do: add title
	*/
	public Button playButton, playButtonHovered;
	private Image title = new ImageIcon("text/title.png").getImage();

	public EndScreen(){
		playButton = new Button(350,270,100,50);
	}

	public void draw(Graphics g, Point mousePosition){
		g.drawImage(title,280,100,240,72,null);

		//play
		playButton.draw(g,mousePosition);
		g.setColor(Color.WHITE);
		g.setFont(GamePanel.f);
		g.drawString("Play Again",358,300);

//		//score
//		g.setFont(GamePanel.f);
//		g.setColor(Color.WHITE);
//		g.drawString("Your Score: "+playerScore,15,25);
	}
}