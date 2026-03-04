package assignments.Ex3.mypacmangame.display;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    public static void playSound(String fileName) {
        new Thread(() -> {
            try {
                File soundFile = new File(fileName);
                if (!soundFile.exists()) {
                    // Fail silently or print error if needed, prevents crash
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                // e.printStackTrace(); // Uncomment for debugging
            }
        }).start();
    }
}