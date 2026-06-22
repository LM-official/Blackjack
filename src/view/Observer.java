package view;

import model.Observable;

/**
 * Represents an element in the {@link Observer} pattern.
 * Objects implementing this interface observe the {@link Observable}.
 * When the {@link Observable}'s state changes it notifies all registered {@link Observer}s.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public interface Observer {
	/**
 * Invoked by the {@link Observable} object to notify a state change.
 * Each {@link Observer} implements this method to handle the update.
 */
	public void update();
}