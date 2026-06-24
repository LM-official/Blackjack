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
 * <p>One {@link Clip} is opened per file and cached, then reused on every play.
 * This avoids leaking native audio lines (a fresh {@link Clip} was previously
 * opened and never closed on each call) and lets background music loop natively
 * with {@link Clip#loop(int)} instead of a self-restarting thread, so soundtracks
 * never stack or overlap across screens.</p>
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class AudioManager {
	/**
	 * The singleton instance of {@link AudioManager}.
	 */
	private static AudioManager instance;
	/**
	 * {@link Map} caching one open {@link Clip} per audio file path.
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
	public static synchronized AudioManager getInstance() {
		if (instance == null) {
			instance = new AudioManager();
		}
		return instance;
	}


	/**
	 * Returns the cached {@link Clip} for the given file, opening and caching it
	 * on first use.
	 *
	 * @param filename The path of the audio file.
	 * @return The open {@link Clip}, or {@code null} if it could not be loaded.
	 */
	private synchronized Clip getClip(String filename) {
		Clip clip = clipMap.get(filename);
		if (clip != null)
			return clip;
		try (InputStream raw = Resources.stream(filename);
		     InputStream in = new BufferedInputStream(raw);
		     AudioInputStream audioIn = AudioSystem.getAudioInputStream(in)) {
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clipMap.put(filename, clip);
			return clip;
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Plays the audio file at the given path once, from the start.
	 *
	 * @param filename The path of the audio file.
	 */
	public synchronized void play(String filename) {
		Clip clip = getClip(filename);
		if (clip == null)
			return;
		clip.stop();
		clip.setFramePosition(0);
		clip.start(); // play once.
	}

	/**
	 * Loops the audio file at the given path continuously.
	 * If it is already running it is left untouched, so calling this repeatedly
	 * (e.g. each time a screen is shown) never starts a second, overlapping loop.
	 *
	 * @param filename The path of the audio file.
	 */
	public synchronized void loop(String filename) {
		Clip clip = getClip(filename);
		if (clip == null || clip.isRunning())
			return;
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Stops the playback of the audio file at the given path.
	 *
	 * @param filename The path of the audio file.
	 */
	public synchronized void stop(String filename) {
		Clip clip = clipMap.get(filename);
		if (clip != null && clip.isRunning())
			clip.stop();
	}


	/**
	 * Checks whether the audio at the given path is playing.
	 *
	 * @param filename The path of the audio file.
	 * @return true if the audio at the given path is playing,
	 * otherwise false.
	 */
	public synchronized boolean isRunning(String filename) {
		Clip clip = clipMap.get(filename);
		return clip != null && clip.isRunning();
	}
}
