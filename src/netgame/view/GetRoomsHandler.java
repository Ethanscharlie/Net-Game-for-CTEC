package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

public class GetRoomsHandler implements HttpHandler 
{
    private WebServer webServer;
    private Controller controller;

    public GetRoomsHandler(WebServer webServer)
    {
        this.webServer = webServer;
        this.controller = webServer.getController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = this.controller.getRoomsString();
        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
