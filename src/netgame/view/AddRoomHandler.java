package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

/**
 * Handler to add a new room
 */
public class AddRoomHandler implements HttpHandler {
    @SuppressWarnings("unused")
    private WebServer webServer;
    private Controller controller;

    public AddRoomHandler(WebServer webServer)
    {
        this.webServer = webServer;
        this.controller = webServer.getController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String roomName = this.controller.addRoom();
        String response = roomName;
        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
