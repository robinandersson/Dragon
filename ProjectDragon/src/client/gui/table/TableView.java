package client.gui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.card.ICard;
import model.player.Bet;
import model.player.IPlayer;
import model.player.Player;

import client.event.*;

import client.model.game.*;

public class TableView implements EventHandler, ActionListener{
	
	private Table table;
	private JFrame frame;
	private JPanel backPanel;
	private PlayerOnePanel playerOnePanel;
	private PlayerTwoPanel playerTwoPanel;
	private PlayerThreePanel playerThreePanel;
	private PlayerFourPanel playerFourPanel;
	private PlayerFivePanel playerFivePanel;
	private PlayerSixPanel playerSixPanel;
	private PlayerSevenPanel playerSevenPanel;
	private PlayerEightPanel playerEightPanel;
	private PlayerNinePanel playerNinePanel;
	private PlayerTenPanel playerTenPanel;
	private TableInfoPanel tableInfoPanel;
	private UserBetPanel userBetPanel;
	private ArrayList<IPlayerPanel> playerPanelList;
	
	private JButton leaveTableButton;
	
	
	public TableView(Table table) {
		this.table = table;
		init();
		EventBus.register(this);
	}
	
	private void init() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		backPanel = new JPanel();
		backPanel.setBounds(100, 100, 1024, 768);
		
		leaveTableButton = new JButton("Leave table");
		leaveTableButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		leaveTableButton.setBounds(10, 683, 111, 40);
		
		playerOnePanel = new PlayerOnePanel();
		playerTwoPanel = new PlayerTwoPanel();
		playerThreePanel = new PlayerThreePanel();
		playerFourPanel = new PlayerFourPanel();
		playerFivePanel = new PlayerFivePanel();
		playerSixPanel = new PlayerSixPanel();
		playerSevenPanel = new PlayerSevenPanel();
		playerEightPanel = new PlayerEightPanel();
		playerNinePanel = new PlayerNinePanel();
		playerTenPanel = new PlayerTenPanel();
		
		playerPanelList = new ArrayList<IPlayerPanel>();
		playerPanelList.add(playerOnePanel);
		playerPanelList.add(playerTwoPanel);
		playerPanelList.add(playerThreePanel);
		playerPanelList.add(playerFourPanel);
		playerPanelList.add(playerFivePanel);
		playerPanelList.add(playerSixPanel);
		playerPanelList.add(playerSevenPanel);
		playerPanelList.add(playerEightPanel);
		playerPanelList.add(playerNinePanel);
		playerPanelList.add(playerTenPanel);
		
		tableInfoPanel = new TableInfoPanel();
		userBetPanel = new UserBetPanel();
		
		for(IPlayerPanel p : playerPanelList) {
			//TODO Will this work?
			backPanel.add((JPanel)p);
		}
		
		backPanel.add(tableInfoPanel);
		backPanel.add(userBetPanel);
		backPanel.add(leaveTableButton);
		
		frame.getContentPane().add(backPanel);
		
		frame.setVisible(true);
		frame.setResizable(false);
	}

	@Override
	public void onEvent(Event evt) {
		List<IPlayer> allPlayers = table.getPlayers();
		
		switch (evt.getTag()) {
		
		case CURRENT_BET_CHANGED:
			int bet = table.getRound().getBettingRound().getCurrentBet().getValue();
			tableInfoPanel.setBet(Integer.toString(bet));
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
			int balanceChangedPlayerIndex = -1;
			
			//TODO: om man fick in ett index fr�n b�rjan ist hade inte f�ljand varit n�dv�ndigt:
			/* get the index of the player whos balance was changed */
			for(int i = 0; i < allPlayers.size(); i++) {
				if(balanceChangedPlayer.equals(allPlayers.get(i))) {
					playerPanelList.get(i).setBalance(balanceChangedPlayer.getBalance().toString());
					break;
				}
			}
			break;
			
			//TODO What is this case supposed to do?
//		case OWN_CURRENT_BET_CHANGED:
//			Bet ownCurrentBet;
//			if (!(evt.getValue() instanceof Bet)) {
//				System.out.println("Wrong evt.getValue() for evt.getTag(): "
//						+ evt.getTag());
//			} else {
//				ownCurrentBet = (Bet)evt.getValue();
//				IPlayer betOwner = ownCurrentBet.getOwner();
//				for(int i = 0; i < allPlayers.size(); i++) {
//					if(allPlayers.get(i).equals(betOwner)) {
//						playerPanelList.get(i).setBalance()
//						break;
//					}
//				}
//			}
//			break;
			
		case TURN_CHANGED:
			int turnIndex = (Integer) evt.getValue();
			playerPanelList.get(turnIndex).setBackground(Color.green);
			if(turnIndex == 1) {
				playerPanelList.get(10).setBackground(Color.gray);
			}
			else {
				playerPanelList.get(turnIndex-1).setBackground(Color.gray);
			}
			break;
			
		case HANDS_CHANGED:
			
			if (!(evt.getValue() instanceof Player)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				for (IPlayer acp : table.getActivePlayers()) {
					int index = allPlayers.indexOf(acp);
					playerPanelList.get(index).showCards(allPlayers.get(index).getHand());
				}
			}
			break;
			
		case COMMUNITY_CARDS_CHANGED:
			List<ICard> communityCards = table.getCommunityCards();
			tableInfoPanel.showCards(communityCards);
			break;

	}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(leaveTableButton)) {
			//TODO this is the wrong event
			EventBus.publish(new Event(Event.Tag.LEAVE_TABLE, 1));
		}
	}

}
