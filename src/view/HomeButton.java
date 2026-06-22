package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;


/**
 * Button of the {@link Home}, the initial screen.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class HomeButton extends JButton{
 /**
 * {@link Dimension} of the {@link HomeButton}.
 */
    static final Dimension SIZE = new Dimension(MainWindow.SIZE.width/8, MainWindow.SIZE.height/16);
    
	/**
	 * {@link Color} of the background of the {@link HomeButton}.
	 */
    static final Color BACKGROUND_COLOR = new Color(0x5b3a29);
	/**
	 * {@link Color} of the border of the {@link HomeButton}.
	 */
    static final Color BORDER_COLOR = new Color(0xcda434);
 /**
 * {@link Border} of the {@link HomeButton}. 
 */
    static final Border BORDER = BorderFactory.createLineBorder(BORDER_COLOR, MainWindow.SIZE.height/220);
    
	/**
	 * {@link Color} when hovering over the {@link HomeButton}.
	 */
    static final Color HOVER_COLOR = new Color(0x3D1C0B);
 /**
 * {@link Color} of the text of the {@link HomeButton}.
 */
    static final Color TEXT_COLOR = Color.WHITE;
 /**
 * {@link Font} of the text of the {@link HomeButton}.
 */
    static final Font FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/35); // use the window font adapting its size.
    
 /**
 * {@link Color} of the background while the {@link HomeButton} is pressed.
 */
    static final Color PRESSED_COLOR = Color.RED;
    
    
 /**
 * Constructor of the {@link HomeButton}.
 * 
 * @param text The button text.
 */
	public HomeButton(String text) {
		super(text); // text.
		setPreferredSize(SIZE);
        setMaximumSize(SIZE);
        setMinimumSize(SIZE);
		setBorder(BORDER);
		setBackground(BACKGROUND_COLOR); 
		setForeground(TEXT_COLOR); // text color.
		setFont(FONT);
		setFocusable(false);
		setAlignmentX(CENTER_ALIGNMENT);
		
		// animations:
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { // action when the cursor is over the button.
            	if (getModel().isEnabled())
            		setBackground(HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) { // action when the cursor is no longer over the button.
            	setBackground(BACKGROUND_COLOR);
            }
            @Override
            public void mouseReleased(MouseEvent e) { // action when the button is released.
            	setBackground(BACKGROUND_COLOR);
            	setCursor(MainWindow.CURSOR);
            }
        });
		
		setUI(new BasicButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) { // action while the button is held down.
            	setBackground(PRESSED_COLOR);
            	setCursor(MainWindow.CURSOR_PRESS);
            }
        });
		
		// audio:
		addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioManager.getInstance().play("/sounds/click.wav");
			}
		});
	}
}
