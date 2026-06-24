package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * Screen to log in or register a user.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Account extends JPanel{
	/**
	 * {@link Dimension} of the {@link Account}.
	 */
	static final Dimension SIZE = new Dimension((int)(MainWindow.SIZE.width/4.5), (int)(MainWindow.SIZE.height/4.5));
	
	/**
	 * {@link Point} of the opening of the {@link Account}.
	 */
	static final Point OPENING_POSITION = new Point((MainWindow.SIZE.width - SIZE.width)/2, (MainWindow.SIZE.height - SIZE.height)/2 - MainWindow.SIZE.height/25);
	
	/**
	 * {@link Border} of the {@link Account}.
	 */
	static final Border BORDER = HomeAI.BORDER;
	/**
	 * {@link Color} background color of the {@link Account}.
	 */
	static final Color BACKGROUND_COLOR = HomeAI.BACKGROUND_COLOR;
	
	/**
	 * {@link Font} of the text of the {@link Account}.
	 */
	static final Font TEXT_FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/80); // use the window font adapting its size.
	
	/**
	 * {@link Dimension} of the input fields of the {@link Account}.
	 */
	private static final Dimension INPUT_SIZE = new Dimension((int)(SIZE.width/1.7), SIZE.height/9);
	
	/**
 * {@link Dimension} of the buttons of the {@link Account}.
 */
    static final Dimension BUTTONS_SIZE = new Dimension(MainWindow.SIZE.width/27, MainWindow.SIZE.height/27);
 /**
 * {@link Font} of the text of the buttons of the {@link Account}.
 */
    static final Font BUTTONS_FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/80);
    
 /**
 * {@link String} path of the file with the users' credentials.
 * The project's {@code src/accounts.txt} is used, located relative to the
 * directory the compiled classes are loaded from (…/bin -> …/src/accounts.txt),
 * so the game can be launched from any working directory and still updates the
 * project file (no data is written outside the project directory).
 */
    static final String FILE_PATH = initAccountFile();

 /**
 * Resolves the project's {@code src/accounts.txt} and makes sure it exists.
 * On first run it is seeded from the bundled classpath default ("/accounts.txt")
 * and normalized to end with a newline (so appended registrations start on a new line).
 *
 * @return The absolute path of the account file.
 */
    private static String initAccountFile() {
    	Path file = resolveAccountsFile();
    	try {
    		Files.createDirectories(file.getParent());
    		if (!Files.exists(file))
    			try (InputStream seed = Resources.stream("/accounts.txt")) {
    				if (seed != null)
    					Files.copy(seed, file); // copy the default data from the classpath.
    				else
    					Files.createFile(file); // no seed: create an empty file.
    			}
    		ensureTrailingNewline(file); // registrations are appended: the file must end with a newline.
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return file.toString();
    }

 /**
 * Locates the project's {@code src/accounts.txt} independently of the working
 * directory, by walking up from the directory the classes are loaded from
 * (typically {@code …/bin}) until a folder containing {@code src} is found.
 * Falls back to {@code src/accounts.txt} relative to the working directory.
 *
 * @return The path of the project's account file.
 */
    private static Path resolveAccountsFile() {
    	try {
    		URL location = Account.class.getProtectionDomain().getCodeSource().getLocation();
    		Path codeSource = Paths.get(location.toURI());
    		// start from the directory holding the classes (e.g. .../bin), or the jar's folder.
    		Path dir = Files.isDirectory(codeSource) ? codeSource : codeSource.getParent();
    		for (Path d = dir; d != null; d = d.getParent())
    			if (Files.isDirectory(d.resolve("src")))
    				return d.resolve("src").resolve("accounts.txt"); // found the project root.
    	} catch (Exception e) {
    		// code-source unavailable (e.g. unusual class loader): fall back below.
    	}
    	return Paths.get("src", "accounts.txt"); // fallback: relative to the working directory.
    }

 /**
 * Ensures the account file ends with a newline, so that
 * appended registrations always start on a new line (the default data
 * provided as a resource do not end with a newline).
 *
 * @param file The account file.
 * @throws IOException On a read/write error.
 */
    private static void ensureTrailingNewline(Path file) throws IOException {
    	byte[] content = Files.readAllBytes(file);
    	if (content.length > 0 && content[content.length - 1] != '\n')
    		Files.write(file, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
    }
    
 /**
	 * {@link Font} used for the error message.
	 */
	private static final Font ERROR_FONT = new Font(MainWindow.FONT.getFontName(), Font.ITALIC, MainWindow.SIZE.height/100);
	
	
	/**
	 * {@link JLabel} icon of the title of the {@link Account}.
	 */
	private JLabel titleIcon;
	/**
	 * {@link JLabel} title of the {@link Account}.
	 */
	private JLabel titleText;
		

	/**
	 * {@link JLabel} nickname of the {@link Account}.
	 */
	private JLabel nicknameLabel;
	/**
	 * {@link JTextField} nickname entered in the {@link Account}.
	 */
	JTextField nicknameInput;

	/**
	 * {@link JLabel} nickname of the {@link Account}.
	 */
	private JLabel passwordLabel;
	/**
	 * {@link JTextField} nickname entered in the {@link Account}.
	 */
	JPasswordField passwordInput;
	

	/**
	 * {@link GameButton} login.
	 */
	private GameButton login;
	/**
	 * {@link GameButton} sign up.
	 */
	private GameButton signup;
	
	
	/**
	 * {@link JLabel} shown when the fields are required.
	 */
	private JLabel requiredMessage;
	/**
	 * {@link JLabel} shown when the field was left empty.
	 */
	private JLabel nicknameEmpty;
	/**
	 * {@link JLabel} shown when the field was left empty.
	 */
	private JLabel passwordEmpty;
	/**
	 * {@link JLabel} shown when the entered fields do not allow access.
	 */
	private JLabel wrongCredentials;
	/**
	 * {@link JLabel} shown when the account being registered already exists.
	 */
	private JLabel alreadyRegistered;
	/**
	 * {@link JLabel} shown when the password does not meet the standard rules.
	 */
	private JLabel invalidPassword;
	/**
	 * {@link JLabel} shown when the nickname contains the reserved field separator.
	 */
	private JLabel invalidNickname;

	/**
	 * The field {@link String} separator used in the accounts file. It must not
	 * appear inside a nickname or password, otherwise it would corrupt the record.
	 */
	private static final String SEPARATOR = ":";


	/**
	 * Constructor of the {@link Account}.
	 */
	public Account() {
		setSize(SIZE);
		setLocation(OPENING_POSITION); // opening position.
		setBorder(BORDER);
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// title:
		JPanel title = new JPanel(new BorderLayout());
		title.setBackground(null);
			// icon:
			titleIcon = new JLabel(HomeAI.ICON);
			titleIcon.setBorder(new EmptyBorder(0, MainWindow.SIZE.height/220, 0, 0));
			title.add(titleIcon, BorderLayout.WEST);
					
			// text
			titleText = new JLabel("User");
			titleText.setFont(HomeAI.TITLE_FONT);
			titleText.setForeground(MainWindow.TITLE_COLOR); // text color.
			titleText.setHorizontalAlignment(JLabel.CENTER);
			titleText.setBorder(new EmptyBorder(0, 0, 0, MainWindow.SIZE.height/50));
			title.add(titleText, BorderLayout.CENTER);
		add(title);
		
		// nickname:
		JPanel boxNickname = new JPanel();
		boxNickname.setLayout(new BoxLayout(boxNickname, BoxLayout.X_AXIS));
		boxNickname.setBackground(null);
			// label:
			nicknameLabel = new JLabel("Nickname:");
			nicknameLabel.setForeground(MainWindow.TEXT_COLOR);
			nicknameLabel.setFont(TEXT_FONT);
			boxNickname.add(nicknameLabel);
			// textfield:
			nicknameInput = new JTextField();
			nicknameInput.setPreferredSize(INPUT_SIZE);
			nicknameInput.setMaximumSize(INPUT_SIZE);
			nicknameInput.setMinimumSize(INPUT_SIZE);
			nicknameInput.setForeground(MainWindow.TEXT_COLOR);
			nicknameInput.setFont(TEXT_FONT);
			boxNickname.add(nicknameInput);
			
			// empty field:
			nicknameEmpty = new JLabel("*");
			nicknameEmpty.setForeground(BACKGROUND_COLOR); // text color.
			nicknameEmpty.setFont(ERROR_FONT);
			boxNickname.add(nicknameEmpty);
		add(boxNickname);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/100)));
					
		// password:
		JPanel boxPassword = new JPanel();
		boxPassword.setLayout(new BoxLayout(boxPassword, BoxLayout.X_AXIS));
		boxPassword.setBackground(null);
			// label:
			passwordLabel = new JLabel("Password:");
			passwordLabel.setForeground(MainWindow.TEXT_COLOR);
			passwordLabel.setFont(TEXT_FONT);
			boxPassword.add(passwordLabel);
			// textfield:
			passwordInput = new JPasswordField();
			passwordInput.setPreferredSize(INPUT_SIZE);
			passwordInput.setMaximumSize(INPUT_SIZE);
			passwordInput.setMinimumSize(INPUT_SIZE);
			passwordInput.setForeground(MainWindow.TEXT_COLOR);
			passwordInput.setFont(BUTTONS_FONT);
			boxPassword.add(passwordInput);
			
			// empty field:
			passwordEmpty = new JLabel("*");
			passwordEmpty.setForeground(BACKGROUND_COLOR); // text color.
			passwordEmpty.setFont(ERROR_FONT);
			boxPassword.add(passwordEmpty);
		add(boxPassword);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/50)));
        
		// buttons:
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.setBackground(null);
			// login:
			login = new GameButton("Log in");
			login.setPreferredSize(BUTTONS_SIZE);
			login.setMaximumSize(BUTTONS_SIZE);
			login.setMinimumSize(BUTTONS_SIZE);
			login.setFont(BUTTONS_FONT);
			login.addActionListener(new ActionListener() { // on click.
				@Override
				public void actionPerformed(ActionEvent e) {
					if (dataEntered() && isRegistered()) {
						AudioManager.getInstance().play("/sounds/ok.wav");
						MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(Account.this);
						JPanel mainPanel = (JPanel) window.getContentPane();
                        mainPanel.removeAll();
                        mainPanel.add(new HomeRegistered(nicknameInput.getText()));
                        mainPanel.revalidate(); // notify the layout manager that the structure changed.
                        mainPanel.repaint(); // repaint.
					}
				}
			});
			buttons.add(login);
			
			// sign up:	
			signup = new GameButton("Sign up");
			signup.setPreferredSize(BUTTONS_SIZE);
			signup.setMaximumSize(BUTTONS_SIZE);
			signup.setMinimumSize(BUTTONS_SIZE);
			signup.setFont(BUTTONS_FONT);
			signup.addActionListener(new ActionListener() { // on click.
				@Override
				public void actionPerformed(ActionEvent e) {
					if (dataEntered() && isNicknameValid() && !isNicknameRegistered() && isPasswordValid()) {
						AudioManager.getInstance().play("/sounds/click.wav");
						// The account is written atomically (as a single complete line) only once
						// the avatar is chosen, so an abandoned registration leaves no half-row.
						setVisible(false);
						MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(Account.this);
						JPanel mainPanel = (JPanel) window.getContentPane();
						JLayeredPane mainPanel2 = (JLayeredPane) mainPanel.getParent();
						mainPanel2.add(new AccountAvatar(nicknameInput.getText(), new String(passwordInput.getPassword())), Integer.valueOf(2));
						mainPanel2.revalidate(); // notify the layout manager that the structure changed.
						mainPanel2.repaint(); // repaint.
					}
				}
			});
			buttons.add(signup);
		add(buttons);
	
		// error messages:
		// message shown when the fields are required.
		requiredMessage = new JLabel("* required field");
		requiredMessage.setForeground(BACKGROUND_COLOR); // text color.
		requiredMessage.setFont(ERROR_FONT);
		requiredMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(requiredMessage);
        
 // message shown when the credentials are not valid.
     	wrongCredentials = new JLabel("* wrong credentials");
     	wrongCredentials.setForeground(BACKGROUND_COLOR); // text color.
     	wrongCredentials.setFont(ERROR_FONT);
     	wrongCredentials.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(wrongCredentials);
        
 // message shown when the account being registered already exists.
        alreadyRegistered = new JLabel("* account already registered");
        alreadyRegistered.setForeground(BACKGROUND_COLOR); // text color.
		alreadyRegistered.setFont(ERROR_FONT);
		alreadyRegistered.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(alreadyRegistered);
		
		// message shown when the password does not meet the standard rules.
        invalidPassword = new JLabel("* password: > 8 characters, uppercase, lowercase, number, special character (not '" + SEPARATOR + "')");
        invalidPassword.setForeground(BACKGROUND_COLOR); // text color.
        invalidPassword.setFont(ERROR_FONT);
        invalidPassword.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(invalidPassword);

		// message shown when the nickname contains the reserved separator.
        invalidNickname = new JLabel("* nickname cannot contain '" + SEPARATOR + "'");
        invalidNickname.setForeground(BACKGROUND_COLOR); // text color.
        invalidNickname.setFont(ERROR_FONT);
        invalidNickname.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(invalidNickname);
	}
	
	
	/**
	 * Checks whether the fields have been filled in. 
	 * 
	 * @return true if the fields have been filled in,
	 * false if the fields have not been filled in.
	 */
	private boolean dataEntered() {
		String nicknameText = nicknameInput.getText();
		String password = new String(passwordInput.getPassword());
		
		nicknameEmpty.setForeground(nicknameText.isEmpty() ? Color.RED : BACKGROUND_COLOR); // condition ? true : false
		passwordEmpty.setForeground(password.isEmpty() ? Color.RED : BACKGROUND_COLOR); // condition ? true : false
		
		if (!nicknameText.isEmpty() && !password.isEmpty()) {
			requiredMessage.setForeground(BACKGROUND_COLOR); // text color.
			return true;
		}
		
		AudioManager.getInstance().play("/sounds/no.wav");
		wrongCredentials.setForeground(BACKGROUND_COLOR);
		requiredMessage.setForeground(Color.RED); // text color.
		return false;
	}
	
	
	/**
	 * Checks whether the account exists among the registered accounts. 
	 * 
	 * @return true if present,
	 * false if not present.
	 */
	private boolean isRegistered() {
		alreadyRegistered.setForeground(BACKGROUND_COLOR);
		try (FileReader file = new FileReader(FILE_PATH)) {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String[] credentials = scanner.nextLine().split(":"); // split the row on ':'.
				if (credentials.length < 2) // malformed row: skip it.
					continue;
				if (nicknameInput.getText().equals(credentials[0]) && new String(passwordInput.getPassword()).equals(credentials[1]))
					return true;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		AudioManager.getInstance().play("/sounds/no.wav");
		wrongCredentials.setForeground(Color.RED);
		return false;
	}
	/**
	 * Checks whether the nickname exists among the registered accounts. 
	 * 
	 * @return true if present,
	 * false if not present.
	 */
	private boolean isNicknameRegistered() {
		wrongCredentials.setForeground(BACKGROUND_COLOR);
		try (FileReader file = new FileReader(FILE_PATH)) {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String[] credentials = scanner.nextLine().split(":"); // split the row on ':'.
				if (nicknameInput.getText().equals(credentials[0])) {
					alreadyRegistered.setForeground(Color.RED);
					AudioManager.getInstance().play("/sounds/no.wav");
					return true;
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		alreadyRegistered.setForeground(BACKGROUND_COLOR);
		return false;
	}
	
	/**
	 * Checks whether the nickname is free of the reserved field separator,
	 * which would otherwise corrupt the account record on disk.
	 *
	 * @return true if the nickname is acceptable,
	 * false otherwise.
	 */
	private boolean isNicknameValid() {
		if (nicknameInput.getText().contains(SEPARATOR)) {
			invalidNickname.setForeground(Color.RED);
			nicknameEmpty.setForeground(Color.RED);
			AudioManager.getInstance().play("/sounds/no.wav");
			return false;
		}
		invalidNickname.setForeground(BACKGROUND_COLOR);
		return true;
	}

	/**
	 * Checks whether the password meets the standard rules:
	 * minimum length 8 characters, at least one uppercase and one lowercase, at least one number and one special character.
	 * The reserved field separator is not allowed (it would corrupt the record).
	 *
	 * @return true if the password is acceptable,
	 * false otherwise.
	 */
	private boolean isPasswordValid() {
		char[] password = passwordInput.getPassword();

		if (new String(password).contains(SEPARATOR)) { // the separator would corrupt the record.
			invalidPassword.setForeground(Color.RED);
			passwordEmpty.setForeground(Color.RED);
			return false;
		}

		if (password.length<8) {
			invalidPassword.setForeground(Color.RED);
			passwordEmpty.setForeground(Color.RED);
			return false;
		}
		
		boolean uppercase = false, lowercase = false, special = false, number = false;
		for(char character : password) {
			switch(Character.getType(character)) {
				case Character.UPPERCASE_LETTER -> uppercase = true;
				case Character.LOWERCASE_LETTER -> lowercase = true;
				case Character.DECIMAL_DIGIT_NUMBER -> number = true;
				default -> special = true;
			}
			
			if (uppercase && lowercase && special && number) 
				break;
		}
		
		boolean ok = uppercase && lowercase && special && number;
		invalidPassword.setForeground(ok ? BACKGROUND_COLOR : Color.RED); // condition ? true : false
		passwordEmpty.setForeground(ok ? BACKGROUND_COLOR : Color.RED); // condition ? true : false
		return ok;
	}
}