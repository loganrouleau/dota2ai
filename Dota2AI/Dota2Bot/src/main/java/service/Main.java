package service;

import java.io.IOException;

import bots.lina.Lina;
import fi.iki.elonen.NanoHTTPD;
import se.lu.lucs.dota2.service.Dota2AIService;

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
		final Dota2AIService service = new Dota2AIService(new Lina());
		service.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

}
