package model;


import java.util.ArrayList;
import java.util.List;
import view.Observer;


/**
 * A hand is a {@link List} of {@link Card}
 * and has an {@link int} score.
 * A hand is observable by the {@link Observer}s.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public abstract class Hand implements Observable{
	/**
	 * {@link List} of {@link Card}s.
	 */
	private List<Card> cards;
	/**
	 * {@link int} score.
	 */
	private int points;
	
	/**
	 * {@link List} of {@link Observer}.
	 */
	private List<Observer> observers;
	
	
	/**
	 * Constructor of an empty {@link Hand}.
	 */
	public Hand() {
		cards = new ArrayList<Card>();
		points = 0;
		
		observers = new ArrayList<>();
	}
	
	/**
	 * Returns the {@link List} of {@link Card}s.
	 * 
	 * @return The {@link List} of {@link Card}s.
	 */
	public List<Card> getCards(){
		return cards;
	}
	/**
	 * Returns the {@link int} score.
	 * 
	 * @return The {@link int} score.
	 */
	public int getPoints() {
		updatePoints();
		return points;
	}
	/**
	 * Updates the {@link int} score.
	 * The points count is optimized to obtain the maximum score,
	 * letting any aces count as 1 or 11 depending on what is best.
	 * Face-down {@link Card}s are not counted.
	 */
	private void updatePoints() {
		int points = 0, aces = 0;

		for (Card card : cards) {
			if (!card.isFaceUp()) // face-down cards are not counted.
				continue;
			if (card.isAce()) { // if the card is an ace.
				aces++;
				points += 1; // an ace is worth at least 1.
			}
			else
				points += card.getValue(); // add the card's score.
		}

		// at most one ace can count as 11 without busting (a second 11 would always exceed 21).
		if (aces > 0 && points <= 11)
			points += 10;

		this.points = points;
	}
	
	
	/**
	 * Returns the {@link List} of registered {@link Observer}s.
	 * 
	 * @return The {@link List} of registered {@link Observer}s.
	 */
	public List<Observer> getObservers() {
		return observers;
	}
	
	
	/**
	 * Returns the {@link String} representation of the {@link List} of {@link Card}.
	 * 
	 * @return The {@link String} representation of the {@link List} of {@link Card}.
	 */
	@Override
	public String toString() {
		if (cards.isEmpty())
			return "";
		
		StringBuilder hand = new StringBuilder();
		for (Card card : cards)
			hand.append(card.toString()).append("\n");
		return hand.substring(0, hand.length()-1); // remove the extra \n.
	}
	
	/**
	 * Adds a {@link Card}.
	 * Notifies all registered {@link Observer}s.
	 * 
	 * @param deck The {@link Deck} to draw the {@link Card} from.
	 */
	public void draw(Deck deck) {
		cards.add(deck.drawCard()); // the possible exception is handled by the deck.
		notifyObservers();
	}
	
	
	// methods for the Observable Observer pattern:
	/**
 * Registers the new {@link Observer}; it will be notified on every change.
 *
 * @param observer The {@link Observer} to register.
 */
	public void register(Observer observer) {
		observers.add(observer);
	}

	/**
 * Removes the {@link Observer}; it will no longer be notified.
 *
 * @param observer The {@link Observer} to remove.
 */
	public void unregister(Observer observer) {
		observers.remove(observer);
	}
	
	/**
 * Notifies all registered {@link Observer}s.
 */
	public void notifyObservers() {
		for (Observer observer: observers)
			observer.update();
	}
}