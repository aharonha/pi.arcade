package pi.arcade.simon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

import pi.arcade.Game;
import pi.arcade.Player;

public class SimonGame extends Game {

	private final List<SimonPlayer> players;
	private final Iterator<SimonPlayer> playersIterator;
	protected SimonPlayer currPlayer;

	public SimonGame(Player... player) {
		players = new ArrayList<SimonPlayer>();
		for (Player p : player) {
			SimonPlayer simonPlayer;
			if (!(p instanceof SimonPlayer)) {
				simonPlayer = new SimonPlayer(p);
			} else {
				simonPlayer = (SimonPlayer) p;
			}
			players.add(simonPlayer);
		}
		playersIterator = Iterables.cycle(players).iterator();
	}

	@Override
	protected Player currentPlayer() {
		return currPlayer;
	}

	@Override
	protected void turnEnd() {
		currPlayer = playersIterator.next();
	}

	@Override
	protected boolean isGameEnd() {
		return false;
	}

}
