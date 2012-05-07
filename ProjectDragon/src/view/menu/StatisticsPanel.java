package view.menu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import model.player.Account;
import model.player.Player;

import database.*;

import event.Event;
import event.EventBus;
import event.EventHandler;

@SuppressWarnings("serial")
public class StatisticsPanel extends JPanel implements EventHandler, IDBGame {
	DatabaseCommunicator dbc = DatabaseCommunicator.getInstance();
	
	public StatisticsPanel() {
		init();
		EventBus.register(this);
	}

	@Override
	public void onEvent(Event evt) {
		// TODO Auto-generated method stub
		
	}
	
	private void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveGame(String gameID, String date) {
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
			ResultSet rs =
			myStmt.executeQuery("SELECT * FROM PlayedGames WHERE gameID = '" 
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
			ResultSet rs =
			myStmt.executeQuery("SELECT * FROM WonGames WHERE player = '" 
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
		return -1;
	}
}
