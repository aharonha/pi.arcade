package pi.arcade;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import pi.arcade.simon.SimonGame;
import pi.arcade.simon.SimonPlayer;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;

public class Main {
	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		List<GpioProvider> providers = new Vector<>();
		try {
			GpioProvider providerForButtons1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x20);
			providers.add(providerForButtons1);
			GpioProvider providerForLEDs1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x21);
			providers.add(providerForLEDs1);
			GpioProvider providerForButtons2 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x22);
			providers.add(providerForButtons2);
			GpioProvider providerForLEDs2 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x23);
			providers.add(providerForLEDs2);

			Player player1 = new SimonPlayer(gpio, providerForButtons1,
					"player 1", providerForLEDs1);
			Player player2 = new SimonPlayer(gpio, providerForButtons2,
					"player 2", providerForLEDs2);
			Game game;
			try {
				System.out.println("checking player 2");
				player2.getBlue().blink(100).get();
				System.out.println("player 2 online (0x22-0x23)");
				game = new SimonGame(player1, player2);
			} catch (RuntimeException | ExecutionException e) {
				if (e.getCause() instanceof IOException) {
					System.err.println("player 2 is offline");
					game = new SimonGame(player1);
				} else {
					if (e instanceof RuntimeException) {
						throw (RuntimeException) e;
					} else {
						throw new RuntimeException(e.getCause());
					}
				}
			}

			synchronized (game) {
				game.start();
				game.join();
			}

		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("finishing...");
			providers.forEach(GpioProvider::shutdown);
			gpio.shutdown();
		}
	}

}
