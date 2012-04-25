package remote;

import java.rmi.Remote;

import utilities.*;
/**
 * TODO: Javadoc
 * 
 * @author lisastenberg
 *
 */
public interface iServerGame extends Remote {

	/**
	 * Performs a call.
	 * 
	 * @throws IllegalCallException
	 */
	public void call() throws IllegalCallException;
	
	/**.
	 * Performs a check
	 * 
	 * @throws IllegalCheckException
	 */
	public void check() throws IllegalCheckException;
	
	/**
	 * Performs a raise.
	 * 
	 * @param amount The amount to raise the pot with.
	 * @throws IllegalRaiseException
	 */
	public void raise(int amount) throws IllegalRaiseException;
	
	/**
	 * Performs fold.
	 */
	public void fold();
}
