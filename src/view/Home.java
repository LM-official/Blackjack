package view;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


/**
 * Initial screen.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Home extends JLayeredPane{
	/**
	 * {@link Dimension} of the logo in the {@link Home}.
	 */
	private static final Dimension LOGO_SIZE = new Dimension(MainWindow.SIZE.width/2, MainWindow.SIZE.height/2);
	/**
	 * {@link ImageIcon} of the logo in the {@link Home}.
	 */
	private static final ImageIcon LOGO = new ImageIcon(Resources.icon("/img/logo.png").getImage().getScaledInstance(LOGO_SIZE.width, LOGO_SIZE.height, Image.SCALE_SMOOTH)); // resize.
	/**
	 * {@link JLabel} with the {@link Home} logo.
	 */
	private JLabel logo;
	
	/**
	 * {@link HomeButton} to start the game.
	 */
	private HomeButton play;
	/**
	 * {@link HomeButton} to exit the application.
	 */
	private HomeButton exit;
	
	/**
 * {@link Font} of the text of the {@link HomeButton}.
 */
    static final Font USER_FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/80); // use the window font adapting its size.
	/**
	 * {@link String} shown in {@link #userText} while no user is logged in.
	 * Used everywhere as the sentinel for the "guest" (not-logged-in) state.
	 */
	static final String GUEST_NAME = "User";
	/**
	 * {@link String} id of the default guest avatar (image {@code /img/avatar/user.png}).
	 */
	static final String GUEST_AVATAR = "user";
	/**
	 * {@link JLabel} with the user text.
	 */
	static JLabel userText;
	/**
	 * {@link HomeButtonUser} opens the {@link AccountRegistered} panel.
	 */
	HomeButtonUser user;
	
	
	/**
	 * Constructor of the {@link Home}.
	 * 
	 * @param nickname The user's nickname.
	 */
	public Home(String nickname) {
		setBackground(null);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// logo:
		logo = new JLabel(LOGO);
		logo.setAlignmentX(CENTER_ALIGNMENT);
		add(logo);
		
		// play:
		user = new HomeButtonUser(nickname);
		play = new HomeButton("Play");
		play.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				add(new HomeAI(), Integer.valueOf(1)); // (high priority on screen).
				play.setEnabled(false); // disable the button while the popup is shown.
				user.setEnabled(false); // disable the button while the popup is shown.
			}
		});
		add(play);
			
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/50)));
			
		// exit:
		exit = new HomeButton("Exit");
		exit.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioManager.getInstance().play("/sounds/click.wav");
				try {
					Thread.sleep(350);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.exit(0); // exit the application.
			}
		});
		add(exit);
			
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/4)));
			
		JPanel boxUser = new JPanel();
		boxUser.setLayout(new BoxLayout(boxUser, BoxLayout.X_AXIS));
		boxUser.setBackground(null);
			//space:
			boxUser.add(Box.createRigidArea(new Dimension((int)(MainWindow.SIZE.width/1.1), 0)));
				
			JPanel boxUser2 = new JPanel();
			boxUser2.setBackground(null);
			boxUser2.setLayout(new BoxLayout(boxUser2, BoxLayout.Y_AXIS));
				// text:
				userText = new JLabel(GUEST_NAME);
				userText.setForeground(MainWindow.TEXT_COLOR);
				userText.setFont(USER_FONT);
				boxUser2.add(userText);
				// user:
				user.addActionListener(new ActionListener() { // on click.
					@Override
					public void actionPerformed(ActionEvent e) {
						add(new Account(), Integer.valueOf(1)); // (high priority on screen).
						play.setEnabled(false);
						user.setEnabled(false); // disable the button while the popup is shown.
					}
				});
				boxUser2.add(user);
			boxUser.add(boxUser2);
		add(boxUser);

		audio();
	}
	
	/**
	 * Loops the {@link Home} soundtrack. The clip is loaded and started off the
	 * EDT (large file); {@link AudioManager#loop} is idempotent, so it never
	 * starts a second, overlapping loop.
	 */
	private void audio() {
	    Thread audioThread = new Thread(() -> {
	        AudioManager.getInstance().stop("/sounds/game.wav");
	        AudioManager.getInstance().loop("/sounds/home.wav");
	    });
	    audioThread.setDaemon(true); // close the thread automatically when the application exits.
	    audioThread.start();
	}
}