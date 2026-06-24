package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import view.Resources;


/**
 * A deck is a {@link List} of {@link Card} and an {@link ImageIcon} that represents it graphically.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Deck {
	/**
	 * {@link List} of {@link Card}.
	 */
	private List<Card> deck;
	/**
	 * The {@link ImageIcon} of the {@link Deck} of {@link Card}.
	 */
	private final ImageIcon icon;
	
	
	/**
	 * Constructor of a {@link Deck} of {@link Card}.
	 */
	public Deck() {
		deck = new ArrayList<Card>();
		for (Card.CardSuit suit : Card.CardSuit.values()) // iterate over all suits.
			for (Card.CardValue value : Card.CardValue.values()) // iterate over all values.
				deck.add(new Card(value, suit)); // create and add the card to the deck.
		icon = Resources.icon("/img/cards/Deck.png");
		
		shuffle();
	}
	/**
	 * Shuffles the {@link Card}s of the {@link Deck}.
	 */
	public void shuffle() {
		// Collections.shuffle performs a uniform (Fisher-Yates) shuffle preserving all cards.
		Collections.shuffle(deck);
    }
	
	
	/**
	 * Returns the {@link Deck} of {@link Card}.
	 * 
	 * @return The {@link Deck} of {@link Card}.
	 */
	public List<Card> getCards(){
		return deck;
	}
	/**
	 * Returns the {@link ImageIcon} of the {@link Deck} of {@link Card}.
	 *
	 * @return The {@link ImageIcon} of the {@link Deck} of {@link Card}.
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	/**
	 * Returns the {@link Card} at the top of the {@link Deck}.
	 * 
	 * @return The {@link Card} at the top of the {@link Deck}.
	 * @throws EmptyDeckException Thrown when trying to draw a {@link Card} from an empty {@link Deck}.
	 */
	public Card drawCard() {
		if (deck.isEmpty())
			throw new EmptyDeckException("ERROR: cannot draw from an empty deck");
		return deck.remove(0);
	}
	/**
	 * thrown when trying to draw a {@link Card} from an empty {@link Deck}.
	 * 
	 * @author Mercuri Lorenzo,
	 * @author Matricola: 2145783.
	 */
	public class EmptyDeckException extends RuntimeException{
		/**
	 * Constructor of the {@link EmptyDeckException}.
	 * 
	 * @param message The error {@link String}.
	 */
		public EmptyDeckException(String message) {
	        super(message);
	    }
	}
	
	
	/**
	 * Returns the {@link String} representation of the {@link Deck}'s {@link List} of {@link Card}.
	 * 
	 * @return The {@link String} representation of the {@link Deck}'s {@link List} of {@link Card}.
	 */
	@Override
	public String toString() {
		if (deck.isEmpty())
			return "";
		
		StringBuilder deckString = new StringBuilder();
		for (Card card : deck)
			deckString.append(card.toString()).append("\n");
		return deckString.substring(0, deckString.length()-1); // remove the extra \n.
	}
	
	/**
 * Merges the {@link Card}s of the given {@link Deck} into this {@link Deck}.
 * 
 * @param deck2 The {@link Deck} to take the {@link Card}s from.
 */
    public void merge(Deck deck2) {
    	deck.addAll(deck2.getCards());
    }
}