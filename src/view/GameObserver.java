package view;


import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.Controller;
import model.Card;
import model.Hand;
import model.PlayerHand;


/**
 * Holds the {@link Card}s of a player.
 * Observes the changes during the {@link Game}.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class GameObserver extends JPanel implements Observer {
	/**
	 * {@link Controller} of the {@link Game}.
	 */
	private Controller controller;
	/**
	 * {@link Hand} osservata.
	 */
	private Hand hand;
	
	
	/**
	 * {@link JLabel} points of the {@link Hand}.
	 */
	JLabel points;
	/**
	 * {@link GameButton} play.
	 */
	private GameButton play;
	/**
	 * {@link GameButton} stand.
	 */
	private GameButton stand;
	
	
	/**
	 * Constructor of the {@link GameObserver}.
	 * 
	 * @param controller The {@link Controller} of the {@link Game}.
	 * @param hand The observed {@link Hand}.
	 * @param points The {@link JLabel} that holds the score of the {@link Hand}.
	 */
	public GameObserver(Controller controller, Hand hand, JLabel points) {
		setLayout(new FlowLayout());
		setBackground(null);
		
		this.controller = controller;
		this.hand = hand;
		controller.register(this, hand);
		
		this.points = points;
	}
	
	/**
	 * Constructor of the {@link GameObserver}.
	 * 
	 * @param controller The {@link Controller} of the {@link Game}.
	 * @param hand The observed {@link Hand}.
	 * @param points The {@link JLabel} that holds the score of the {@link Hand}.
	 * @param play The {@link GameButton} that lets the player draw.
	 * @param stand The {@link GameButton} that lets the player stand.
	 */
	public GameObserver(Controller controller, Hand hand, JLabel points, GameButton play, GameButton stand) {
		setLayout(new FlowLayout());
		setBackground(null);
		
		this.controller = controller;
		this.hand = hand;
		controller.register(this, hand);
		
		this.points = points;
		
		this.play = play;
		this.stand = stand;
	}
	
	
	/**
	 * Adds the new {@link Card} to the {@link GameObserver}
	 * and updates the points.
	 */
	@Override
	public void update() {
		removeAll();
		for (Card card: hand.getCards()) {
			ImageIcon cardImg = new ImageIcon(controller.cardGetIcon(card).getImage().getScaledInstance(Game.CARD_SIZE.width, Game.CARD_SIZE.height, Image.SCALE_SMOOTH)); // resize image.
			add(new JLabel(cardImg));
		}
		
		points.setText("Points: " + hand.getPoints());
		if (hand instanceof PlayerHand) // only the player can bust.
			checkBust();
		
		revalidate(); // notify the layout manager that the structure changed.
        repaint(); // repaint.
	}
	
	/**
	 * Handles whether the player has busted.
	 */
	private void checkBust() {
		if (hand.getPoints() > 21) {
			play.setEnabled(false);
			stand.setEnabled(false);
			controller.playerBusted(); // busts: loses immediately, without the dealer playing.
		}
	}
}