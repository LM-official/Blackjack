package view;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;


/**
 * Button of the {@link HomeAI}, the AI-player count selection screen.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class HomeButtonAI extends HomeButton{
	/**
 * {@link Dimension} of the {@link HomeButtonAI}.
 */
    private static final Dimension SIZE = new Dimension(MainWindow.SIZE.width/35, MainWindow.SIZE.height/35);
    
 /**
 * {@link Border} of the {@link HomeButton}. 
 */
    private static final Border BORDER = BorderFactory.createLineBorder(HomeButton.BORDER_COLOR, MainWindow.SIZE.height/300);
    
 /**
 * {@link Font} of the text of the {@link HomeButton}.
 */
    private static final Font FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/70); // use the window font adapting its size.
    
    
 /**
 * Constructor of the {@link HomeButtonAI}.
 * 
 * @param text The button text.
 */
	public HomeButtonAI(String text) {
		super(text); // text.
		setPreferredSize(SIZE);
        setMaximumSize(SIZE);
        setMinimumSize(SIZE);
        setBorder(BORDER);
        setFont(FONT);
        
        addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				JBlackJack.getInstance().start(Integer.valueOf(getText())+2); // number of AI players + player + dealer.
	        }
        });
	}
}