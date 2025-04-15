package netgame.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner; 

public class WebServer
{
	public final int port = 3000;
	
	public HttpServer server;
	
	public final String html = """
    <html>
        <body>
            <h1>Hello, World!</h1>
            <button onclick="myFunction()">Click me</button>
            
            <script>
	            // Create WebSocket connection.
				const socket = new WebSocket("ws://127.0.0.1:9999");
				
				// Connection opened
				socket.addEventListener("open", (event) => {
				  socket.send("Hello Server!");
				});
				
				// Listen for messages
				socket.addEventListener("message", (event) => {
				  console.log("Message from server ", event.data);
				});
				
				socket.send("Hello from JavaScript");
            </script>
        </body>
    </html>
    """;
	
	private String serverStatus = "";
	
	public WebServer()
	{
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
		WebServer app;
		
		public MainHandler(WebServer app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        	this.app.addToStatus(String.format("Handled Request"));
            String response = this.app.html;
            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            System.out.println(exchange.getRequestBody().read());
            os.close();
        }
    }
}
