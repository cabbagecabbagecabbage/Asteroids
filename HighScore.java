import java.io.*;
import java.util.Scanner;

public class HighScore{
    private static Scanner input;
    private static PrintWriter output;

    public static int getHighScore(){
        try {
            input = new Scanner(new BufferedReader(new FileReader("highScore.txt")));
            int ret = (input.hasNextInt() ? input.nextInt() : 0);
            input.close();
            return ret;
        }
        catch (Exception e){
            throw new RuntimeException("Failed to read from highScore.txt",e);
        }
    }
    public static void updateHighScore(int score) {
        try {
            if (getHighScore() < score) {
                output = new PrintWriter(new BufferedWriter(new FileWriter("highScore.txt")));
                System.out.println("Score update");
                output.print(score);
                output.close();
            }
        }
        catch (Exception e){
            throw new RuntimeException("Failed to write to highScore.txt",e);
        }
    }
}
