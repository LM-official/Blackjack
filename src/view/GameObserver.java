package view;


import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.Controller;
import model.Card;
import model.Hand;
import model.PlayerHand;


/**
 * Holds the {@link Card}s of a player.
 * Observes the changes during the {@link Game}.
 *
 * <p>The panel has a <b>fixed size</b> so that adding {@link Card}s never
 * changes its footprint: the surrounding UI (deck, other players) therefore
 * never shifts. When a hand grows too large the cards fan out and overlap so
 * they always stay inside the fixed area.</p>
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class GameObserver extends JPanel implements Observer {
	/**
	 * Horizontal gap between two non-overlapping {@link Card}s.
	 */
	private static final int GAP = Game.CARD_SIZE.width / 10;
	/**
	 * Hands shown side by side in the bottom row in the worst case
	 * (player + 3 AI players): the card area is sized so they all fit.
	 */
	private static final int MAX_HANDS_IN_ROW = 4;
	/**
	 * Horizontal gap kept between two hands placed side by side.
	 */
	static final int ROW_GAP = MainWindow.SIZE.width / 60;
	/**
	 * Margin kept between the row of hands and the left/right screen edges.
	 */
	private static final int SIDE_MARGIN = MainWindow.SIZE.width / 20;
	/**
	 * Fixed {@link Dimension} of the card area. Its width is derived from the
	 * screen so the widest possible row of hands always fits; any card that
	 * does not fit overlaps the previous ones. Being fixed, it never causes
	 * the rest of the UI to move when cards are dealt.
	 */
	static final Dimension AREA_SIZE = new Dimension(
			(MainWindow.SIZE.width - 2 * SIDE_MARGIN - (MAX_HANDS_IN_ROW - 1) * ROW_GAP) / MAX_HANDS_IN_ROW,
			Game.CARD_SIZE.height);

	/**
	 * Cache of card images already scaled to {@link Game#CARD_SIZE}, keyed by the
	 * source {@link Image}. Card art never changes, so each face is scaled only
	 * once instead of on every repaint. Only touched on the EDT.
	 */
	private static final Map<Image, ImageIcon> SCALED_CACHE = new HashMap<>();

	/**
	 * Returns the given card icon scaled to {@link Game#CARD_SIZE}, reusing a
	 * cached result when available.
	 *
	 * @param source The full-size card {@link ImageIcon}.
	 * @return The icon scaled to card size.
	 */
	static ImageIcon scaledCard(ImageIcon source) {
		return SCALED_CACHE.computeIfAbsent(source.getImage(), img ->
				new ImageIcon(img.getScaledInstance(Game.CARD_SIZE.width, Game.CARD_SIZE.height, Image.SCALE_SMOOTH)));
	}

	/**
	 * {@link Controller} of the {@link Game}.
	 */
	private Controller controller;
	/**
	 * The observed {@link Hand}.
	 */
	private Hand hand;


	/**
	 * {@link JLabel} points of the {@link Hand}.
	 */
	JLabel points;
	/**
	 * {@link GameButton} hit: lets the player draw another card.
	 */
	private GameButton hit;
	/**
	 * {@link GameButton} stand.
	 */
	private GameButton stand;


	/**
	 * Constructor of the {@link GameObserver}.
	 *
	 * @param controller The {@link Controller} of the {@link Game}.
	 * @param hand The observed {@link Hand}.
	 * @param points The {@link JLabel} that holds the score of the {@link Hand}.
	 */
	public GameObserver(Controller controller, Hand hand, JLabel points) {
		init();

		this.controller = controller;
		this.hand = hand;
		controller.register(this, hand);

		this.points = points;
	}

	/**
	 * Constructor of the {@link GameObserver}.
	 *
	 * @param controller The {@link Controller} of the {@link Game}.
	 * @param hand The observed {@link Hand}.
	 * @param points The {@link JLabel} that holds the score of the {@link Hand}.
	 * @param hit The {@link GameButton} that lets the player draw.
	 * @param stand The {@link GameButton} that lets the player stand.
	 */
	public GameObserver(Controller controller, Hand hand, JLabel points, GameButton hit, GameButton stand) {
		this(controller, hand, points);

		this.hit = hit;
		this.stand = stand;
	}

	/**
	 * Common initialization: the panel manages the {@link Card}s itself
	 * (see {@link #doLayout()}) and keeps a fixed size so the rest of the
	 * UI never moves when cards are dealt.
	 */
	private void init() {
		setLayout(null); // cards are positioned manually in doLayout().
		setBackground(null); // inherit the table color.
		setAlignmentX(CENTER_ALIGNMENT);
		setPreferredSize(AREA_SIZE);
		setMinimumSize(AREA_SIZE);
		setMaximumSize(AREA_SIZE);
	}


	/**
	 * Lays out the {@link Card}s inside the fixed area: centered and side by
	 * side while they fit, fanned out and overlapping once there are too many.
	 * The most recently dealt card (index 0) sits on the right, on top.
	 */
	@Override
	public void doLayout() {
		int n = getComponentCount();
		if (n == 0)
			return;

		int cardW = Game.CARD_SIZE.width;
		int cardH = Game.CARD_SIZE.height;
		int areaW = getWidth();
		int areaH = getHeight();

		// step between the left edges of two consecutive cards:
		int step = cardW + GAP;
		if (cardW + (n - 1) * step > areaW) // they don't fit: overlap them.
			step = (areaW - cardW) / (n - 1);

		int used = cardW + (n - 1) * step; // total width actually occupied.
		int x = (areaW - used) / 2; // horizontally centered.
		int y = (areaH - cardH) / 2; // vertically centered.

		// the oldest card (highest index) is the leftmost one:
		for (int slot = 0; slot < n; slot++) {
			getComponent(n - 1 - slot).setBounds(x, y, cardW, cardH);
			x += step;
		}
	}


	/**
	 * Adds the new {@link Card} to the {@link GameObserver}
	 * and updates the points.
	 */
	@Override
	public void update() {
		removeAll();
		for (Card card: hand.getCards())
			add(new JLabel(scaledCard(controller.cardGetIcon(card))), 0); // newest card first: it is drawn on top, on the right.

		points.setText("Points: " + hand.getPoints());
		if (hand instanceof PlayerHand) // only the player can bust.
			checkBust();

		revalidate(); // notify the layout manager that the structure changed.
        repaint(); // repaint.
	}

	/**
	 * Handles whether the player has busted.
	 */
	private void checkBust() {
		if (hand.getPoints() > 21) {
			hit.setEnabled(false);
			stand.setEnabled(false);
			controller.playerBusted(); // busts: loses immediately, without the dealer playing.
		}
	}
}
