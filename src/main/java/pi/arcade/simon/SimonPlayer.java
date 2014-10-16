package pi.arcade.simon;

import java.util.Deque;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

import pi.arcade.Player;
import pi.pushbutton.PushButtonWithLED;

public class SimonPlayer extends Player {

	public SimonPlayer(GpioController gpio, GpioProvider providerForButtons,
			String player, GpioProvider providerForLEDs) {
		super(gpio, providerForButtons, player, providerForLEDs);
	}

	Deque<PushButtonWithLED> buttons;

	@Override
	public boolean turn() {

		return false;
	}

}
