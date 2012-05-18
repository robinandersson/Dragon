/**
 * 
 */
package client.ctrl.game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import client.event.Event;
import client.event.EventHandler;
import client.event.EventBus;
import client.model.game.Table;

import model.player.Account;
import model.player.Balance;
import model.player.Player;
import model.player.IPlayer;
import model.player.User;
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
public class RemoteCommunicationController implements IClient, EventHandler {

	// A map with games the user is currently playing represented by the remote
	// game controller for that specific game
	private Map<IPlayer, RemoteGameController> activeGames;
	
	// The reference to the server
	private IServer serverComm;
	
	// TODO Flytta "lagringen" av account till ett mer passande st�lle?
	private Account account;
	
	public RemoteCommunicationController() {
		activeGames = new TreeMap<IPlayer, RemoteGameController>();
		account = null;
		EventBus.register(this);
	}
	
	/** 
	 * Tries to get a connection with the server on the default port and returns
	 * a reference if successful
	 * 
	 * @return The reference to the server
	 */
	public IServer connectToServer(){
		return connectToServer(Registry.REGISTRY_PORT);
	}
	
	/** 
	 * Tries to get a connection with the server on the specified port number
	 * and returns a reference if successful
	 * 
	 * @param port The port number of the searched server
	 * @return The reference to the server
	 */
	public IServer connectToServer(int port){
		IServer server = null;
		
	    try {
	    	
	        Registry registry = LocateRegistry.getRegistry(port);

	        server = (IServer) registry.lookup(IServer.REMOTE_NAME);
	        System.out.println("*** Connection established on port: " + port
	        														+ " ***");
	    }
	    
	    catch (Exception e) {
	    	System.out.println("*** Failed to connect to server: ***");
	    	System.out.println();
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
	 * @return The Account instance containing useful information and used as
	 * security clearance
	 */
	public boolean login(IClient client, String accountName,
													String accountPassword){
		
		serverComm = connectToServer();
		
		if(serverComm != null){
			
			// TODO Remove this first try-catch part after connection through
			// network has been established. Also remove testPrint from
			// IServerGame
			try {
				System.out.println(serverComm.testPrint());
			} catch (RemoteException e) {
				System.out.println("Test print unsuccessful");
				e.printStackTrace();
			}
		
			try {	
				this.account = serverComm.login(client, accountName, accountPassword);
				
				if(this.account != null){
					EventBus.publish(new Event(Event.Tag.LOGIN_SUCCESS, ""));
					return true;
				} else {
					EventBus.publish(new Event(Event.Tag.LOGIN_FAILED, ""));
				}
				
			} catch (RemoteException e) {
				// TODO Handle login failure better
				EventBus.publish(new Event(Event.Tag.LOGIN_FAILED, ""));
				System.out.println("*** Connection problem, failed to login ***");
				e.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	public boolean logout() {
		
		try {
			serverComm.logout(this.account);
			return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public Account getAccount(){
		return this.account;
	}	
	
	private boolean tryRegisterAccount(String userName, String firstName, 
			String lastName, String passWord) {
		serverComm = connectToServer();

		Account tmp = new Account(firstName, lastName, userName, passWord);
		try {
			if (serverComm.createAccount(tmp)) {
				login(this, tmp.getUserName(), tmp.getPassWord());
				EventBus.publish(new Event(Event.Tag.REGISTER_SUCCESS, ""));
				return true;
			} else {
				EventBus.publish(new Event(Event.Tag.REGISTER_FAILED, ""));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean createGame(int entranceFee, int playerStartingChips,
															int maxPlayers) {
		
		if(serverComm == null) {
			return false;
		}
		
		Player player = new Player(new Hand(),
						getAccount().getUserName(),
						new Balance(playerStartingChips));
		
		IPlayer user = new User(player);
		
		Table table = new Table(0);
		table.addPlayer(user);
		

		
		try {
			
			RemoteGameController clientGame = new RemoteGameController(this,
																user, table);
			
			IServerGame serverGame = serverComm.createGame(getAccount(),
					clientGame, entranceFee, maxPlayers, playerStartingChips);
			clientGame.setServerGame(serverGame);
			activeGames.put(user, clientGame);
			
			return true;
			
		} catch (RemoteException e) {
			// TODO Better handling when not able to create a game
			System.out.println("*** Communication error," + 
												"couldn not create game ***");
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Returns the active games that players are able to join 
	 * @return A list with the active games that players are able to join
	 */
	public List<IServerGame> getActiveGames() {
		
		try {
			return serverComm.getActiveGames(this.account);
		} catch (RemoteException e) {
			System.out.println("*** Could not get active games! ***");
			e.printStackTrace();
		}
		
		return null;
		
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
	public boolean joinGame(int gameID) {
		
		if(serverComm == null) {
			return false;
		}
		
		// TODO Change the balance to be the same as the startingChips
		Player player = new Player(new Hand(),
				getAccount().getUserName(),
				new Balance(1000));
		
		IPlayer user = new User(player);
		
		
		IServerGame serverGame = null;

		try {
			
			RemoteGameController clientGame = new RemoteGameController(this,
												user, new GameController());
			
			serverGame = serverComm.joinGame(getAccount(), player, clientGame,
																	gameID);
			if(serverGame == null) {
				return false;
			}
			
			List<IPlayer> playerList = serverGame.getPlayers();
			playerList.add(user);
			clientGame.newTable(playerList, playerList.size() - 1);
			clientGame.addPlayers(playerList);
			activeGames.put(user, clientGame);
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return true;
		
	}

	@Override
	public void onEvent(Event evt) {
		
		switch(evt.getTag()) {
		
		case TRY_LOGIN:
			ArrayList<char[]> login;
			if(!(evt.getValue() instanceof ArrayList)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				login = (ArrayList<char[]>)evt.getValue();
				String userName, passWord;
				userName = new String(login.get(0));
				passWord = new String(login.get(1));
				login(this, userName, passWord);
			}
			break;
			
		case TRY_REGISTER:
			ArrayList<char[]> accountInfo;
			if(!(evt.getValue() instanceof ArrayList)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				accountInfo = (ArrayList<char[]>)evt.getValue();
				String userName, firstName, lastName, passWord;
				userName = new String(accountInfo.get(0));
				firstName = new String(accountInfo.get(1));
				lastName = new String(accountInfo.get(2));
				passWord = new String(accountInfo.get(3));
				tryRegisterAccount(userName, firstName, lastName, passWord);
			}
			break;
			
		case CREATE_TABLE:
			
			ArrayList<String> tableInfo;
			if(!(evt.getValue() instanceof ArrayList)) {
				System.out.println("Wrong evt.getValue() for evt.getTag(): "
						+ evt.getTag());
			} else {
				tableInfo = (ArrayList<String>) evt.getValue();
				
				ArrayList<Integer> tableInfoParsed =
													new ArrayList<Integer>();
				
				for(String numberString : tableInfo) {
					try{
						tableInfoParsed.add(Integer.parseInt(numberString));
						
					} catch (NumberFormatException e){
						System.out.println("Wrong evt.getValue() for evt.getTag(): "
								+ evt.getTag());
					}
				}
				
				if(tableInfoParsed.size() == 3 &&
						createGame(tableInfoParsed.get(0),
						tableInfoParsed.get(1), tableInfoParsed.get(2))) {
					
					EventBus.publish(new Event(Event.Tag.GO_TO_TABLE, ""));
					
				}
			}
			
			break;
			
		case GET_ACTIVE_GAMES:
			EventBus.publish(new Event(Event.Tag.PUBLISH_ACTIVE_GAMES,
															getActiveGames()));
			break;
			
		case GET_ACCOUNT_INFORMATION:
			EventBus.publish(new Event(Event.Tag.PUBLISH_ACCOUNT_INFORMATION,
															getAccount()));
			break;
		}
	}

}
