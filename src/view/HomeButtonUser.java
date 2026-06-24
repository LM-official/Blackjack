package view;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;


/**
 * Button to access the user area.
 */
public class HomeButtonUser extends JButton{
 /**
 * {@link Border} of the {@link HomeButtonUser}. 
 */
    private static final Border BORDER = new EmptyBorder(0, 0, 0, 0);
    
 /**
 * {@link Dimension} of the {@link HomeButtonUser}.
 */
    static final Dimension USER_SIZE = new Dimension(MainWindow.SIZE.height/18, MainWindow.SIZE.height/18);
	
	
	/**
	 * Constructor of the {@link HomeButtonUser}.
	 * 
	 * @param icon The id of the icon of the button.
	 */
	public HomeButtonUser(String icon) {
		setIcon(new ImageIcon(Resources.icon("/img/avatar/"+icon+".png").getImage().getScaledInstance(USER_SIZE.width, USER_SIZE.height, Image.SCALE_SMOOTH))); // resize.
		setBorder(BORDER);
		setBackground(MainWindow.BACKGROUND_COLOR);
		
		// animations:
		addMouseListener(new MouseAdapter() {
			@Override
		    public void mouseEntered(MouseEvent e) { // action when the cursor is over the button.
				if (getModel().isEnabled())
					setIcon(new ImageIcon(Resources.icon("/img/avatar/"+icon+"dark.png").getImage().getScaledInstance(USER_SIZE.width, USER_SIZE.height, Image.SCALE_SMOOTH))); // resize.
		    }
		    @Override
		    public void mouseExited(MouseEvent e) { // action when the cursor is no longer over the button.
		    	setIcon(new ImageIcon(Resources.icon("/img/avatar/"+icon+".png").getImage().getScaledInstance(USER_SIZE.width, USER_SIZE.height, Image.SCALE_SMOOTH))); // resize.
		    }
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
				AudioManager.getInstance().play("/sounds/click.wav");
			}
		});
	}
}