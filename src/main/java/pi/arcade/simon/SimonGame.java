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
	private volatile boolean ended = false;
	protected SimonPlayer currPlayer, firstPlayer;

	public SimonGame(Player... player) {
		super("Simon Game");
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
		firstPlayer = currPlayer = playersIterator.next();
	}

	@Override
	protected Player currentPlayer() {
		return currPlayer;
	}

	@Override
	protected void turnEnd() {
		SimonPlayer nextPlayer = playersIterator.next();
		ended = (nextPlayer == firstPlayer);
		currPlayer = nextPlayer;
	}

	@Override
	protected boolean isGameEnd() {
		return ended;
	}
}
