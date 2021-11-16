import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EndScreen {
	/*
	to do: add title
	*/
	private Button retryButton, retryButtonHovered;

	public EndScreen(){
		retryButton = new Button(350,300,100,50,"buttons/button.png");
		retryButtonHovered = new Button(350,300,100,50,"buttons/buttonHovered.png");
	}

	public void paintMenu(Graphics g, Point mousePosition){
		if (hoverRetry(mousePosition)){
			retryButtonHovered.draw(g);
		}
		else {
			retryButton.draw(g);
		}
	}

	public boolean hoverRetry(Point mousePosition){
		return retryButton.contains(mousePosition);
	}
}