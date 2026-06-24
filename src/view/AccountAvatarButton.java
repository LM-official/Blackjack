package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;


/**
 * Button to choose the user's avatar.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class AccountAvatarButton extends JButton{
 /**
	 * {@link Dimension} of the logo in the {@link Home}.
	 */
	private static final Dimension AVATAR_SIZE = new Dimension(MainWindow.SIZE.height/21, MainWindow.SIZE.height/21);
	
    
 /**
	 * Constructor of the {@link AccountAvatarButton}.
	 *
	 * @param avatar The number matching the avatar.
	 * @param nickname The user's nickname.
	 * @param password The user's password.
	 */
	public AccountAvatarButton(int avatar, String nickname, String password) {
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Account.BACKGROUND_COLOR);
        ImageIcon avatarIcon = new ImageIcon(Resources.icon("/img/avatar/"+avatar+".png").getImage().getScaledInstance(AVATAR_SIZE.width, AVATAR_SIZE.height, Image.SCALE_SMOOTH));
		setIcon(avatarIcon);

		addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				try (FileWriter file = new FileWriter(Account.FILE_PATH, true)){ // true to append to the file.
					// full record written in one shot: nickname:password:avatar:played:won:lost:xp:level
					file.write(nickname + ":" + password + ":" + avatar + ":0:0:0:0:1" + System.lineSeparator());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(AccountAvatarButton.this);
				JPanel mainPanel = (JPanel) window.getContentPane();
                mainPanel.removeAll();
                AccountAvatarButton.this.getParent().getParent().setVisible(false);
                mainPanel.add(new HomeRegistered(nickname));
                mainPanel.revalidate(); // notify the layout manager that the structure changed.
                mainPanel.repaint(); // repaint.
			}
		});
		
		
		// animations:
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) { // action when the button is released.
				setCursor(MainWindow.CURSOR);
			}
		});
		
		setUI(new BasicButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) { // action while the button is held down.
            	setCursor(MainWindow.CURSOR_PRESS);
            }
        });
		
		// audio:
		addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioManager.getInstance().play("/sounds/ok.wav");
			}
		});
	}
}