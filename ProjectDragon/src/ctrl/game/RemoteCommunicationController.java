/**
 * 
 */
package ctrl.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseCommunicator;
import database.IDBAccount;

import model.player.Account;
import model.player.Player;
import model.player.iPlayer;

import remote.iClient;
import remote.iRemote;
import remote.iServerGame;

/**
 * @author robinandersson
 *
 */
public class RemoteCommunicationController extends UnicastRemoteObject
											implements iRemote, IDBAccount {
	
	iServerGame serverGame;
	Map<iPlayer, iClient> clients;
	DatabaseCommunicator dbc = DatabaseCommunicator.getInstance();
	
	RemoteGameController remoteGameController = new RemoteGameController();
	
	public RemoteCommunicationController() throws RemoteException {
		this(new RemoteGameController());
	}
	
	public RemoteCommunicationController(iServerGame serverGame)
			throws RemoteException {
		super();
		new ServerStarter(this);
		this.serverGame = serverGame;
		clients = new HashMap();
	}

	@Override
	public iServerGame getIServerGame() throws RemoteException {
		return serverGame;
	}

	@Override
	public void registerClient(iPlayer player, iClient client)
			throws RemoteException {
		clients.put(player, client);
	}
	
	@Override
	public void unRegisterClient(iPlayer player)
			throws RemoteException {
		clients.remove(player);
		
	}

	@Override
	public boolean login(iClient client, String accountName,
			String accountPassword) throws RemoteException {
		// TODO Auto-generated method stub
		
		
		/*
		 * if(correctpassword){
		 * 	clients.put(database.getPlayer(), client);
		 * 	return true;
		 * }
		 */
		
		return false;
	}

	@Override
	public Account loadAccount(String account) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs =
			myStmt.executeQuery("SELECT * FROM Accounts WHERE userName = '" + account + "'");
			if (rs.next()) {
				// Accountinformation
				String firstName = rs.getString(1);
				String lastName = rs.getString(2);
				String passWord = rs.getString(3);
				String balance = rs.getString(4);
				int x = Integer.parseInt(balance);

				Account a = new Account(firstName, lastName, account, passWord);
				a.getBalance().addToBalance(x);
				return a;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveAccount(Account account) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			
			//Accountinformation
			String userName = account.getUserName();
			String firstName = account.getFirstName();
			String lastName = account.getLastName();
			String passWord = account.getPassWord();
			int balance = account.getBalance().getValue();
			
			String updateString = "INSERT INTO Accounts VALUES('" + userName + 
					"', '" + firstName + "', '" + lastName + "', '" + 
					passWord + "', '" + balance + "')";
			int up =
			myStmt.executeUpdate(updateString);
			
			if(up == 0) {
				System.out.println("Account with userName " + userName + " " +
						"already exists");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}



}
