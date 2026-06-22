package view;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * Screen with the user's information.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class AccountRegistered extends JPanel{
	
	/**
	 * invisible inner {@link Border}.
	 */
	private static final Border INNER_BORDER = new EmptyBorder(0, MainWindow.SIZE.height/220, MainWindow.SIZE.height/220, MainWindow.SIZE.height/220);
	/**
	 * outer {@link Border}.
	 */
	private static final Border OUTER_BORDER = HomeAI.BORDER;
	/**
	 * {@link Border} of the {@link AccountRegistered}.
	 */
	static final Border BORDER = BorderFactory.createCompoundBorder(OUTER_BORDER, INNER_BORDER);
	
	
	/**
	 * {@link JLabel} icon of the title.
	 */
	private JLabel titleIcon;
	/**
	 * {@link JLabel} title.
	 */
	private JLabel titleText;
	
	
	/**
	 * {@link String} array with all the account information.
	 */
	private String[] data;
	
	/**
	 * {@link JLabel} showing the nickname. 
	 */
	private JLabel nickname;
	/**
	 * {@link JLabel} showing the nickname value. 
	 */
	private JLabel nicknameValue;
	/**
	 * {@link JLabel} showing the games played. 
	 */
	private JLabel gamesPlayed;
	/**
	 * {@link JLabel} showing the number of games played. 
	 */
	private JLabel gamesPlayedValue;
	/**
	 * {@link JLabel} showing the games won. 
	 */
	private JLabel gamesWon;
	/**
	 * {@link JLabel} showing the number of games won. 
	 */
	private JLabel gamesWonValue;
	/**
	 * {@link JLabel} showing the games lost. 
	 */
	private JLabel gamesLost;
	/**
	 * {@link JLabel} showing the number of games lost. 
	 */
	private JLabel gamesLostValue;
	/**
	 * {@link JLabel} showing the level. 
	 */
	private JLabel level;
	/**
	 * {@link JLabel} showing the level value. 
	 */
	private JLabel levelValue;
	
	/**
	 * {@link GameButton} that closes the screen.
	 */
	private GameButton close;
	/**
	 * {@link GameButton} that logs the user out.
	 */
	private GameButton logout;
	
	
	/**
	 * Constructor of the {@link AccountRegistered}.
	 * 
	 * @param accountNickname The user's nickname.
	 */
	public AccountRegistered(String accountNickname) {
		setSize(Account.SIZE);
		setLocation(Account.OPENING_POSITION); // opening position.
		setBorder(BORDER);
		setBackground(Account.BACKGROUND_COLOR);
		setLayout(new GridLayout(0,2));// x rows, 2 columns
		data = getData(accountNickname);
		
		// title:
		titleIcon = new JLabel(HomeAI.ICON);
		titleIcon.setBorder(new EmptyBorder(0, 0, 0, (int)(MainWindow.SIZE.height/5.8)));
		add(titleIcon, BorderLayout.WEST);
				
		// text
		titleText = new JLabel("Statistics");
		titleText.setFont(HomeAI.TITLE_FONT);
		titleText.setForeground(MainWindow.TITLE_COLOR); // text color.
		add(titleText, BorderLayout.CENTER);
		
		
		// nickname:
		nickname = new JLabel("Nickname:");
		nickname.setFont(Account.TEXT_FONT);
		nickname.setForeground(MainWindow.TEXT_COLOR);
		add(nickname);
		nicknameValue = new JLabel(data[0]);
		nicknameValue.setFont(Account.TEXT_FONT);
		nicknameValue.setForeground(MainWindow.TEXT_COLOR);
		add(nicknameValue);
		
		// partite giocate:
		gamesPlayed = new JLabel("Games played:");
		gamesPlayed.setFont(Account.TEXT_FONT);
		gamesPlayed.setForeground(MainWindow.TEXT_COLOR);
		add(gamesPlayed);
		gamesPlayedValue = new JLabel(data[3]);
		gamesPlayedValue.setFont(Account.TEXT_FONT);
		gamesPlayedValue.setForeground(MainWindow.TEXT_COLOR);
		add(gamesPlayedValue);
		
		// partite vinte:
		gamesWon = new JLabel("Games won:");
		gamesWon.setFont(Account.TEXT_FONT);
		gamesWon.setForeground(MainWindow.TEXT_COLOR);
		add(gamesWon);
		gamesWonValue = new JLabel(data[4]);
		gamesWonValue.setFont(Account.TEXT_FONT);
		gamesWonValue.setForeground(MainWindow.TEXT_COLOR);
		add(gamesWonValue);
		
		// partite perse:
		gamesLost = new JLabel("Games lost:");
		gamesLost.setFont(Account.TEXT_FONT);
		gamesLost.setForeground(MainWindow.TEXT_COLOR);
		add(gamesLost);
		gamesLostValue = new JLabel(data[5]);
		gamesLostValue.setFont(Account.TEXT_FONT);
		gamesLostValue.setForeground(MainWindow.TEXT_COLOR);
		add(gamesLostValue);
		
		// level:
		level = new JLabel("Level:");
		level.setFont(Account.TEXT_FONT);
		level.setForeground(MainWindow.TEXT_COLOR);
		add(level);
		levelValue = new JLabel(data[7]);
		levelValue.setFont(Account.TEXT_FONT);
		levelValue.setForeground(MainWindow.TEXT_COLOR);
		add(levelValue);
	
		
		// close:
		close = new GameButton("Close");
		close.setPreferredSize(Account.BUTTONS_SIZE);
		close.setMaximumSize(Account.BUTTONS_SIZE);
		close.setMinimumSize(Account.BUTTONS_SIZE);
		close.setFont(Account.BUTTONS_FONT);
		close.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(AccountRegistered.this);
				JPanel mainPanel = (JPanel) window.getContentPane();
                mainPanel.removeAll();
                mainPanel.add(new HomeRegistered(accountNickname));
                mainPanel.revalidate(); // notify the layout manager that the structure changed.
                mainPanel.repaint(); // repaint.
			}
		});
		add(close);
		
		// logout:
		logout = new GameButton("Log out");
		logout.setPreferredSize(Account.BUTTONS_SIZE);
		logout.setMaximumSize(Account.BUTTONS_SIZE);
		logout.setMinimumSize(Account.BUTTONS_SIZE);
		logout.setFont(Account.BUTTONS_FONT);
		logout.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(AccountRegistered.this);
				JPanel mainPanel = (JPanel) window.getContentPane();
                mainPanel.removeAll();
                mainPanel.add(new Home("user"));
                mainPanel.revalidate(); // notify the layout manager that the structure changed.
                mainPanel.repaint(); // repaint.
			}
		});
		add(logout);
	}
	
	
	/**
	 * Returns the data of the account with the given nickname.
	 * 
	 * @param nickname The account nickname.
	 * @return The account data with nickname passed as a parameter.
	 */
	private String[] getData(String nickname) {
		try (FileReader file = new FileReader(Account.FILE_PATH)) {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String[] credentials = scanner.nextLine().split(":"); // split the row on ':'.
				if (credentials.length >= 8 && nickname.equals(credentials[0]))
					return credentials;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// impossible since it is called only after registering or logging into an account.
		// in any case, on unexpected errors it returns consistent default data (8 fields) so the screen does not crash.
		return new String[] {nickname, "", "0", "0", "0", "0", "0", "1"};
	}
}