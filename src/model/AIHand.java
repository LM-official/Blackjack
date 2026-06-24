package model;


import java.util.Random;


/**
 * Concrete {@link Hand} of an AI player.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class AIHand extends Hand{
	/**
	 * {@link int} minimum points of the {@link AIHand}.
	 * While the {@link AIHand}'s points are below the minimum, keep drawing.
	 * Once reached or exceeded, it stops drawing. 
	 */
	private int standPoints;
	/**
	 * {@link int} minimum points safely reachable without busting.
	 */
	private static final int MINIMUM_POINTS = 12;
	/**
	 * {@link int} maximum points without busting.
	 */
	private static final int MAXIMUM_POINTS = 21;
	
	
	/**
	 * Constructor of a {@link AIHand}.
	 * Sets the minimum points the {@link AIHand} aims to reach.
	 */
	public AIHand() {
		standPoints = new Random().nextInt(MAXIMUM_POINTS - MINIMUM_POINTS + 1) + MINIMUM_POINTS; // random number from 12 to 21.
	}
	
	
	/**
	 * Returns the minimum points the {@link AIHand} aims to reach.
	 * 
	 * @return The minimum points the {@link AIHand} aims to reach.
	 */
	public int getMinimumPoints() {
		return standPoints;
	}
}