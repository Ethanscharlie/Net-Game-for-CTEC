package netgame.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer
{
	public final int port = 3000;

	public String gamedata;
	public HttpServer server;
	public HashMap<String, int[]> positions; 
	
	
	public String html;
	
	private String serverStatus = "";
	
	public WebServer()
	{
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

		this.positions = new HashMap<>(); 

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
        
        this.serverStatus += "Server started\n";
        
        System.out.printf("Server started on port %d\n", this.port);
        
	}
	
	public String getStatus() 
	{
		return this.serverStatus;
	}
	
	public void addToStatus(String text)
	{
		this.serverStatus += text + "\n";
	}

	public String getPositionsAsString()
	{
		String str = "";

		for (HashMap.Entry<String, int[]> entry : positions.entrySet()) {
		    String userID = entry.getKey();
		    int[] pos = entry.getValue();

			str += String.format("id=%s&x=%d&y=%d\n", userID, pos[0], pos[1]);
		}

		return str;
	}
	
	static class MainHandler implements HttpHandler {
		WebServer app;
		
		public MainHandler(WebServer app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
			Integer randomUserID = new Random().nextInt();
            String response = this.app.html;
			response = response.replace("USERIDHERE", randomUserID.toString());

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
        	this.app.addToStatus(String.format("Handled Request"));

			String query = exchange.getRequestURI().getQuery();
			String[] querys = query.split("&");
			String userID = querys[0].replace("id=", "");
			double x = Double.parseDouble(querys[1].replace("x=", ""));
			double y = Double.parseDouble(querys[2].replace("y=", ""));

			System.out.println(exchange.getRequestURI());

			this.app.positions.put(userID, new int[]{(int)x, (int)y});





			String response = this.app.getPositionsAsString();

            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

			System.out.println("Handled get req");


        }
    }
}
