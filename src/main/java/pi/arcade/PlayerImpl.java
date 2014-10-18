package pi.arcade;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

public class PlayerImpl extends Player {

	public PlayerImpl(GpioController gpio, GpioProvider providerForButtons,
			String player, GpioProvider providerForLEDs) {
		super(gpio, providerForButtons, player, providerForLEDs);
	}

	public PlayerImpl(Player p) {
		super(p);
	}

	@Override
	public boolean turn() throws InterruptedException {
		return false;
	}

}
