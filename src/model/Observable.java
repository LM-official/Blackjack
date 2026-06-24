package model;


import view.Observer;


/**
 * Represents an observable element in the Observable Observer pattern.
 * When the state of an object implementing {@link Observable} changes it notifies all its registered {@link Observer}s.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public interface Observable {
	/**
 * Registers the new {@link Observer}; it will be notified on every change.
 *
 * @param observer The {@link Observer} to register.
 */
	public void register(Observer observer);
	/**
 * Removes the {@link Observer}; it will no longer be notified.
 *
 * @param observer The {@link Observer} to remove.
 */
	public void unregister(Observer observer);
	/**
 * Notifies all registered {@link Observer}s.
 */
	public void notifyObservers();
}