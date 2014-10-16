package pi.arcade;

import pi.pushbutton.control.Panel;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

public abstract class Player extends Panel {
	public Player(GpioController gpio, GpioProvider providerForButtons,
			String player, GpioProvider providerForLEDs) {
		super(gpio, providerForButtons, providerForLEDs);
		setName(player);
	}

	public abstract boolean turn();
}
