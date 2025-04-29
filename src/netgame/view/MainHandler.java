package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
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
		String response = this.webServer.getRoomsHTML();
		String room = "";

		if (exchange.getRequestURI().getRawQuery() != null) {
  		    final String query = exchange.getRequestURI().getQuery();
			room = query.replace("room=", "");
        	response = this.webServer.getGameHTML().replace("`#INSERTROOMIDHERE`", room);

	 		final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
			this.controller.registerPlayer(room, clientIp);
		}

        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}