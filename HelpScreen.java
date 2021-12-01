import java.awt.*;

public class HelpScreen {
    public Button menuButton;

    public HelpScreen() {
        menuButton = new Button(330, 400, 140, 70);
    }

    public void draw(Graphics g, Point mousePosition) {
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.f);
        //instructions
        g.drawString("Welcome to Asteroids!", 30, 30);
        g.drawString("The objective of this game is to score as many points as you can", 30, 50);
        g.drawString("Shooting down a large, medium, or small asteroid gives 10, 20, and 30 points, respectively", 30, 70);
        g.drawString("If you get crash into an asteroid, you lose one of your 3 total lives", 30, 90);
        g.drawString("Whenever you respawn, you have 5 seconds of invulnerability - use this to get to safety", 30, 110);
        g.drawString("At level 2, alien ships will spawn periodically and shoot at you: don't get hit by their bullets either!", 30, 130);
        g.drawString("Shooting down these alien ships will score 200 points", 30, 150);

        g.drawString("Controls", 30, 190);
        g.drawString("W to thrust, A to turn left, D to turn right, S to hyperspace (teleport to a random location on screen)", 30, 210);
        g.drawString("Hyperspace has a chance of self-destructing the ship - use it carefully (max once per second)!", 30, 230);
        g.drawString("Each mouse press shoots one bullet from the tip of your ship (max 1 bullet / 0.5 seconds)", 30, 250);
        g.drawString("Good luck on your mission!", 30, 290);

        //menu button
        menuButton.draw(g, mousePosition);
        g.drawString("Back to Menu", 348, 440);
    }
}