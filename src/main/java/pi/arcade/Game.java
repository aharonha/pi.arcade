package pi.arcade;

public abstract class Game extends Thread {
	public Game(String name) {
		super(name);
	}

	protected abstract Player currentPlayer();

	protected abstract void turnEnd();

	protected abstract boolean isGameEnd();

	@Override
	public void run() {
		try {
			do {
				if (!currentPlayer().turn()) {
					turnEnd();
				}
			} while (!isGameEnd());
		} catch (InterruptedException e) {

		}
	}
}
