/**
 * 
 */
package client.ctrl.game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import model.player.Account;
import model.player.Player;
import model.player.IPlayer;
import model.player.hand.Hand;

import remote.IClient;
import remote.IClientGame;
import remote.IServer;
import remote.IServerGame;
import utilities.IllegalCallException;

/**
 * @author robinandersson
 *
 */
public class RemoteCommunicationController implements IClient {

	// A map with games the user is currently playing represented by the remote
	// game controller for that specific game
	private Map<IPlayer, RemoteGameController> activeGames;

	
	// The reference to the server
	private IServer server;
	
	// TODO Flytta "lagringen" av account till ett mer passande st�lle?
	private Account account = null;
	
	public RemoteCommunicationController(){
		activeGames = new TreeMap<IPlayer, RemoteGameController>();

		server = connectToServer();
	}
	
	/** 
	 * Tries to get a connection with the server on the default port and returns
	 * a reference if successful
	 */
	public IServer connectToServer(){
		return connectToServer(Registry.REGISTRY_PORT);
	}
	
	/** 
	 * Tries to get a connection with the server on the specified port number
	 * and returns a reference if successful
	 * 
	 * @param port The port number of the searched server
	 */
	public IServer connectToServer(int port){
		IServer server = null;
		
	    try {
	    	
	        Registry registry = LocateRegistry.getRegistry(port);
	        server = (IServer) registry.lookup(IServer.REMOTE_NAME);	        
	    }
	    
	    catch (Exception e) {
	        System.err.println("Client exception: " + e.toString());
	        e.printStackTrace();
	    }
	    
	    return server;
	}
	
	
	/** 
	 * Tries to login with the provided account name and password. Also passes
	 * a reference from the clients controller that handles game communication
	 * with the server
	 * 
	 * @param clientGame The reference to this clients game controller
	 * @param accountName The name of the account
	 * @param accountPassword The password associated with the account name.
	 */
	public Account login(IClient client, String accountName,
													String accountPassword){

		
		try {	
			this.account = server.login(client, accountName, accountPassword);
				
		} catch (RemoteException e) {
			// TODO Handle login failure
			e.printStackTrace();
			System.out.println("Failed to login");
		}
		
		return this.account;
	}
	
	public Account getAccount(){
		return this.account;
	}
	
	/** 
	 * Tries to join the table with the specified index. Passes the unique
	 * Account object along for security clearance. Returns the iPlayer object
	 * that has been assigned to the table if successful.
	 * 
	 * @param account The users unique Account object
	 * @param gameIndex The index of the game that the user wants to join
	 * @return true if the game was successfully joined
	 */
	public boolean joinGame(Account account, int gameIndex) {
		
		Player player = new Player(new Hand(), account.getUserName(),
														account.getBalance());
		IClientGame clientGame = new RemoteGameController(this);
		IServerGame serverGame = null;
		

		try {
			serverGame = server.joinGame(account, player, clientGame,
																	gameIndex);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(serverGame == null) {
			return false;
		}

		try {
			
			RemoteGameController remoteGameController =
					new RemoteGameController(this);
			
			Collection<IPlayer> playerList = serverGame.getPlayers();
			remoteGameController.addPlayers(playerList);
			activeGames.put(player, remoteGameController);
			
		} catch (IllegalCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}

}
