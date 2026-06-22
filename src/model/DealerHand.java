package model;


/**
 * Concrete {@link Hand} of the dealer.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class DealerHand extends Hand{
	/**
	 * {@link int} minimum points of the {@link DealerHand}.
	 * While the {@link DealerHand}'s points are below the minimum, keep drawing.
	 * Once reached or exceeded, it stops drawing. 
	 */
	private final int MINIMUM_POINTS = 17;

	/**
	 * Returns the minimum points of the {@link DealerHand}.
	 * 
	 * @return The minimum points of the {@link DealerHand}.
	 */
	public int getMinimumPoints() {
		return MINIMUM_POINTS;
	}
}