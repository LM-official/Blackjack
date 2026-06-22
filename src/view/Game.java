package view;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Game extends JPanel{
	/**
	 * {@link Controller} of the {@link Game}.
	 */
	private Controller controller;
	
	
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
		 * {@link JLabel} dealer title.
		 */
		private JLabel dealerTitle;
		/**
		 * {@link GameObserver}, list of the dealer's {@link Card}s.
		 */
		GameObserver dealerCards;
		/**
		 * {@link JLabel} dealer points.
		 */
		JLabel dealerPoints;
			
		/**
		 * {@link JLabel} containing the deck's {@link ImageIcon}.
		 */
		private JLabel deck;	
	
	/**
	 * {@link JPanel} center, initially empty, later holds the result.
	 */
	JPanel center;
		
	/**
	 * {@link JLabel} player title.
	 */
	private JLabel playerTitle;	
	/**
	 * {@link GameObserver}, list of the player's {@link Card}s.
	 */
	GameObserver playerCards;
	/**
	 * {@link JLabel} player points.
	 */
	JLabel playerPoints;
		/**
		 * {@link GameButton} hit. 
		 */
		GameButton hitButton;
		/**
		 * {@link GameButton} stand. 
		 */
		GameButton standButton;
	
	/**
	 * {@link JLabel} AI player title.
	 */
	 private JLabel aiTitle;
	/**
	 * {@link GameObserver}, list of the AI player's {@link Card}s.
	 */
	 GameObserver aiCards;
	/**
	 * {@link JLabel} AI player points.
	 */
	JLabel aiPoints;
		
	/**
	 * Constructor of the {@link Game}.
	 * 
	 * @param controller The {@link Controller} of the game.
	 */
	public Game(Controller controller) {
		this.controller = controller;
		
		
		setBackground(null);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.setBackground(null);
		top.setBorder(new EmptyBorder(MainWindow.SIZE.height/50, DECK_SIZE.width + MainWindow.SIZE.height/10 + MainWindow.SIZE.height/3, 0, MainWindow.SIZE.height/10));
			// dealer:
			JPanel dealer = new JPanel();
			dealer.setLayout(new BoxLayout(dealer, BoxLayout.Y_AXIS));
			dealer.setBackground(null);
			
				// title:
				dealerTitle = new JLabel("Dealer");
				dealerTitle.setFont(FONT);
				dealerTitle.setForeground(MainWindow.TEXT_COLOR); // text color.
				dealerTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				dealer.add(dealerTitle);
				
				dealerPoints = new JLabel("Points: " + String.valueOf(controller.getDealerHand().getPoints()));
				// cards:
				dealerCards = new GameObserver(controller, controller.getDealerHand(), dealerPoints);
				printCards(dealerCards, controller.getDealerHand());
				dealer.add(dealerCards);
			
				// points:
				dealerPoints.setFont(FONT);
				dealerPoints.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				dealerPoints.setForeground(MainWindow.TEXT_COLOR);
				dealer.add(dealerPoints);
			top.add(dealer);
			
			// space:
			top.add(Box.createRigidArea(new Dimension(MainWindow.SIZE.height/3, 0)), Integer.valueOf(0));
			
			// deck:
			ImageIcon deckImg = new ImageIcon(controller.deckGetIcon().getImage().getScaledInstance(DECK_SIZE.width, DECK_SIZE.height, Image.SCALE_SMOOTH)); // resize image.
			deck = new JLabel(deckImg);
			top.add(deck);
		add(top);
		
		
		// space:
		center = new JPanel();
		center.setBackground(null);
		center.add(Box.createRigidArea(new Dimension(0, (int)(MainWindow.SIZE.height/2.7))));
		add(center);
		
		
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setBackground(null);
		bottom.setBorder(new EmptyBorder(0, 0, MainWindow.SIZE.height/50, 0));
			// player:
			JPanel player = new JPanel();
			player.setLayout(new BoxLayout(player, BoxLayout.Y_AXIS));
			player.setBackground(null);
				// title:
				playerTitle = new JLabel("Player");
				playerTitle.setFont(FONT);
				playerTitle.setForeground(MainWindow.TEXT_COLOR); // text color.
				playerTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				player.add(playerTitle);
				
				playerPoints = new JLabel("Points: " + String.valueOf(controller.getPlayerHand().getPoints()));
				JPanel playerButtons = new JPanel(new FlowLayout());
				hitButton = new GameButton("Hit");
				standButton = new GameButton("Stand");
				// cards:
				playerCards = new GameObserver(controller, controller.getPlayerHand(), playerPoints, hitButton, standButton);
				printCards(playerCards, controller.getPlayerHand());
				player.add(playerCards);
		
				// points:
				playerPoints.setFont(FONT);
				playerPoints.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				playerPoints.setForeground(MainWindow.TEXT_COLOR);
				player.add(playerPoints);
				
				// space:
				player.add(Box.createRigidArea(new Dimension(0, MainWindow.SIZE.height/50)), Integer.valueOf(0));
				
				// buttons:
				playerButtons.setBackground(null);
					// card:
					hitButton.addActionListener(new ActionListener() { // on click.
						@Override
						public void actionPerformed(ActionEvent e) {
							controller.draw(controller.getPlayerHand());
						}
					});
					playerButtons.add(hitButton, Integer.valueOf(0));
					
					// stand:
					standButton.addActionListener(new ActionListener() { // on click.
						@Override
						public void actionPerformed(ActionEvent e) {
							hitButton.setEnabled(false); // disable the hit button.
							standButton.setEnabled(false); // disable the stand button.
							controller.playAIPlayers();
						}
					});
					playerButtons.add(standButton);
				player.add(playerButtons);
			bottom.add(player);
			
			
			// giocatori artificiali:
			for (int i=0; i<controller.getHands().size()-2; i++) {
				JPanel ai = new JPanel();
				ai.setLayout(new BoxLayout(ai, BoxLayout.Y_AXIS));
				ai.setBackground(null);
				ai.setBorder(new EmptyBorder(0, MainWindow.SIZE.height/55, MainWindow.SIZE.height/15, 0));
					// title:
					aiTitle = new JLabel("AI player " + (i+1));
					aiTitle.setFont(FONT);
					aiTitle.setForeground(MainWindow.TEXT_COLOR); // text color.
					aiTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					ai.add(aiTitle);
				
					aiPoints = new JLabel("Points: " + String.valueOf(controller.getAIHand(i).getPoints()));
					// cards:
					aiCards = new GameObserver(controller, controller.getAIHand(i), aiPoints);
					printCards(aiCards, controller.getAIHand(i));
					ai.add(aiCards);
					// points:
					aiPoints.setFont(FONT);
					aiPoints.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					aiPoints.setForeground(MainWindow.TEXT_COLOR);
					ai.add(aiPoints);
				bottom.add(ai);
			}
		add(bottom);
		
		audio();
	}
	
	
	/**
	 * Loops the {@link Game} soundtrack.
	 */
	private void audio() {
		AudioManager.getInstance().stop("/sounds/home.wav");
	    Thread audioThread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            do {
	                AudioManager.getInstance().play("/sounds/game.wav");
	                try {
	                    Thread.sleep(162000);
	                } catch (InterruptedException e1) {
	                    e1.printStackTrace();
	                }
	            } while (true);
	        }
	    });
	    audioThread.setDaemon(true); // close the thread automatically when the application exits.
	    audioThread.start();
	}
	
	
	/**
	 * Draws the {@link Card}s of the given {@link Hand} into the {@link JPanel}.
	 * 
	 * @param container The container to insert the {@link Card}s into.
	 * @param hand The {@link Hand} whose {@link Card}s to observe.
	 */
	private void printCards(JPanel container, Hand hand) {
		for (Card card : controller.getCards(hand)) {
			ImageIcon cardImg = new ImageIcon(controller.cardGetIcon(card).getImage().getScaledInstance(CARD_SIZE.width, CARD_SIZE.height, Image.SCALE_SMOOTH)); // resize image.
			container.add(new JLabel(cardImg), Integer.valueOf(0));
		}
	}
}