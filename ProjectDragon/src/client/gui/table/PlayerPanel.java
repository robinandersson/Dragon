package client.gui.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import common.model.card.ICard;
import common.model.player.IPlayer;
import common.model.player.hand.IHand;

public class PlayerPanel extends JPanel {
	private JPanel cardPanel;
	private JPanel creditsPanel;
	private JLabel card1Label;
	private JLabel card2Label;
	private JLabel nameLabel;
	private JLabel creditsLabel;
	private JLabel availableCreditsLabel;
	private String path = "lib/deckimages/";
	
	/**
	 * Creates the panel
	 */
	public PlayerPanel() {
		init();
	}

	private void init() {
		this.setPreferredSize(new Dimension(135, 144));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.gray);
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		cardPanel = new JPanel();
		cardPanel.setLayout(new BorderLayout());
		
		creditsPanel = new JPanel();
		creditsPanel.setLayout(new BorderLayout());
		
		card1Label = new JLabel();
		card1Label.setIcon(new ImageIcon(path + "NORANKNOSUIT.gif"));

		card2Label = new JLabel();
		card2Label.setIcon(new ImageIcon(path + "NORANKNOSUIT.gif"));

		nameLabel = new JLabel("No player");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		creditsLabel = new JLabel("Credits:");
		creditsLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		availableCreditsLabel = new JLabel("xxx");

		cardPanel.add(card1Label, BorderLayout.WEST);
		cardPanel.add(card2Label, BorderLayout.EAST);
		
		creditsPanel.add(creditsLabel, BorderLayout.WEST);
		creditsPanel.add(availableCreditsLabel, BorderLayout.EAST);
		
		this.add(cardPanel, BorderLayout.NORTH);
		this.add(creditsPanel, BorderLayout.SOUTH);
		this.add(nameLabel, BorderLayout.CENTER);
	}

	public boolean discard() {
		card1Label.setIcon(new ImageIcon(path + "NORANKNOSUIT.gif"));
		card2Label.setIcon(new ImageIcon(path + "NORANKNOSUIT.gif"));
		return true;
	}

	public void showCards(IHand h) {
		ICard card1 = h.getCards().get(0);
		ICard card2 = h.getCards().get(1);
		card1Label.setIcon(new ImageIcon(
				path + card1.getRank() + card1.getSuit() + ".gif"));
		card2Label.setIcon(new ImageIcon(
				path + card2.getRank() + card2.getSuit() + ".gif"));
	}

	public void setName(IPlayer p) {
		nameLabel.setText(p.getName());
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	}
	
	public void setBalance(String s) {
		availableCreditsLabel.setText(s);
	}
}
