package client.gui.table;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import common.model.card.ICard;
import common.model.player.Bet;
import common.model.player.IPlayer;
import common.model.player.Player;


import client.event.*;

import client.model.game.*;

/**
 * This is the main table panel. It is in this panel that all other panels are created and added.
 * 
 * @author forssenm
 * @author lisastenberg
 *
 */
public class TableView implements EventHandler, ActionListener{
	
	private Table table;
	private JFrame frame;
	private JPanel backPanel;
	private JPanel foundationPanel;
	
	private JPanel northPanel, southPanel, westPanel, eastPanel, centerPanel, isReadyPanel, cardLayoutPanel;
	
	private CardLayout cardLayout;
	
	private PlayerPanel player1Panel, player2Panel, player3Panel, player4Panel, 
	player5Panel, player6Panel, player7Panel, player8Panel, player9Panel
	, player10Panel;

	private TableInfoPanel tableInfoPanel;
	private UserBetPanel userBetPanel;
	private List<PlayerPanel> playerPanelList;
	
	private JButton isReadyButton;
	
	private int frameHeight = P.INSTANCE.getFrameHeight();
	private int frameWidth = P.INSTANCE.getFrameWidth();
	
	IPlayer user;
	
	/**
	 * Creates the panel
	 * @param table It takes a table as parameter to be able to set names of player etc
	 */
	public TableView(Table table) {
		this.table = table;
		user = table.getPlayers().get(table.getMeIndex());
		init();
		EventBus.register(this);
	}
	
	private void init() {
		frame = new JFrame();
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());	
		
		foundationPanel = new JPanel();
		foundationPanel.setLayout(new BorderLayout());
		
		backPanel = new JPanel();
		backPanel.setSize(frameWidth, frameHeight);
		backPanel.setLayout(new BorderLayout());
		
		northPanel = new JPanel();
		southPanel = new JPanel();
		westPanel = new JPanel();
		eastPanel = new JPanel();
		centerPanel = new JPanel();
		isReadyPanel = new JPanel();
		
		isReadyButton = new JButton("Click if ready");
		isReadyButton.setPreferredSize(new Dimension(418, 144));
		isReadyButton.addActionListener(this);
		
		isReadyPanel.add(isReadyButton);
		
		cardLayout = new CardLayout();
		cardLayoutPanel = new JPanel(cardLayout);
		
		northPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new FlowLayout());
		westPanel.setLayout(new FlowLayout());
		eastPanel.setLayout(new FlowLayout());
		
		player1Panel = new PlayerPanel();
		player2Panel = new PlayerPanel();
		player3Panel = new PlayerPanel();
		player4Panel = new PlayerPanel();
		player5Panel = new PlayerPanel();
		player6Panel = new PlayerPanel();
		player7Panel = new PlayerPanel();
		player8Panel = new PlayerPanel();
		player9Panel = new PlayerPanel();
		player10Panel = new PlayerPanel();
		
		playerPanelList = new ArrayList<PlayerPanel>();
		playerPanelList.add(player1Panel);
		playerPanelList.add(player2Panel);
		playerPanelList.add(player3Panel);
		playerPanelList.add(player4Panel);
		playerPanelList.add(player5Panel);
		playerPanelList.add(player6Panel);
		playerPanelList.add(player7Panel);
		playerPanelList.add(player8Panel);
		playerPanelList.add(player9Panel);
		playerPanelList.add(player10Panel);
		
		tableInfoPanel = new TableInfoPanel();
		userBetPanel = new UserBetPanel(user);
		
		westPanel.add(player8Panel);
		southPanel.add(player9Panel);
		southPanel.add(player10Panel);
		southPanel.add(player1Panel);
		southPanel.add(player2Panel);
		eastPanel.add(player3Panel);
		northPanel.add(player7Panel);
		northPanel.add(player6Panel);
		northPanel.add(player5Panel);
		northPanel.add(player4Panel);
		
		centerPanel.add(tableInfoPanel);
		
		cardLayoutPanel.add(isReadyPanel, "isReadyPanel");
		cardLayoutPanel.add(centerPanel, "centerPanel");
		
		backPanel.add(cardLayoutPanel, BorderLayout.CENTER);
		backPanel.add(northPanel, BorderLayout.NORTH);
		backPanel.add(southPanel, BorderLayout.SOUTH);
		backPanel.add(westPanel, BorderLayout.WEST);
		backPanel.add(eastPanel, BorderLayout.EAST);
		
		foundationPanel.add(backPanel, BorderLayout.NORTH);
		foundationPanel.add(userBetPanel, BorderLayout.SOUTH);
		
		frame.add(foundationPanel);
		frame.setVisible(true);
		frame.setResizable(true);
	}

	@Override
	public void onEvent(Event evt) {
		List<IPlayer> allPlayers = table.getPlayers();
		
		switch (evt.getTag()) {

		case PLAYERS_CHANGED:
			int index = 0;
			for(PlayerPanel p : playerPanelList) {
				p.setName("No Player");
				p.setBalance("xxx");
			}
			for(IPlayer p : allPlayers) {
					playerPanelList.get(index).setName(p);
					playerPanelList.get(index).setBalance("" + p.getBalance());
					index++;
			}
			break;
		
		case CURRENT_BET_CHANGED:
			int bet = table.getRound().getBettingRound().getCurrentBet().getValue();
			tableInfoPanel.setBet(Integer.toString(bet));
			userBetPanel.updateSpinner(bet);
			break;
			
		case POT_CHANGED:
			int potValue = table.getRound().getPot().getValue();
			tableInfoPanel.setPotSize(Integer.toString(potValue));
			break;
		
		case HAND_DISCARDED:
			IPlayer handDiscardedPlayer = (Player) evt.getValue();
			for(int i = 0; i < allPlayers.size(); i++) {
				if(handDiscardedPlayer.equals(allPlayers.get(i))) {
					playerPanelList.get(i).discard();
					playerPanelList.get(i).setBackground(Color.red);
					break;
				}
			}
			break;
			
		case BALANCE_CHANGED:
			
			IPlayer balanceChangedPlayer = (Player) evt.getValue();
			
			//TODO: om man fick in ett index fr�n b�rjan ist hade inte f�ljand varit n�dv�ndigt:
			/* get the index of the player whos balance was changed */
			for(int i = 0; i < allPlayers.size(); i++) {
				if(balanceChangedPlayer.equals(allPlayers.get(i))) {
					playerPanelList.get(i).setBalance(balanceChangedPlayer.getBalance().toString());
					break;
				}
			}
			if(balanceChangedPlayer.equals(user)) {
				userBetPanel.updateAvailableCredits();
			}
			break;
			
		case OWN_CURRENT_BET_CHANGED:
			Bet ownCurrentBet;
			if (!(evt.getValue() instanceof Bet)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				ownCurrentBet = (Bet)evt.getValue();
				IPlayer betOwner = ownCurrentBet.getOwner();
				if(betOwner.equals(user)) {
					userBetPanel.updateOwnCurrentBet();
				}
			}
			break;
			
		case TURN_CHANGED:
			int turnIndex = (Integer) evt.getValue();
			playerPanelList.get(turnIndex).setBackground(Color.green);
			playerPanelList.get((turnIndex-1)%10).setBackground(Color.gray);
			
			List<String> legalButtons = table.getLegalButtons();
			if(legalButtons.contains("check") || legalButtons.contains("call")) {
				userBetPanel.setCheckEnabled(true);
			} else {
				userBetPanel.setCheckEnabled(false);
			}
			if(legalButtons.contains("fold")) {
				userBetPanel.setFoldEnabled(true);
			} else {
				userBetPanel.setFoldEnabled(false);
			}
			if(legalButtons.contains("raise")) {
				userBetPanel.setRaiseEnabled(true);
			} else {
				userBetPanel.setRaiseEnabled(false);
			}
			break;
			
		case HANDS_CHANGED:
			
			if (!(evt.getValue() instanceof Player)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				for (IPlayer acp : table.getActivePlayers()) {
					int i = allPlayers.indexOf(acp);
					playerPanelList.get(i).showCards(allPlayers.get(i).getHand());
				}
			}
			break;
			
		case COMMUNITY_CARDS_CHANGED:
			List<ICard> communityCards = table.getCommunityCards();
			tableInfoPanel.showCards(communityCards);
			break;
		
		case LEAVE_TABLE:
			frame.dispose();
			EventBus.publish(new Event(Event.Tag.GO_TO_MAIN, 1));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(isReadyButton)) {
			EventBus.publish(new Event(Event.Tag.PLAYER_SET_ACTIVE, true));
			cardLayout.show(cardLayoutPanel, "centerPanel");
		}
	}

}
