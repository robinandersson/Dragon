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
	public iServerGame getIServerGame(Account account) throws RemoteException {
		
		/*
		 * IF the supplied account has been successfully logged in the client
		 * gains access to the gameController methods
		 */
		if(clients.containsKey(account)){
			return serverGame;
		}
		
		return null;
	}
	
	@Override
	public void unRegisterClient(iPlayer player)
			throws RemoteException {
		clients.remove(player);
		
	}

	@Override
	public Account login(iClient client, String accountName,
			String accountPassword) throws RemoteException {
		
		Account account = loadAccount(accountName);
		
		if(account != null && account.getPassWord() == accountPassword){
			
			//TODO A method in account to get a player object!
			//clients.put(account.getPlayer(), client);
		}
		
		return account;
	}

	@Override
	public Account loadAccount(String accountName) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs =
			myStmt.executeQuery("SELECT * FROM Accounts WHERE userName = '" 
			+ accountName + "'");
			if (rs.next()) {
				// Accountinformation
				String firstName = rs.getString(1);
				String lastName = rs.getString(2);
				String passWord = rs.getString(3);
				String balance = rs.getString(4);
				int x = Integer.parseInt(balance);

				Account a = new Account(firstName, lastName, accountName, passWord);
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
	public boolean createAccount(Account account) {
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
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean updateAccount(Account account, String oldPassword) {
		Connection conn = dbc.getConnection();
		Statement myStmt;
		try {
			myStmt = conn.createStatement();
			ResultSet rs =
					myStmt.executeQuery("SELECT * FROM Accounts WHERE userName" +
							" = '" + account.getUserName() + "'");
			
			if(rs.next()) {
				if(!rs.getString(3).equals(oldPassword)) {
					System.out.println("Wrong password!");
					return false;
				}
			} else {
				System.out.println("There exists no account with userName: " +
						account.getUserName());
				return false;
			}
			//Accountinformation
			String userName = account.getUserName();
			String firstName = account.getFirstName();
			String lastName = account.getLastName();
			String newPass = account.getPassWord();
			int balance = account.getBalance().getValue();
			
			String updateString = "UPDATE Accounts SET firstName = '" + firstName + 
					"', lastName = '" + lastName + "', passWord = '" + 
					newPass + "', balance = '" + balance + "' WHERE userName = '"
					+ userName + "'";
			int up =
			myStmt.executeUpdate(updateString);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}



}
