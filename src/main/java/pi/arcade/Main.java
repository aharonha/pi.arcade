package pi.arcade;

import java.io.IOException;

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
		try {
			GpioProvider providerForButtons1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x20);
			GpioProvider providerForLEDs1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x21);

			GpioProvider providerForButtons2 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x22);
			GpioProvider providerForLEDs2 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574GpioProvider.PCF8574_0x23);

			Player player1 = new SimonPlayer(gpio, providerForButtons1,
					"player 1", providerForLEDs1);
			Player player2 = new SimonPlayer(gpio, providerForButtons2,
					"player 2", providerForLEDs2);
			Game game = new SimonGame(player1, player2);

			synchronized (game) {
				game.start();
				game.join();
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		} finally {
			gpio.shutdown();
		}
	}

}
