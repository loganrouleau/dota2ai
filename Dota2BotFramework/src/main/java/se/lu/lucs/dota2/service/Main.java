package se.lu.lucs.dota2.service;

import java.io.IOException;

import de.lighti.dota2.bot.Lina;
import fi.iki.elonen.NanoHTTPD;

public class Main {

	public static void main(String[] args) {
		try {
			new Main().run();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void run() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		final Dota2AIService service = new Dota2AIService(new Lina()); // TODO: Find a way to the class name automatically from the dependency

		// final Class<?> visualizerClass = Class.forName("se.lu.lucs.visualizer.MatchVisualizer");
		// service.add((FrameListener) visualizerClass.newInstance());

		service.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

}
