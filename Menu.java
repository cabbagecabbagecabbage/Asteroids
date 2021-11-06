import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu {
	//buttons
	private Button playButton, playButtonHovered;

	public Menu(){
		playButton = new Button(350,300,100,50,"buttons/button.png","Play");
		playButtonHovered = new Button(350,300,100,50,"buttons/buttonHovered.png","Play");
	}

	public void paintMenu(Graphics g, Point mousePosition){
		if (hoverPlay(mousePosition)){
			playButtonHovered.draw(g);
		}
		else {
			playButton.draw(g);
		}
	}

	public boolean hoverPlay(Point mousePosition){
		return playButton.contains(mousePosition);
	}
}