package pi.arcade.simon;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pi.arcade.Player;
import pi.arcade.PlayerImpl;
import pi.pushbutton.PushButtonWithLED;

import com.pi4j.component.switches.Switch;
import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

public class SimonPlayer extends PlayerImpl implements SwitchListener {

	public SimonPlayer(GpioController gpio, GpioProvider providerForButtons,
			String player, GpioProvider providerForLEDs) {
		super(gpio, providerForButtons, player, providerForLEDs);
		this.mainPlayerThread = Thread.currentThread();
	}

	private Thread mainPlayerThread;

	public SimonPlayer(Player p) {
		super(p);
		this.red = p.getRed();
		this.green = p.getGreen();
		this.blue = p.getBlue();
		this.yellow = p.getYellow();
		this.white = p.getWhite();
		this.mainPlayerThread = Thread.currentThread();
	}

	protected List<PushButtonWithLED> buttons = new ArrayList<>();
	final protected OfInt randomInts = new Random(System.currentTimeMillis())
			.ints(0, allPushButtonWithLEDs.length).map(Math::abs).iterator();

	@Override
	public boolean turn() throws InterruptedException {
		System.out.println(getName() + "'s turn");
		if (mainPlayerThread != Thread.currentThread())
			mainPlayerThread = Thread.currentThread();

		// Add button to the buttons list
		buttons.add(allPushButtonWithLEDs[randomInts.nextInt()]);
 
		// Blink phase
		Queue<Switch> expectedButtonsOnThisTurn = new LinkedBlockingQueue<>(
				buttons.size());
		buttons.forEach(led -> {
			synchronized (this) {
				expectedButtonsOnThisTurn.offer(led);
				System.err.println(led.getName());
				led.pulse(SECONDS.toMillis(2), true);
			}
		});

		// read response phase
		return buttonsPressSquanceMatches(expectedButtonsOnThisTurn);
	}

	private boolean buttonsPressSquanceMatches(Queue<Switch> expectedButtons)
			throws InterruptedException {
		actualPressedButtons = new ArrayBlockingQueue<Switch>(
				expectedButtons.size());
		do {
			Switch expectedSwitch = expectedButtons.poll();
			if (expectedSwitch != actualPressedButtons.take()) {
				synchronized (this) {
					actualPressedButtons = null;
				}
				return false;
			}
		} while (!expectedButtons.isEmpty());

		return true;
	}

	private volatile ArrayBlockingQueue<Switch> actualPressedButtons = null;

	@Override
	public void onStateChange(SwitchStateChangeEvent event) {
		synchronized (this) {
			if (actualPressedButtons != null) {
				if (event.getNewState().equals(SwitchState.ON)) {
					try {
						actualPressedButtons.put(event.getSwitch());
					} catch (InterruptedException e) {
						e.printStackTrace();
						mainPlayerThread.interrupt();
					}
				}
			}
		}
	}
}
