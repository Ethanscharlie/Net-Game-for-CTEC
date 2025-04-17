package netgame.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer
{
	public final int port = 3000;

	public String gamedata;
	
	public HttpServer server;
	
	public final String html = """
    <html>
        <body>
		    <h1>Hello, World!</h1>
            <div id="responseDiv"></div>

            <script>
                function sendGetRequest() {
                    fetch('/getgamedata')
                        .then(response => response.text())
                        .then(data => {
                            console.log('Response:', data);
							document.getElementById('responseDiv').innerText = 'Last Response: ' + data;
                        })
                        .catch(error => console.error('Error:', error));
                }

				var intervalId = window.setInterval(function(){
					sendGetRequest();
				}, 100);
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
	
	static class MainHandler implements HttpHandler {
		WebServer app;
		
		public MainHandler(WebServer app)
		{
			this.app = app;
		}
		
        @Override
        public void handle(HttpExchange exchange) throws IOException {
			this.app.gamedata += "Somebody joined the game :3";
			this.app.gamedata += Math.random() % 5;
			this.app.gamedata += "\n";

        	this.app.addToStatus(String.format("Handled Request"));
            String response = this.app.html;
            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            System.out.println(exchange.getRequestBody().read());
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
            String response = this.app.gamedata;
            exchange.sendResponseHeaders(200, response.getBytes().length);  
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            System.out.println(exchange.getRequestBody().read());
            os.close();
        }
    }
}
