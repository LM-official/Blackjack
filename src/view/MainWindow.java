package view;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


/**
 * The application's main window.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class MainWindow extends JFrame {
	/**
	 * {@link Image} of the icon of the {@link MainWindow}.
	 */
	static final Image ICON = Resources.icon("/img/icon.png").getImage();
	
	/**
	 * {@link String} name of the {@link MainWindow}.
	 */
	private static final String NAME = "JBlackJack";
	
	/**
	 * {@link Dimension} of the {@link MainWindow} (full screen).
	 */
	static final Dimension SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**
	 * {@link Color} of the background of the {@link MainWindow}.
	 */
	static final Color BACKGROUND_COLOR = new Color(0x35654d);
	
	/**
	 * {@link Image} of the cursor in the {@link MainWindow}.
	 */
	static final Cursor CURSOR = createCursor("/img/cursors/pointer.png", "pointer");
	/**
	 * {@link Image} of the cursor when pressed in the {@link MainWindow}.
	 */
	static final Cursor CURSOR_PRESS = createCursor("/img/cursors/press.png", "press");

	/**
	 * Creates a custom cursor by resizing the image to the size
	 * supported by the operating system (the source images are much larger
	 * than a cursor size, otherwise the cursor would look giant).
	 *
	 * @param path The classpath path of the cursor image.
	 * @param name The cursor name.
	 * @return The custom, resized {@link Cursor}.
	 */
	private static Cursor createCursor(String path, String name) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension size = toolkit.getBestCursorSize(32, 32); // cursor size supported by the OS.
		Image original = Resources.icon(path).getImage();
		// new ImageIcon(...) forces synchronous loading of the resized image.
		Image scaled = new ImageIcon(original.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH)).getImage();
		return toolkit.createCustomCursor(scaled, new Point(0, 0), name); // image, hotspot pixel.
	}
	
	/**
	 * {@link Font} used in the {@link MainWindow}.
	 */
	static final Font FONT = new Font("Comic Sans MS", Font.BOLD, 1);
	/**
	 * {@link Color} of the text in the {@link MainWindow}.
	 */
	static final Color TEXT_COLOR = Color.BLACK;
	/**
	 * {@link Color} of the title in the {@link MainWindow}.
	 */
	static final Color TITLE_COLOR = Color.RED;
	
	/**
	 * {@link Home}, the initial screen.
	 */
	private Home home;
	
	
	/**
	 * Constructor of the {@link MainWindow}.
	 */
	public MainWindow() {
		setIconImage(ICON);
		setTitle(NAME);
		setSize(SIZE.width, SIZE.height);
		setResizable(false);
		setUndecorated(true); // remove the title bar.
		getContentPane().setBackground(BACKGROUND_COLOR); // background.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the program on close.
		setCursor(CURSOR);
		
		home = new Home(Home.GUEST_AVATAR);
		add(home);
		setVisible(true);
	}
}