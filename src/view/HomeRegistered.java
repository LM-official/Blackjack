package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


/**
 * Initial screen after logging in.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class HomeRegistered extends Home{
	/**
	 * Constructor of the {@link HomeRegistered}.
	 * 
	 * @param nickname The user's nickname.
	 */
	public HomeRegistered(String nickname) {
		super(getAvatarId(nickname));
		userText.setText(nickname);
		user.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				add(new AccountRegistered(nickname), Integer.valueOf(1)); // (high priority).
				user.setEnabled(false); // disable the button while the popup is shown.
			}
		});
	}
	
	
	/**
	 * Returns the avatar id associated with the account having the given nickname.
	 * 
	 * @param nickname The nickname of the account whose avatar is requested.
	 * @return {@link String} The account's avatar id.
	 */
	private static String getAvatarId(String nickname) {
		try (FileReader file = new FileReader(Account.FILE_PATH)) {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String[] credentials = scanner.nextLine().split(":"); // split the row on ':'.
				if (credentials.length >= 3 && nickname.equals(credentials[0]))
					return credentials[2];
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// impossible since it is called only after registering or logging into an account.
		// in any case, on unexpected errors it returns the default image.
		return "user";
	}
}