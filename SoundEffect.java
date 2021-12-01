import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/*
code from Mr.Mackenzie
I just added the loop method
*/

public class SoundEffect {
    private Clip c;

    public SoundEffect(String filename) {
        setClip(filename);
    }

    public void setClip(String filename) {
        try {
            File f = new File(filename);
            c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(f));
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public void loop() {
        //loop indefinitely, for thrust sound
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play() {
        c.setFramePosition(0);
        c.start();
    }

    public void stop() {
        c.stop();
    }
}
