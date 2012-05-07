/**
 * 
 */
package ctrl.game;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.RmiStarter;
import remote.iRemote;


/**
 * @author robinandersson
 *
 */
public class ServerStarter extends RmiStarter{

	public ServerStarter(iRemote stub) {
		
	    super(iRemote.class);
		
		try {
			
            Registry registry = LocateRegistry.getRegistry();
            //TODO Okej med rebind ist�llet f�r bind? Verkar l�sa n�gra problem
            registry.rebind(iRemote.REMOTE_NAME, stub);

            System.out.println("Server ready");
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
