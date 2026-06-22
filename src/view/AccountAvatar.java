package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 * Screen to choose the user's avatar.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class AccountAvatar extends JPanel{
	/**
	 * {@link JLabel} icon of the title of the {@link AccountAvatar}.
	 */
	private JLabel titleIcon;
		
	/**
	 * {@link JLabel} title of the {@link AccountAvatar}.
	 */
	private JLabel titleText;
		
	/**
	 * {@link AccountAvatarButton} button for avatar 1.
	 */
	AccountAvatarButton avatar1;
	/**
	 * {@link AccountAvatarButton} button for avatar 2.
	 */
	AccountAvatarButton avatar2;
	/**
	 * {@link AccountAvatarButton} button for avatar 3.
	 */
	AccountAvatarButton avatar3;
	/**
	 * {@link AccountAvatarButton} button for avatar 4.
	 */
	AccountAvatarButton avatar4;
	/**
	 * {@link AccountAvatarButton} button for avatar 5.
	 */
	AccountAvatarButton avatar5;
	/**
	 * {@link AccountAvatarButton} button for avatar 6.
	 */
	AccountAvatarButton avatar6;
	/**
	 * {@link AccountAvatarButton} button for avatar 7.
	 */
	AccountAvatarButton avatar7;
	/**
	 * {@link AccountAvatarButton} button for avatar 8.
	 */
	AccountAvatarButton avatar8;
	/**
	 * {@link AccountAvatarButton} button for avatar 9.
	 */
	AccountAvatarButton avatar9;
	/**
	 * {@link AccountAvatarButton} button for avatar 10.
	 */
	AccountAvatarButton avatar10;
	/**
	 * {@link AccountAvatarButton} button for avatar 11.
	 */
	AccountAvatarButton avatar11;
	/**
	 * {@link AccountAvatarButton} button for avatar 12.
	 */
	AccountAvatarButton avatar12;
	/**
	 * {@link AccountAvatarButton} button for avatar 13.
	 */
	AccountAvatarButton avatar13;
	/**
	 * {@link AccountAvatarButton} button for avatar 14.
	 */
	AccountAvatarButton avatar14;
	/**
	 * {@link AccountAvatarButton} button for avatar 15.
	 */
	AccountAvatarButton avatar15;
	
	
	/**
	 * Constructor of the {@link AccountAvatar}.
	 * 
	 * @param nickname The user's nickname.
	 */
	public AccountAvatar(String nickname) {
		setSize(Account.SIZE);
		setLocation(Account.OPENING_POSITION); // opening position.
		setBorder(Account.BORDER);
		setBackground(Account.BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// title:
		JPanel title = new JPanel(new BorderLayout());
		title.setBackground(null);
			// icon:
			titleIcon = new JLabel(HomeAI.ICON);
			titleIcon.setBorder(new EmptyBorder(0, MainWindow.SIZE.height/220, 0, 0));
			title.add(titleIcon, BorderLayout.WEST);
							
			// text
			titleText = new JLabel("Select the avatar");
			titleText.setFont(HomeAI.TITLE_FONT);
			titleText.setForeground(MainWindow.TITLE_COLOR); // text color.
			titleText.setHorizontalAlignment(JLabel.CENTER);
			titleText.setBorder(new EmptyBorder(0, 0, 0, MainWindow.SIZE.height/50));
			title.add(titleText, BorderLayout.CENTER);
		add(title);
		
		JPanel avatar = new JPanel(new GridLayout(3,5)); // 3 rows 5 colonne
		avatar.setBackground(null);
			avatar1 = new AccountAvatarButton(1, nickname);
			avatar.add(avatar1);
			avatar2 = new AccountAvatarButton(2, nickname);
			avatar.add(avatar2);
			avatar3 = new AccountAvatarButton(3, nickname);
			avatar.add(avatar3);
			avatar4 = new AccountAvatarButton(4, nickname);
			avatar.add(avatar4);
			avatar5 = new AccountAvatarButton(5,nickname);
			avatar.add(avatar5);
			avatar6 = new AccountAvatarButton(6, nickname);
			avatar.add(avatar6);
			avatar7 = new AccountAvatarButton(7, nickname);
			avatar.add(avatar7);
			avatar8 = new AccountAvatarButton(8, nickname);
			avatar.add(avatar8);
			avatar9 = new AccountAvatarButton(9, nickname);
			avatar.add(avatar9);
			avatar10 = new AccountAvatarButton(10, nickname);
			avatar.add(avatar10);
			avatar11 = new AccountAvatarButton(11, nickname);
			avatar.add(avatar11);
			avatar12 = new AccountAvatarButton(12, nickname);
			avatar.add(avatar12);
			avatar13 = new AccountAvatarButton(13, nickname);
			avatar.add(avatar13);
			avatar14 = new AccountAvatarButton(14, nickname);
			avatar.add(avatar14);
			avatar15 = new AccountAvatarButton(15, nickname);
			avatar.add(avatar15);
		add(avatar);
		
		// space:
		add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/45)));
	}
}