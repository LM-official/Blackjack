package controller;


import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import model.AIHand;
import model.DealerHand;
import model.Card;
import model.PlayerHand;
import model.Hand;
import model.Deck;
import view.AudioManager;
import view.JBlackJack;
import view.Observer;
import view.Game;


/**
 * Controls the {@link Game}.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Controller{
	/**
	 * The deck used.
	 */
	private Deck deck;
	/**
	 * {@link List} of the players' {@link Hand}s:
	 * the first is the {@link DealerHand},
	 * the second is the {@link PlayerHand},
	 * any others are the {@link AIHand}s.
	 */
	private List<Hand> hands;
	
	
	/**
	 * Constructor of the {@link Controller}.
	 * 
	 * @param numberOfPlayers The {@link int} number of players of the {@link Game}.
	 */
	public Controller(int numberOfPlayers) {
		deck = new Deck();
		deck.merge(new Deck()); // create a deck made of 2 decks.
		
		
		hands = new ArrayList<Hand>();
		hands.add(new DealerHand()); // dealer's hand.
		hands.add(new PlayerHand()); // player's hand.
		
		for (int i=2; i<numberOfPlayers; i++) {
			hands.add(new AIHand()); // hands of the possible AI players.
		}
		
		// each starts with 2 cards.
		for (Hand hand : hands) {
			hand.draw(deck);
			hand.draw(deck);
		}
		hands.get(0).getCards().get(1).flip(); // the dealer's second card is face down.
	}

	
	// Deck methods:
	/**
	 * Returns the {@link ImageIcon} of the {@link Deck}.
	 * 
	 * @return The {@link ImageIcon} of the {@link Deck}.
	 */
	public ImageIcon deckGetIcon() {
		return deck.getIcon();
	}
	
	
	// methods to get the hands:
	/**
	 * Returns the {@link List} of {@link Hand}s.
	 * 
	 * @return The {@link List} of {@link Hand}s.
	 */
	public List<Hand> getHands() {
		return hands;
	}
	/**
	 * Returns the {@link DealerHand}.
	 * 
	 * @return The {@link DealerHand}.
	 */
	public DealerHand getDealerHand() {
		return (DealerHand) hands.get(0); // the dealer is always the first hand.
	}
	/**
	 * Returns the {@link PlayerHand}.
	 * 
	 * @return The {@link PlayerHand}.
	 */
	public PlayerHand getPlayerHand() {
		return (PlayerHand) hands.get(1); // the player is always the second hand.
	}
	/**
	 * Returns the {@link AIHand} at the given index.
	 * If there are no AI players it returns null.
	 * 
	 * @param index The index of the {@link AIHand} to return.
	 * @return The {@link AIHand} at the index passed as a parameter.
	 */
	public AIHand getAIHand(int index) {
		if (hands.size() <= 2) // if there are no AI players.
			return null;
		
		return (AIHand) hands.get(index+2);
	}
	
	
	// Hand methods:
	/**
	 * Returns the {@link List} of the given {@link Hand}'s {@link Card}s.
	 * 
	 * @param hand The {@link Hand} whose {@link List} of {@link Card} to return.
	 * @return The {@link List} of the {@link Hand}'s {@link Card}s passed as a parameter.
	 */
	public List<Card> getCards(Hand hand){
		return hand.getCards();
	}
	/**
	 * Returns the points of the given {@link Hand}.
	 *
	 * @param hand The {@link Hand} whose points to return.
	 * @return The points of the {@link Hand} passed as a parameter.
	 */
	public int getPoints(Hand hand) {
		return hand.getPoints();
	}
	/**
	 * Makes the given {@link Hand} draw a {@link Card}.
	 * 
	 * @param hand The {@link Hand} to make draw a {@link Card}.
	 */
	public void draw(Hand hand) {
		AudioManager.getInstance().play("/sounds/card.wav");
		hand.draw(deck);
	}
	
	
	// Card methods:
	/**
	 * Returns the {@link ImageIcon} of the given {@link Card}.
	 * 
	 * @param card The {@link Card} whose {@link ImageIcon} is returned.
	 * @return The {@link ImageIcon} of the {@link Card} passed as a parameter.
	 */
	public ImageIcon cardGetIcon(Card card) {
		return card.getIcon();
	}
	/**
	 * Flips the given {@link Card}.
	 * 
	 * @param card The {@link Card} to flip.
	 */
	public void cardFlip(Card card) {
		card.flip();
	}
	
	
	// methods for the Observable Observer pattern:
	/**
	 * Registers the new {@link Observer}; it will be notified on every change of the {@link Hand}.
	 *
	 * @param observer The {@link Observer} to register.
	 * @param hand The {@link Hand} to observe.
	 */
	public void register(Observer observer, Hand hand) {
		hand.register(observer);
	}
		
	/**
	 * Removes the {@link Observer}; it will no longer be notified of the {@link Hand}'s changes.
	 *
	 * @param observer The {@link Observer} to remove.
	 * @param hand The {@link Hand} to observe.
	 */
	public void unregister(Observer observer, Hand hand) {
		hand.unregister(observer);
	}
		
	/**
	 * Notifies all {@link Observer}s registered to the {@link Hand}.
	 * 
	 * @param hand The {@link Hand} to observe.
	 */
	public void notifyObservers(Hand hand) {
		hand.notifyObservers(); // delegate to the model's own notification logic.
	}
		
		
	// gameplay methods:
	/**
	 * Lets all the {@link AIHand}s play and then the {@link DealerHand}.
	 * The whole sequence runs on a separate thread so it does not block
	 * the GUI (EDT): the pauses between one card and the next happen on the
	 * background thread, while the graphics updates are delegated to the EDT.
	 */
	public void playAIPlayers() {
		Thread gameThread = new Thread(() -> {
			for (int i=0; i<hands.size()-2; i++) {
				AIHand hand = (AIHand) hands.get(i+2);
				while (hand.getPoints() < hand.getMinimumPoints()) {
					pause(1000);
					runOnEDT(() -> draw(hand));
				}
			}

			pause(1000);
			playDealer();
		});
		gameThread.setDaemon(true); // ends with the application.
		gameThread.start();
	}
	/**
	 * Lets the {@link DealerHand} play.
	 * Must be invoked from the game thread created by {@link #playAIPlayers()}.
	 */
	public void playDealer() {
		runOnEDT(() -> {
			AudioManager.getInstance().play("/sounds/card.wav");
			getDealerHand().getCards().get(1).flip();
			notifyObservers(getDealerHand()); // visually reveals the dealer's face-down card.
		});

		DealerHand hand = getDealerHand();
		while (hand.getPoints() < hand.getMinimumPoints()) {
			pause(1000);
			runOnEDT(() -> draw(hand));
		}

		pause(1000);
		runOnEDT(this::end);
	}

	/**
	 * Pauses the current thread for the given milliseconds.
	 *
	 * @param milliseconds The pause duration.
	 */
	private void pause(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Runs a graphics operation on the EDT, waiting for it to complete, so that
	 * the model state is updated before the next iteration.
	 *
	 * @param operation The operation to run on the EDT.
	 */
	private void runOnEDT(Runnable operation) {
		if (SwingUtilities.isEventDispatchThread()) {
			operation.run();
			return;
		}
		try {
			SwingUtilities.invokeAndWait(operation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indicates that the game has ended and reports the result.
	 * The player who busts (over 21) always loses; otherwise the one
	 * with the highest score wins, with the dealer losing if it busts.
	 */
	public void end() {
		int playerPoints = getPlayerHand().getPoints();
		int dealerPoints = getDealerHand().getPoints();

		String result;
		if (playerPoints > 21) // the player busts: always loses.
			result = "You lost";
		else if (dealerPoints > 21 || playerPoints > dealerPoints) // dealer busts or player higher.
			result = "You won";
		else if (playerPoints == dealerPoints)
			result = "Tie";
		else
			result = "You lost";

		JBlackJack.getInstance().end(result);
	}
}