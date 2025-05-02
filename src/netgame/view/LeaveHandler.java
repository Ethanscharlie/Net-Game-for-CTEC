package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

/**
 * Handler used by the client to properly leave a game
 */
public class LeaveHandler implements HttpHandler {
    private WebServer webServer;
    private Controller controller;

    public LeaveHandler(WebServer webServer)
    {
        this.webServer = webServer;
        this.controller = webServer.getController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
	 	final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
        final String query = exchange.getRequestURI().getQuery();
        final String room = query.replace("room=", "");

        this.controller.removePlayer(room, clientIp);

        exchange.sendResponseHeaders(200, "Left room".getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write("Left room".getBytes());
        os.close();
    }
}
