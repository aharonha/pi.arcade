package pi.arcade;

import static com.pi4j.gpio.extension.pcf.PCF8574GpioProvider.PCF8574_0x20;
import static com.pi4j.gpio.extension.pcf.PCF8574GpioProvider.PCF8574_0x21;
import static com.pi4j.gpio.extension.pcf.PCF8574GpioProvider.PCF8574_0x22;
import static com.pi4j.gpio.extension.pcf.PCF8574GpioProvider.PCF8574_0x23;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import pi.arcade.simon.SimonGame;
import pi.arcade.simon.SimonPlayer;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.impl.I2CBusImpl;

public class Main {
	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		List<GpioProvider> providers = new Vector<>();
		try {

			try {
				I2CBusImpl.getBus(I2CBus.BUS_1).getDevice(PCF8574_0x20).read();
				I2CBusImpl.getBus(I2CBus.BUS_1).getDevice(PCF8574_0x21).read();
			} catch (IOException e) {
				throw new RuntimeException("Player 1 is not connected", e);
			}

			boolean withPlayer2 = false;

			try {
				I2CBusImpl.getBus(I2CBus.BUS_1).getDevice(PCF8574_0x20).read();
				I2CBusImpl.getBus(I2CBus.BUS_1).getDevice(PCF8574_0x21).read();
				withPlayer2 = true;
			} catch (IOException e) {
				I2CBusImpl.getBus(I2CBus.BUS_1).close();
			}

			GpioProvider providerForButtons1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574_0x20);
			providers.add(providerForButtons1);
			GpioProvider providerForLEDs1 = new PCF8574GpioProvider(
					I2CBus.BUS_1, PCF8574_0x21);
			providers.add(providerForLEDs1);
			Player player1 = new SimonPlayer(gpio, providerForButtons1,
					"player 1", providerForLEDs1);

			Game game;
			if (withPlayer2) {
				GpioProvider providerForButtons2 = new PCF8574GpioProvider(
						I2CBus.BUS_1, PCF8574_0x22);
				providers.add(providerForButtons2);
				GpioProvider providerForLEDs2 = new PCF8574GpioProvider(
						I2CBus.BUS_1, PCF8574_0x23);
				providers.add(providerForLEDs2);

				Player player2 = new SimonPlayer(gpio, providerForButtons2,
						"player 2", providerForLEDs2);
				game = new SimonGame(player1, player2);
			} else {
				game = new SimonGame(player1);
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
