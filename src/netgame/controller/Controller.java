package netgame.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Controller
{

	public Controller()
	{
		Scanner scanner = new Scanner(System.in);

		WebServer server = new WebServer();

		// Handle closing
		System.out.println("Type anything to close");
		scanner.nextLine();
		server.server.stop(1);
		System.out.println("Server Closed.");
	}
}
