package view;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


/**
 * Screen showing the game result.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class GameResult extends JPanel{	
	/**
	 * {@link Font} of the {@link GameResult}.
	 */
	static final Font FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/10); // use the window font adapting its size.
	/**
	 * {@link JLabel} text of the {@link GameResult}.
	 */
	JLabel text;
	
	/**
	 * {@link GameButton} newMatch.
	 */
	private GameButton newMatch;
	/**
	 * {@link GameButton} playAgain.
	 */
	private GameButton playAgain;
	/**
	 * {@link GameButton} exit.
	 */
	private GameButton exit;
	
		
	/**
	 * Constructor of the {@link GameResult}.
	 * 
	 * @param result The game result.
	 */
	public GameResult(String result) {
		setBackground(null);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// text:
		text = new JLabel(result);
        text.setFont(FONT);
        text.setForeground(MainWindow.TEXT_COLOR);
        text.setAlignmentX(CENTER_ALIGNMENT);
        text.setBorder(new EmptyBorder(0, 0, MainWindow.SIZE.height/50, 0));
        add(text);
        
        
        // update the account data:
        if (!Home.userText.getText().equals(Home.GUEST_NAME)) { // if the user has logged in.
        	String nickname = Home.userText.getText();
        	
 	// save the data of every row:
        	List<String> rows = new ArrayList<>();
        	try (FileReader file = new FileReader(Account.FILE_PATH)) {
        		Scanner scanner = new Scanner(file);
    			while(scanner.hasNextLine()) {
    				String[] data = scanner.nextLine().split(":"); // split the row on ':'.
    				if (data.length >= 8 && nickname.equals(data[0])) // only update well-formed rows.
    					data = updateData(data, result);
    				rows.add(getRow(data));
    			}
        	} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	
 	// rewrite every row:
        	try (FileWriter file2 = new FileWriter(Account.FILE_PATH)){
        		for (String row: rows)
        			file2.write(row);
        	} catch (IOException e1) {
				e1.printStackTrace();
			} 
        }
        
        
 // buttons:
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(null);
 	// newMatch:
			newMatch = new GameButton("Rematch");
			// "New match" is wider than the default button: give it more room.
			Dimension wide = new Dimension(MainWindow.SIZE.width/12, GameButton.SIZE.height);
			newMatch.setPreferredSize(wide);
			newMatch.setMinimumSize(wide);
			newMatch.setMaximumSize(wide);
			newMatch.addActionListener(new ActionListener() { // on click.
				@Override
				public void actionPerformed(ActionEvent e) {
					JBlackJack.getInstance().newMatch(); // restart with the same number of players.
				}
			});
			buttons.add(newMatch);

 	// playAgain:
			playAgain = new GameButton("Home");
			playAgain.addActionListener(new ActionListener() { // on click.
				@Override
				public void actionPerformed(ActionEvent e) {
					MainWindow window = (MainWindow) SwingUtilities.getWindowAncestor(GameResult.this);
					window.getContentPane().removeAll();
					String nickname = Home.userText.getText();
					if (!nickname.equals(Home.GUEST_NAME)) // if the user has logged in.
						window.getContentPane().add(new HomeRegistered(nickname));
					else
						window.getContentPane().add(new Home(Home.GUEST_AVATAR));
					window.revalidate(); // notify the layout manager that the structure changed.
	                window.repaint(); // repaint.
				}
			});
			buttons.add(playAgain);
			
			// exit:
			exit = new GameButton("Exit");
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
			buttons.add(exit);
		add(buttons);
		
		audio(result);
	}
	
	
	/**
	 * Updates the data of the calling account.
	 * 
	 * @param data The account data.
	 * @param result The game result.
	 * @return The data of the calling account.
	 */
	private String[] updateData(String[] data, String result) {
		data[3] = Integer.toString(Integer.valueOf(data[3])+1); // games played += 1.

		if (result.equals("You won")) {
			data[4] = Integer.toString(Integer.valueOf(data[4])+1); // games won += 1.
			data[6] = Integer.toString(Integer.valueOf(data[6])+new Random().nextInt((100 - 50) + 1) + 50); // xp += [50-100].
			return nextLevel(data);
		}
		if (result.equals("Tie")) {
			data[6] = Integer.toString(Integer.valueOf(data[6])+50); // xp += 50.
			return nextLevel(data);
		}
		data[5] = Integer.toString(Integer.valueOf(data[5])+1); // games lost += 1.
		data[6] = Integer.toString(Integer.valueOf(data[6])+20); // xp += 20.
		return nextLevel(data);
	}
	/**
	 * Checks the level increase.
	 * 
	 * @param data The account data.
	 * @return The account data passed as a parameter.
	 */
	private String[] nextLevel(String[] data) {
		int level = Integer.valueOf(data[7]);
		int xp = Integer.valueOf(data[6]);
		int xpNextLevel = 150*(level-1)+50;
		while (xp >= xpNextLevel) { // can gain several levels in a single game.
			xp -= xpNextLevel; // reset xp.
			level++; // level += 1.
			xpNextLevel = 150*(level-1)+50; // threshold for the next level.
		}
		data[6] = Integer.toString(xp);
		data[7] = Integer.toString(level);

		return data;
	}
	
	/**
	 * Returns a {@link String} with the account data passed as a parameter. 
	 * 
	 * @param data The account data.
	 * @return A {@link String} with the account data passed as a parameter.
	 */
	private String getRow(String[] data) {
		StringBuilder row = new StringBuilder();
		for (String element : data)
			row.append(element + ":");
		
		return row.substring(0, row.length()-1) + System.lineSeparator(); // remove the extra ':' and add the line separator.
	}
	
	
	/**
	 * Plays the sound suited to the result.
	 * 
	 * @param result The game result.
	 */
	private void audio(String result) {
		if (result.equals("You won"))
			AudioManager.getInstance().play("/sounds/won.wav");
		else if (result.equals("You lost"))
			AudioManager.getInstance().play("/sounds/lost.wav");
		// "Tie": no specific sound.
	}
}
