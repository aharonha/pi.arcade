package pi.arcade;

import pi.pushbutton.control.Panel;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

public abstract class Player extends Panel {
	private final GpioController gpio;
	private final GpioProvider providerForButtons, providerForLEDs;

	public Player(GpioController gpio, GpioProvider providerForButtons,
			String player, GpioProvider providerForLEDs) {
		super(gpio, providerForButtons, providerForLEDs);
		setName(player);
		this.gpio = gpio;

		this.providerForButtons = providerForButtons;
		this.providerForLEDs = providerForLEDs;
	}

	public Player(Player p) {
		this(p.gpio, p.providerForButtons, p.getName(), p.providerForLEDs);
	}

	public abstract boolean turn() throws InterruptedException;
}
