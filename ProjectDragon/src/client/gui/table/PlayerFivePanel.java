package client.gui.table;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.card.ICard;
import model.player.hand.IHand;

public class PlayerFivePanel extends JPanel implements IPlayerPanel {
	
	private JLabel p5c1Label;
	private JLabel p5c2Label;
	private JLabel p5NameLabel;
	private JLabel p5CreditsLabel;
	private JLabel p5AvailableCreditsLabel;
	private String path = "lib/deckimages/";
	
	public PlayerFivePanel() {
		init();
	}

	private void init() {
		this.setLayout(null);
		this.setBounds(580, 405, 135, 144);

		p5c1Label = new JLabel("Card 1");
		p5c1Label.setBounds(10, 5, 53, 80);
		//p5c1Label.setIcon(new ImageIcon(getClass().getResource(path + "NORANKNOSUIT.gif")));
		this.add(p5c1Label);

		p5c2Label = new JLabel("Card 2\r\n");
		p5c2Label.setBounds(73, 5, 53, 80);
		//p5c2Label.setIcon(new ImageIcon(getClass().getResource(path + "NORANKNOSUIT.gif")));
		this.add(p5c2Label);

		p5NameLabel = new JLabel("Player1\r\n");
		p5NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		p5NameLabel.setBounds(10, 96, 115, 14);
		this.add(p5NameLabel);

		p5CreditsLabel = new JLabel("Credits:");
		p5CreditsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		p5CreditsLabel.setBounds(10, 119, 53, 14);
		this.add(p5CreditsLabel);

		p5AvailableCreditsLabel = new JLabel("xxx");
		p5AvailableCreditsLabel.setBounds(72, 119, 53, 14);
		this.add(p5AvailableCreditsLabel);
		
	}
	
	@Override
	public boolean discard() {
		//TODO: Avkommentera n�r det finns en bild med no rank och nosuit
				/*
				p5c1Label.setIcon(new ImageIcon(getClass().getResource(path + "NORANKNOSUIT.gif")));
				p5c2Label.setIcon(new ImageIcon(getClass().getResource(path + "NORANKNOSUIT.gif")));
				*/
		return true;
	}

	@Override
	public void setBalance(String s) {
		p5AvailableCreditsLabel.setText(s);
	}

	@Override
	public void showCards(IHand h) {
		ICard card1 = h.getCards().get(0);
		ICard card2 = h.getCards().get(1);
		p5c1Label.setIcon(new ImageIcon(getClass().getResource(path + 
				card1.getRank() + card1.getSuit() + ".gif")));
		p5c2Label.setIcon(new ImageIcon(getClass().getResource(path + 
				card2.getRank() + card2.getSuit() + ".gif")));
	}
	
	@Override
	public void setBackground(Color c) {
		this.setBackground(c);
	}
}
