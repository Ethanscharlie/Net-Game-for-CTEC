package netgame.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer
{
	public final int port = 3000;

	public HttpServer server;
	public String html;

	public ArrayList<String> players;
	public int drawingPlayerID = 0;
	public String canvasData;
	
	public WebServer()
	{
		players = new ArrayList<>();

		// Read HTML
		this.html = "";
		Scanner scanner;
		try 
		{
			scanner = new Scanner(new File("index.html"));

			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();
				this.html += line;
			}

			scanner.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		// Open Webserver
		try
		{
			server = HttpServer.create(new InetSocketAddress(this.port), 0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
        server.createContext("/", new MainHandler(this));
        server.createContext("/getgamedata", new GameDataGetHandler(this));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        System.out.printf("Server started on port %d\n", this.port);
        
	}

	public int getPlayerID(String ip) {
		int id = this.players.indexOf(ip);
		return id;
	}
	
	static class MainHandler implements HttpHandler {
		WebServer app;
		
		public MainHandler(WebServer app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
			final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
			this.app.players.add(clientIp);

			// Integer randomUserID = new Random().nextInt();
            String response = this.app.html;

            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

	static class GameDataGetHandler implements HttpHandler {
		WebServer app;
		
		public GameDataGetHandler(WebServer app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
			final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

			// Request
			String query = exchange.getRequestURI().getQuery();

			if (this.app.getPlayerID(clientIp) == this.app.drawingPlayerID) {
				this.app.canvasData = query.replace("canvas=", "");
			}

			// Response
			String response = "";
			response += String.format("yourID=%d\n", this.app.getPlayerID(clientIp));
			response += String.format("drawingPlayerID=%s\n", this.app.drawingPlayerID);
			response += String.format("canvas=%s", this.app.canvasData);

            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
