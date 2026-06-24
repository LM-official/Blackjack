package model;


import javax.swing.ImageIcon;
import view.Resources;


/**
 * Each Card has
 * a {@link CardValue}, a {@link CardSuit},
 * a {@link boolean} state (face up or face down)
 * and an {@link ImageIcon} that represents it graphically.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Card {
	/**
	 * The {@link CardValue} of the {@link Card}.
	 */
	private final CardValue value;
	/**
	 * {@link enum} {@link CardValue} of a {@link Card}.
	 * Each {@link CardValue} has an associated {@link int} value and {@link String}.
	 * 
	 * @author Mercuri Lorenzo,
	 * @author Matricola: 2145783.
	 */
	protected enum CardValue {
		ACE(1, "Ace"),
		TWO(2, "Two"),
		THREE(3, "Three"),
		FOUR(4, "Four"),
		FIVE(5, "Five"),
		SIX(6, "Six"),
		SEVEN(7, "Seven"),
		EIGHT(8, "Eight"),
		NINE(9, "Nine"),
		TEN(10, "Ten"),
		JACK(10, "Jack"),
		QUEEN(10, "Queen"),
		KING(10, "King");
		/**
		 * {@link int} value of the {@link Card}.
		 */
		private final int value;
		/**
		 * {@link String} representation of the {@link Card}'s value.
		 */
		private final String valueString;
		
		
		/**
		 * Constructor of a {@link CardValue}.
		 * 
		 * @param value The {@link int} value of the {@link Card}.
	 * @param valueString The {@link String} representation of the {@link Card}'s value.
		 */
		private CardValue(int value, String valueString) {
			this.value = value;
			this.valueString = valueString;
		}
		
		
		/**
		 * Returns the {@link int} value of the {@link Card}.
		 * 
		 * @return The {@link int} value of the {@link Card}.
		 */
		protected int getValue() {
			return value;
		}
		/**
		 * Returns the {@link String} representation of the {@link Card}'s value.
		 * 
		 * @return The {@link String} representation of the {@link Card}'s value.
		 */
		protected String getValueString() {
			return valueString;
		}
	}
	
	
	/**
	 * The {@link CardSuit} of the {@link Card}.
	 */
	private final CardSuit suit;
	/**
	 * {@link enum} {@link CardSuit} of a {@link Card}.
	 * Each {@link CardSuit} has an associated {@link String} representation.
	 * 
	 * @author Mercuri Lorenzo,
	 * @author Matricola: 2145783.
	 */
	protected enum CardSuit {
		HEARTS("Hearts"),
		DIAMONDS("Diamonds"),
		CLUBS("Clubs"),
		SPADES("Spades");
		/**
		 * {@link String} representation of the {@link CardSuit} of the {@link Card}.
		 */
		private final String suit;
		
		
		/**
		 * Constructor of a {@link CardSuit}.
		 * 
		 * @param suit {@link String} representation of the {@link CardSuit} of the {@link Card}.
		 */
		private CardSuit(String suit) {
			this.suit = suit;
		}
		
		
		/**
		 * Returns the {@link String} representation of the {@link CardSuit} of the {@link Card}.
		 * 
		 * @return The {@link String} representation of the {@link CardSuit} of the {@link Card}.
		 */
		protected String getSuit() {
			return suit;
		}
	}
	
	
	/**
	 * The {@link boolean} state of the {@link Card}:
	 * face up = true, face down = false.
	 */
	private boolean faceUp;
	/**
	 * The {@link ImageIcon} of the {@link Card}'s front.
	 */
	private final ImageIcon frontIcon;
	/**
	 * The {@link ImageIcon} of the {@link Card}'s back.
	 */
	private final ImageIcon backIcon;
	

	/**
	 * Constructor of a {@link Card}.
	 * 
	 * @param value {@link CardValue} of the {@link Card}.
	 * @param suit {@link CardSuit} of the {@link Card}.
	 */
	public Card(CardValue value, CardSuit suit) {
		this.value = value;
		this.suit = suit;
		faceUp = true;

		frontIcon = Resources.icon("/img/cards/" + value.getValueString() + suit.getSuit() + ".png"); // e.g. /img/cards/AceSpades.png
		backIcon = Resources.icon("/img/cards/Back.png"); // the back is shared by all cards.
	}
	
	
	/**
	 * Returns the {@link int} {@link CardValue} of the {@link Card}.
	 *
	 * @return The {@link int} {@link CardValue} of the {@link Card}.
	 */
	public int getValue() {
		return value.getValue();
	}
	/**
	 * Returns whether the {@link Card} is an ace.
	 *
	 * @return true if the {@link Card} is an ace, false otherwise.
	 */
	public boolean isAce() {
		return value == CardValue.ACE;
	}
	/**
	 * Returns the {@link String} representation of the {@link CardValue} of the {@link Card}.
	 * 
	 * @return The {@link String} representation of the {@link CardValue} of the {@link Card}.
	 */
	protected String getValueString() {
		return value.getValueString();
	}
	/**
	 * Returns the {@link CardSuit} of the {@link Card}.
	 * 
	 * @return The {@link CardSuit} of the {@link Card}.
	 */
	public String getSuit() {
		return suit.getSuit();
	}
	/**
	 * Returns the {@link boolean} state of the {@link Card}:
	 * faceUp = true,
	 * face down = false.
	 * 
	 * @return The {@link boolean} state of the {@link Card}.
	 */
	public boolean isFaceUp() {
		return faceUp;
	}
	/**
	 * Returns the {@link ImageIcon} of the {@link Card}.
	 * If it is face up it returns the front,
	 * if it is face down it returns the back.
	 *
	 * @return The {@link ImageIcon} of the {@link Card}.
	 */
	public ImageIcon getIcon() {
		return faceUp ? frontIcon : backIcon;
	}
	
	
	/**
	 * Returns the {@link String} representation of the {@link Card} through its visible features.
	 * If it is face up it returns {@link CardValue} and {@link CardSuit},
	 * if it is face down it returns nothing.
	 * 
	 * @return The visible features of the {@link Card}.
	 */
	@Override
	public String toString() {
		if (faceUp)
			return value.getValueString() + " of " + suit.getSuit().toLowerCase();
		return "Face-down card";
	}
	
	/**
	 * Flips the {@link Card}.
	 * If it is face up, it turns it face down,
	 * if it is face down, it turns it face up.
	 */
	public void flip() {
		faceUp = !faceUp;
	}
}