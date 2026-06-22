package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * Screen to select the number of AI players.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class HomeAI extends JPanel{
	/**
	 * {@link Dimension} of the {@link HomeAI}.
	 */
	static final Dimension SIZE = new Dimension(MainWindow.SIZE.width/5, MainWindow.SIZE.height/5);
	
	/**
	 * {@link Point} of the opening of the {@link HomeAI}.
	 */
	static final Point OPENING_POSITION = new Point((MainWindow.SIZE.width - SIZE.width)/2, (MainWindow.SIZE.height - SIZE.height)/2 - MainWindow.SIZE.height/28);
	
	/**
	 * {@link Color} of the border of the {@link HomeAI}.
	 */
	static final Color BORDER_COLOR = Color.BLACK;
	/**
	 * inner {@link Border} of the {@link HomeAi}.
	 */
	private static final Border INNER_BORDER = BorderFactory.createLineBorder(BORDER_COLOR, MainWindow.SIZE.height/260);
	/**
	 * outer {@link Border}, shadow of the {@link HomeAi}.
	 */
	private static final Border OUTER_BORDER = BorderFactory.createMatteBorder(0, 0, MainWindow.SIZE.height/380, MainWindow.SIZE.height/380, new Color(0, 0, 0, 130));
	/**
	 * {@link Border} of the {@link HomeAi}.
	 */
	static final Border BORDER = BorderFactory.createCompoundBorder(OUTER_BORDER, INNER_BORDER);
	/**
	 * {@link Color} of the background of the {@link HomeAI}.
	 */
	static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	
	/**
	 * {@link Dimension} of the icon of the {@link HomeAI}. 
	 */
	private static final Dimension ICON_SIZE = new Dimension(MainWindow.SIZE.height/70, MainWindow.SIZE.height/70);
	/**
	 * {@link ImageIcon} of the icon of the {@link HomeAI}.
	 */
	static final ImageIcon ICON = new ImageIcon(MainWindow.ICON.getScaledInstance(ICON_SIZE.width, ICON_SIZE.height, Image.SCALE_SMOOTH)); // resize.
	
	/**
	 * {@link JLabel} icon of the title of the {@link HomeAI}.
	 */
	private JLabel titleIcon;
	/**
	 * {@link Font} of the title text of the {@link HomeAI}.
	 */
	static final Font TITLE_FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/50); // use the window font adapting its size.
	/**
	 * {@link JLabel} title of the {@link HomeAI}.
	 */
	private JLabel titleText;
	
	/**
	 * {@link JLabel} text of the {@link HomeAI}.
	 */
	private JLabel text;
	/**
	 * {@link Dimension} of the {@link ImageIcon} of the text of the {@link HomeAI}. 
	 */
	private static final Dimension TEXT_IMAGE_SIZE = new Dimension(MainWindow.SIZE.height/14, MainWindow.SIZE.height/14);
	/**
	 * {@link ImageIcon} of the text of the {@link HomeAI}.
	 */
	private static final ImageIcon TEXT_IMAGE = new ImageIcon(Resources.icon("/img/handWithCards.png").getImage().getScaledInstance(TEXT_IMAGE_SIZE.width, TEXT_IMAGE_SIZE.height, Image.SCALE_SMOOTH)); // resize.
	/**
	 * {@link Font} of the text of the {@link HomeAI}.
	 */
	static final Font TEXT_FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/80); // use the window font adapting its size.
	
	/**
	 * {@link HomeButtonAI} zero.
	 */
	private HomeButtonAI zero;
	/**
	 * {@link HomeButtonAI} one.
	 */
	private HomeButtonAI one;
	/**
	 * {@link HomeButtonAI} two.
	 */
	private HomeButtonAI two;
	/**
	 * {@link HomeButtonAI} three.
	 */
	private HomeButtonAI three;
	
	/**
	 * Constructor of the {@link HomeAI}.
	 */
	public HomeAI() {
		setSize(SIZE);
		setLocation(OPENING_POSITION); // opening position.
		setBorder(BORDER);
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// title:
		JPanel title = new JPanel(new BorderLayout());
		title.setBackground(null);
			// icon:
			titleIcon = new JLabel(ICON);
			titleIcon.setBorder(new EmptyBorder(0, MainWindow.SIZE.height/220, 0, 0));
			title.add(titleIcon, BorderLayout.WEST);
			
			// text
			titleText = new JLabel("AI players");
			titleText.setFont(TITLE_FONT);
			titleText.setForeground(MainWindow.TITLE_COLOR); // text color.
			titleText.setHorizontalAlignment(JLabel.CENTER);
			titleText.setBorder(new EmptyBorder(0, 0, 0, MainWindow.SIZE.height/50));
			title.add(titleText, BorderLayout.CENTER);
		add(title);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/60)));

		// text:
		text = new JLabel("Select the number of AI players");
		text.setIcon(TEXT_IMAGE);
		text.setFont(TEXT_FONT);
		text.setForeground(MainWindow.TEXT_COLOR); // text color.
		text.setAlignmentX(CENTER_ALIGNMENT);
		add(text);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/60)));
		
		// buttons:
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.setBackground(null);
			zero = new HomeButtonAI("0");
			buttons.add(zero);
			one = new HomeButtonAI("1");
			buttons.add(one);
			two = new HomeButtonAI("2");
			buttons.add(two);
			three = new HomeButtonAI("3");
			buttons.add(three);
		add(buttons);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/60)));
	}
}
