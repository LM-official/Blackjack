package view;


import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;


/**
 * Utility for loading resources (images, sounds, data files) from the classpath.
 * This way the application can be launched from any working directory,
 * without depending on relative paths like "./src/...".
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public final class Resources {
	/**
	 * Private constructor: class of static-only utilities.
	 */
	private Resources() {
	}


	/**
	 * Returns the {@link URL} of a classpath resource.
	 *
	 * @param path The absolute path of the resource (e.g. "/img/logo.png").
	 * @return The resource's {@link URL}.
	 */
	public static URL url(String path) {
		return Resources.class.getResource(path);
	}

	/**
	 * Loads an image from the classpath.
	 *
	 * @param path The absolute path of the image (e.g. "/img/logo.png").
	 * @return The loaded {@link ImageIcon}.
	 */
	public static ImageIcon icon(String path) {
		return new ImageIcon(url(path));
	}

	/**
	 * Opens an {@link InputStream} on a classpath resource.
	 *
	 * @param path The absolute path of the resource (e.g. "/sounds/click.wav").
	 * @return The resource's {@link InputStream}.
	 */
	public static InputStream stream(String path) {
		return Resources.class.getResourceAsStream(path);
	}
}