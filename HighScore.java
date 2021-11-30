import java.io.*;
import java.util.Scanner;

public class HighScore {
    private static Scanner input;
    private static PrintWriter output;

    public static int getHighScore() {
        try {
            input = new Scanner(new BufferedReader(new FileReader("highScore.txt")));
            int ret = (input.hasNextInt() ? input.nextInt() : 0);
            input.close();
            return ret;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read from highScore.txt", e);
        }
    }

    public static String getHighScorer() {
        try {
            input = new Scanner(new BufferedReader(new FileReader("highScorer.txt")));
            String ret = (input.hasNext() ? input.nextLine() : "Anonymous Player");
            input.close();
            return ret;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read from highScorer.txt", e);
        }
    }

    public static void updateHighScore(int score, String name) {
        try {
            if (getHighScore() < score) {
                output = new PrintWriter(new BufferedWriter(new FileWriter("highScore.txt")));
                output.print(score);
                output.close();
                output = new PrintWriter(new BufferedWriter(new FileWriter("highScorer.txt")));
                output.print(name == null ? "Anonymous Player" : name);
                output.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to highScore.txt", e);
        }
    }
}
