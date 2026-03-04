package assignments.Ex3.mypacmangame.display;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * A utility class responsible for playing sound effects in the game.
 * Uses Java's javax.sound.sampled library to play .wav files.
 * The audio playback runs on a separate thread to prevent blocking the main game loop.
 */
public class AudioPlayer {

    /**
     * Plays a sound file specified by the given file path.
     * This method creates a new thread for each sound to ensure the game
     * continues running smoothly while the audio plays.
     *
     * It checks if the file exists before attempting to play it to avoid errors.
     *
     * @param fileName The relative or absolute path to the audio file (e.g., "sounds/eat.wav").
     */
    public static void playSound(String fileName) {
        new Thread(() -> {
            try {
                File soundFile = new File(fileName);
                if (!soundFile.exists()) {
                    // Fail silently or print error if needed, prevents crash if file is missing
                    return;
                }

                // Open the audio stream and play the clip
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                // In a production environment, we might want to log this error.
                // e.printStackTrace(); // Uncomment for debugging
            }
        }).start();
    }
}