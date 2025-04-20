package netgame.view;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import netgame.controller.Controller;

class MainHandler implements HttpHandler {
	private WebServer webServer;
	private Controller controller;
	
	public MainHandler(WebServer webServer)
	{
		this.webServer = webServer;
		this.controller = webServer.getController();
	}
	
    @Override
    public void handle(HttpExchange exchange) throws IOException {
	    final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
		this.controller.registerPlayer(clientIp);

        String response = this.webServer.getHTML();

        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}