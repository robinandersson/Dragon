package client.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.event.Event;

import model.player.*;

import database.*;


@SuppressWarnings("serial")
public class StatisticsPanel extends JPanel implements ActionListener,
		client.event.EventHandler, IDBGame {
	DatabaseCommunicator dbc = DatabaseCommunicator.getInstance();

	private JLabel setThisUserName;
	private JLabel setThisFirstName;
	private JLabel setThisLastName;
	private JLabel setThisPlayedGames;
	private JLabel setThisWonGames;

	private JButton statisticsBackButton;

	public StatisticsPanel() {
		init();
		client.event.EventBus.register(this);
	}

	@Override
	public void onEvent(client.event.Event evt) {
		if(evt.getTag().equals(Event.Tag.PUBLISH_ACCOUNT_INFORMATION)) {
			Account acc = (Account)evt.getValue();
			setThisUserName.setText(acc.getUserName());
			setThisFirstName.setText(acc.getFirstName());
			setThisLastName.setText(acc.getLastName());
			setThisWonGames.setText(Integer.toString(loadNbrOfWonGames(acc.getUserName())));
			setThisPlayedGames.setText(Integer.toString(loadNbrOfPlayedGames(acc.getUserName())));
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == statisticsBackButton) {
			client.event.EventBus.publish(new client.event.Event(client.event.Event.Tag.GO_TO_MAIN, 1));
		}
	}

	private void init() {
		this.setLayout(null);
		this.setBackground(P.INSTANCE.getBackground());

		JLabel userName = new JLabel("User name");
		userName.setBounds(447, 172, 200, 20);
		userName.setFont(P.INSTANCE.getBoldLabelFont());
		this.add(userName);
		
		//TODO set the different values, get them from where?
		setThisUserName = new JLabel("Get the name from where?");
		setThisUserName.setBounds(447, 197, 200, 20);
		setThisUserName.setFont(P.INSTANCE.getLabelFont());
		this.add(setThisUserName);
		
		JLabel firstName = new JLabel("First name");
		firstName.setBounds(447, 222, 200, 20);
		firstName.setFont(P.INSTANCE.getBoldLabelFont());
		this.add(firstName);
		
		setThisFirstName = new JLabel("Get the name from where?");
		setThisFirstName.setBounds(447, 247, 200, 20);
		setThisFirstName.setFont(P.INSTANCE.getLabelFont());
		this.add(setThisFirstName);
		
		JLabel lastName = new JLabel("Last name");
		lastName.setBounds(447, 272, 200, 20);
		lastName.setFont(P.INSTANCE.getBoldLabelFont());
		this.add(lastName);
		
		setThisLastName = new JLabel("Get the name from where?");
		setThisLastName.setBounds(447, 297, 200, 20);
		setThisLastName.setFont(P.INSTANCE.getLabelFont());
		this.add(setThisLastName);
		
		JLabel playedGames = new JLabel("Number of played games");
		playedGames.setBounds(447, 322, 230, 20);
		playedGames.setFont(P.INSTANCE.getBoldLabelFont());
		this.add(playedGames);
		
		setThisPlayedGames = new JLabel("Get the number of games from where?");
		setThisPlayedGames.setBounds(447, 347, 200, 20);
		setThisPlayedGames.setFont(P.INSTANCE.getLabelFont());
		this.add(setThisPlayedGames);
		
		JLabel wonGames = new JLabel("Number of won games");
		wonGames.setBounds(447, 372, 200, 20);
		wonGames.setFont(P.INSTANCE.getBoldLabelFont());
		this.add(wonGames);
		
		setThisWonGames = new JLabel(("Get number of won games from where?"));
		setThisWonGames.setBounds(447, 397, 200, 20);
		setThisWonGames.setFont(P.INSTANCE.getLabelFont());
		this.add(setThisWonGames);
		
		statisticsBackButton = new JButton("Back");
		statisticsBackButton.setFont(P.INSTANCE.getLabelFont());
		statisticsBackButton.setBounds(10, 683, 108, 36);
		statisticsBackButton.addActionListener(this);
		this.add(statisticsBackButton);
	}

	@Override
	public void saveGame(int gameID, String date) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void savePlacement(String gameID, Player player, int placement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Integer, String> loadGamePlacements(String gameID) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs = myStmt
					.executeQuery("SELECT * FROM PlayedGames WHERE gameID = '"
							+ gameID + "'");
			Map<Integer, String> placements = new TreeMap<Integer, String>();
			while (rs.next()) {
				String placement = rs.getString(3);
				int tmp = Integer.parseInt(placement);
				String player = rs.getString(2);

				placements.put(tmp, player);
			}
			return placements;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int loadNbrOfWonGames(String userName) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs = myStmt
					.executeQuery("SELECT * FROM WonGames WHERE player = '"
							+ userName + "'");
			if (rs.next()) {
				String nbrOfWonGames = rs.getString(2);
				int tmp = Integer.parseInt(nbrOfWonGames);
				return tmp;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int calculateGameID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int loadNbrOfPlayedGames(String userName) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs = myStmt
					.executeQuery("SELECT player, COUNT(*) AS " +
							"nbrOfPlayedGames FROM PlayedGames WHERE player = '"
							+ userName + "' GROUP BY player");
			if (rs.next()) {
				String nbrOfPlayedGames = rs.getString(2);
				int tmp = Integer.parseInt(nbrOfPlayedGames);
				return tmp;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
}
