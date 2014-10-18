package pi.arcade;

public abstract class Game extends Thread {
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
