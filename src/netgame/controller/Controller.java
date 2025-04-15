package netgame.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner; 

public class Controller
{
	public final int port = 3000;
	
	private String serverStatus = "";
	
	public Controller()
	{
		Scanner scanner = new Scanner(System.in);
		
		HttpServer server;
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
        server.setExecutor(null); // creates a default executor
        server.start();
        
        this.serverStatus += "Server started\n";
        
        System.out.printf("Server started on port %d\n", this.port);
        System.out.println("Type anything to close");
        scanner.nextLine();
        server.stop(1);
        System.out.println("Server Closed.");
	}
	
	public String getStatus() 
	{
		return this.serverStatus;
	}
	
	public void addToStatus(String text)
	{
		this.serverStatus += text + "\n";
	}
	
	static class MainHandler implements HttpHandler {
		Controller app;
		
		public MainHandler(Controller app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        	this.app.addToStatus(String.format("Handled Request"));
            String response = this.app.getStatus();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
