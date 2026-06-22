package view;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * Manages the playback of audio files.
 * Uses the Singleton pattern to guarantee a single instance.
 * 
 * @author ... e Mercuri Lorenzo,
 * @author ... e Matricola: 2145783.
 */
public class AudioManager {
	/**
 * The singleton instance of {@link AudioManager}.
 */
    private static AudioManager instance;
 /**
 * {@link Map} that stores the audio clips being played.
 */
    private Map<String, Clip> clipMap;
    
    
 /**
 * Constructor of the {@link AudioManager} that prevents direct instantiation.
 */
    private AudioManager() {
    	clipMap = new HashMap<>();
    }

 /**
 * Returns the singleton instance of {@link AudioManager}.
 * If the instance does not exist, it creates it.
 *
 * @return The singleton instance of {@link AudioManager}.
 */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    
 /**
 * Plays the audio file at the given path.
 * 
 * @param filename The path of the audio file.
 */
    public void play(String filename) {
        try {
        	InputStream in = new BufferedInputStream(Resources.stream(filename));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clipMap.put(filename, clip); // store in the map.
            clipMap.get(filename).start(); // start the audio.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

 /**
 * Stops the playback of the audio file at the given path.
 * 
 * @param filename The path of the audio file.
 */
    public void stop(String filename) {
        if (clipMap.containsKey(filename)) {
            Clip clip = clipMap.get(filename);
            if (clip.isRunning())
                clip.stop();
        }
    }
    
    
 /**
 * Checks whether the audio at the given path is playing.
 * 
 * @param filename The path of the audio file.
 * @return true if the audio at the given path is playing,
 * otherwise false.
 */
    public boolean isRunning(String filename) {
    	if (clipMap.containsKey(filename))
            if (clipMap.get(filename).isRunning())
            	return true;
    	return false;
    }
}