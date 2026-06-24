package view;


import java.awt.Component;
import java.awt.GridLayout;
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
	 * Constructor of the {@link AccountRegistered}.
	 *
	 * @param accountNickname The user's nickname.
	 */
	public AccountRegistered(String accountNickname) {
		setSize(Account.SIZE);
		setLocation(Account.OPENING_POSITION); // opening position.
		setBorder(BORDER);
		setBackground(Account.BACKGROUND_COLOR);
		setLayout(new GridLayout(0, 2)); // rows of "label | value", 2 columns.

		String[] data = getData(accountNickname);

		// title:
		JLabel titleIcon = new JLabel(HomeAI.ICON);
		titleIcon.setBorder(new EmptyBorder(0, 0, 0, (int)(MainWindow.SIZE.height/5.8)));
		add(titleIcon);
		JLabel titleText = new JLabel("Statistics");
		titleText.setFont(HomeAI.TITLE_FONT);
		titleText.setForeground(MainWindow.TITLE_COLOR); // text color.
		add(titleText);

		// statistics, one "label | value" row each:
		addText("Nickname:");     addText(data[0]);
		addText("Games played:"); addText(data[3]);
		addText("Games won:");    addText(data[4]);
		addText("Games lost:");   addText(data[5]);
		addText("Level:");        addText(data[7]);

		// buttons:
		GameButton close = accountButton("Close");
		close.addActionListener(e -> replaceContent(new HomeRegistered(accountNickname))); // back to the logged-in home.
		add(close);

		GameButton logout = accountButton("Log out");
		logout.addActionListener(e -> replaceContent(new Home(Home.GUEST_AVATAR))); // back to the anonymous home.
		add(logout);
	}


	/**
	 * Creates a label in the account text style and adds it to the grid.
	 *
	 * @param text The label text.
	 */
	private void addText(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Account.TEXT_FONT);
		label.setForeground(MainWindow.TEXT_COLOR);
		add(label);
	}

	/**
	 * Creates a button sized and styled like the account buttons.
	 *
	 * @param text The button text.
	 * @return The styled {@link GameButton}.
	 */
	private static GameButton accountButton(String text) {
		GameButton button = new GameButton(text);
		button.setPreferredSize(Account.BUTTONS_SIZE);
		button.setMaximumSize(Account.BUTTONS_SIZE);
		button.setMinimumSize(Account.BUTTONS_SIZE);
		button.setFont(Account.BUTTONS_FONT);
		return button;
	}

	/**
	 * Replaces the whole window content with the given screen.
	 *
	 * @param screen The screen to show.
	 */
	private void replaceContent(Component screen) {
		JPanel content = (JPanel) ((MainWindow) SwingUtilities.getWindowAncestor(this)).getContentPane();
		content.removeAll();
		content.add(screen);
		content.revalidate(); // notify the layout manager that the structure changed.
		content.repaint(); // repaint.
	}


	/**
	 * Returns the data of the account with the given nickname.
	 *
	 * @param nickname The account nickname.
	 * @return The account data with nickname passed as a parameter.
	 */
	private String[] getData(String nickname) {
		try (FileReader file = new FileReader(Account.FILE_PATH);
		     Scanner scanner = new Scanner(file)) {
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