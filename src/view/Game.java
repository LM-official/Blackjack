package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import controller.Controller;
import model.Card;
import model.Hand;


/**
 * Game screen.
 *
 * <p>The layout is fully proportional to the screen size and is built so that
 * dealing cards never moves the rest of the interface: the dealer stays
 * centered, the deck is anchored to the top-right corner and the players keep
 * their position (each hand has a fixed footprint, see {@link GameObserver}).</p>
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Game extends JPanel{
	/**
	 * {@link Controller} of the {@link Game}.
	 */
	private final Controller controller;

	/**
	 * {@link JPanel} center, initially empty, later holds the result.
	 */
	JPanel center;


	/**
	 * {@link Font} of the text of the {@link Game}.
	 */
	static final Font FONT = MainWindow.FONT.deriveFont((float)MainWindow.SIZE.height/50); // use the window font adapting its size.

	/**
	 * {@link Dimension} of the {@link Card}s.
	 */
	static final Dimension CARD_SIZE = new Dimension(MainWindow.SIZE.height/8, (int)(MainWindow.SIZE.height/5.5));
	/**
	 * {@link Dimension} of the {@link ImageIcon} of the deck.
	 */
	private static final Dimension DECK_SIZE = new Dimension(MainWindow.SIZE.height/5, MainWindow.SIZE.height/7);
	/**
	 * Margin kept between the deck and the screen edges.
	 */
	private static final int DECK_MARGIN = MainWindow.SIZE.height/15;


	/**
	 * Constructor of the {@link Game}.
	 *
	 * @param controller The {@link Controller} of the game.
	 */
	public Game(Controller controller) {
		this.controller = controller;

		setBackground(null);
		setLayout(new BorderLayout()); // top / center / bottom, scales with the window.

		add(buildTop(), BorderLayout.NORTH);

		center = new JPanel(new GridBagLayout()); // empty for now; holds the centered result later.
		center.setBackground(null);
		add(center, BorderLayout.CENTER);

		add(buildBottom(), BorderLayout.SOUTH);

		audio();
	}


	/**
	 * Builds the top row: the dealer's hand centered on screen, with the deck
	 * pinned to the top-right corner (out of the card flow, so it never moves).
	 */
	private JPanel buildTop() {
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(null);
		top.setBorder(new EmptyBorder(MainWindow.SIZE.height/50, 0, 0, 0));

		// dealer, centered on screen by a FlowLayout wrapper:
		Hand dealerHand = controller.getDealerHand();
		JLabel dealerScore = new JLabel("Points: " + dealerHand.getPoints());
		GameObserver dealerCards = new GameObserver(controller, dealerHand, dealerScore);
		JPanel dealerWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		dealerWrap.setBackground(null);
		dealerWrap.add(handColumn("Dealer", dealerHand, dealerCards));
		top.add(dealerWrap, BorderLayout.CENTER);

		// left spacer the size of the deck area, so the dealer stays centered:
		top.add(spacer(DECK_SIZE.width + DECK_MARGIN), BorderLayout.WEST);

		// deck, anchored to the top-right corner:
		JLabel deck = new JLabel(scaled(controller.deckGetIcon(), DECK_SIZE));
		JPanel deckWrap = new JPanel(new BorderLayout());
		deckWrap.setBackground(null);
		deckWrap.setBorder(new EmptyBorder(0, 0, 0, DECK_MARGIN));
		deckWrap.add(deck, BorderLayout.NORTH);
		top.add(deckWrap, BorderLayout.EAST);

		return top;
	}

	/**
	 * Builds the bottom row: the player and the AI players, centered as a group
	 * and top-aligned so all the titles sit on the same line.
	 */
	private JPanel buildBottom() {
		JPanel bottom = new JPanel(new GridBagLayout());
		bottom.setBackground(null);
		bottom.setBorder(new EmptyBorder(0, 0, MainWindow.SIZE.height/12, 0)); // bottom padding lifts the players up from the screen edge.

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START; // top-align each block.
		gbc.insets = new Insets(0, GameObserver.ROW_GAP/2, 0, GameObserver.ROW_GAP/2); // total gap = ROW_GAP.

		bottom.add(buildPlayer(), gbc);

		int aiPlayers = controller.getHands().size() - 2; // all hands minus the dealer and the player.
		for (int i = 0; i < aiPlayers; i++) {
			Hand hand = controller.getAIHand(i);
			JLabel score = new JLabel("Points: " + hand.getPoints());
			GameObserver cards = new GameObserver(controller, hand, score);
			bottom.add(handColumn("AI player " + (i + 1), hand, cards), gbc);
		}

		return bottom;
	}

	/**
	 * Builds the player's column: the standard hand column plus the Hit/Stand
	 * buttons below it.
	 */
	private JPanel buildPlayer() {
		Hand hand = controller.getPlayerHand();
		JLabel score = new JLabel("Points: " + hand.getPoints());
		GameButton hit = new GameButton("Hit");
		GameButton stand = new GameButton("Stand");
		GameObserver cards = new GameObserver(controller, hand, score, hit, stand);

		JPanel player = handColumn("Player", hand, cards);
		player.add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/50))); // space before the buttons.
		player.add(buttonRow(hit, stand));
		return player;
	}

	/**
	 * Wires the Hit/Stand buttons and lays them out in a centered row.
	 *
	 * @param hit The button that draws another card for the player.
	 * @param stand The button that ends the player's turn.
	 * @return The buttons row.
	 */
	private JPanel buttonRow(GameButton hit, GameButton stand) {
		hit.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.draw(controller.getPlayerHand());
			}
		});
		stand.addActionListener(new ActionListener() { // on click.
			@Override
			public void actionPerformed(ActionEvent e) {
				hit.setEnabled(false);   // disable the hit button.
				stand.setEnabled(false); // disable the stand button.
				controller.playAIPlayers();
			}
		});

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.setBackground(null);
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		buttons.add(hit);
		buttons.add(stand);
		return buttons;
	}

	/**
	 * Builds the vertical "title + cards + score" column shared by every hand
	 * on the table (dealer, player and AI players).
	 *
	 * @param title The title shown above the cards.
	 * @param hand The {@link Hand} whose initial {@link Card}s are drawn.
	 * @param cards The {@link GameObserver} that renders (and keeps updating) the hand.
	 * @return The hand column.
	 */
	private JPanel handColumn(String title, Hand hand, GameObserver cards) {
		JPanel column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
		column.setBackground(null);

		column.add(styled(new JLabel(title)));
		printCards(cards, hand);
		column.add(cards);
		column.add(styled(cards.points)); // the score label the observer keeps in sync.

		return column;
	}


	/**
	 * Applies the shared game font, colour and horizontal centering to a label.
	 *
	 * @param label The label to style.
	 * @return The same label, styled.
	 */
	private static JLabel styled(JLabel label) {
		label.setFont(FONT);
		label.setForeground(MainWindow.TEXT_COLOR);
		label.setAlignmentX(CENTER_ALIGNMENT);
		return label;
	}

	/**
	 * Creates an invisible panel of the given width, used to balance the layout.
	 *
	 * @param width The fixed width of the spacer.
	 * @return The spacer panel.
	 */
	private static JPanel spacer(int width) {
		JPanel spacer = new JPanel();
		spacer.setBackground(null);
		spacer.setPreferredSize(new Dimension(width, 1));
		return spacer;
	}

	/**
	 * Scales an image to the given size with smooth interpolation.
	 *
	 * @param icon The source image.
	 * @param size The target {@link Dimension}.
	 * @return The resized {@link ImageIcon}.
	 */
	private static ImageIcon scaled(ImageIcon icon, Dimension size) {
		return new ImageIcon(icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH));
	}


	/**
	 * Loops the {@link Game} soundtrack. The clip is loaded and started off the
	 * EDT (large file); {@link AudioManager#loop} is idempotent, so a new match
	 * never stacks a second, overlapping loop.
	 */
	private void audio() {
	    Thread audioThread = new Thread(() -> {
	        AudioManager.getInstance().stop("/sounds/home.wav");
	        AudioManager.getInstance().loop("/sounds/game.wav");
	    });
	    audioThread.setDaemon(true); // close the thread automatically when the application exits.
	    audioThread.start();
	}


	/**
	 * Draws the initial {@link Card}s of the given {@link Hand} into the {@link JPanel}.
	 *
	 * @param container The container to insert the {@link Card}s into.
	 * @param hand The {@link Hand} whose {@link Card}s to draw.
	 */
	private void printCards(JPanel container, Hand hand) {
		for (Card card : controller.getCards(hand))
			container.add(new JLabel(GameObserver.scaledCard(controller.cardGetIcon(card))), 0); // newest card first: it is drawn on top, on the right.
	}
}
