package Main;

import java.util.Scanner;

import model.game.Table;
import model.player.Balance;
import model.player.Player;
import model.player.User;
import model.player.iPlayer;
import model.player.hand.TexasHoldemHand;
import ctrl.game.GameController;

public class Main {
	public static void main(String[] args) {
	}
	
	/* p�b�rjad metod som kan anv�ndas n�r vi vill k�ra v�r textbaserade 
	 * Dragon-variant p� torsdag /mattias h 
	 */
	public void run() {
		Table table = new Table();
		GameController gc = new GameController(table);
		iPlayer player = new User(new Player(new TexasHoldemHand(true),
				"Mattias", new Balance()));
		table.addPlayer(player);
		
		Scanner in = new Scanner(System.in);
		
		while(true) {
			System.out.println(table);
			System.out.println('>');
			String cmd = in.nextLine();
			
			if (cmd.equals("n")) {
				if (table.getTableCards().size() == 0) {
					gc.showFlop();
				} else {
					gc.showRiver();
				}	
			} else {
				System.out.println("Command not supported..");
			}
			
		}
	}
	
}
