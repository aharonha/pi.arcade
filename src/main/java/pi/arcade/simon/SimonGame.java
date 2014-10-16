package pi.arcade.simon;

import pi.arcade.Game;
import pi.arcade.Player;

public class SimonGame extends Game {

	final Player[] players;
	Player currPlayer;

	public SimonGame(Player... player) {
		for (Player p : player) {
			if (!(p instanceof SimonPlayer)) {

			}
		}
	}

}
