package view;


import controller.Controller;


/**
 * Main class containing the main method that starts the application.
 * Uses the Singleton pattern to guarantee a single instance.
 * 
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class JBlackJack{
	/**
 * The singleton instance of {@link JBlackJack}.
 */
	private static JBlackJack instance;
	
	/**
 * The application's main {@link MainWindow}.
 */
	private MainWindow window;
	/**
 * The {@link Controller} that manages the game logic.
 */
	private Controller controller;
	
	
	/**
 * Starts the application.
 *
 * @param args The command-line arguments.
 */
	public static void main(String[] args) {
		getInstance();
		instance.window = new MainWindow();
	}
	
	
	/**
 * Constructor that prevents direct instantiation.
 */
	private JBlackJack() {
	}
	/**
 * Returns the singleton instance of {@link JBlackJack}.
 * If the instance does not exist, it creates it.
 *
 * @return The singleton instance of {@link JBlackJack}.
 */
	public static JBlackJack getInstance() {
		if (instance == null)
			instance = new JBlackJack();
		return instance;
	}
	
	
	/**
 * Starts a new {@link Game}.
 *
 * @param numberOfPlayers The number of players of the {@link Game}.
 */
	public void start(int numberOfPlayers) {
		controller = new Controller(numberOfPlayers);
		window.getContentPane().removeAll();
		window.add(new Game(controller));
		window.revalidate(); // notify the layout manager that the structure changed.
        window.repaint(); // repaint.
	}


	/**
	 * Starts a new {@link Game} (new match) with the same number of players
	 * as the current one.
	 */
	public void newMatch() {
		start(controller.getHands().size()); // dealer + player + AI players.
	}
	
	
	/**
	 * Ends the {@link Game}.
	 * 
	 * @param result The {@link GameResult} of the {@link Game}.
	 */
	public void end(String result) {
		Game game = (Game) window.getContentPane().getComponent(0);
		game.center.add(new GameResult(result));
		game.revalidate(); // notify the layout manager that the structure changed.
		game.repaint(); // repaint.
	}
}