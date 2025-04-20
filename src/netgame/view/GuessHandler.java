package netgame.view;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import netgame.controller.Controller;

public class GuessHandler implements HttpHandler
{
	private WebServer webServer;
	private Controller controller;

    public GuessHandler(WebServer webServer) 
    {
        this.webServer = webServer;
        this.controller = webServer.getController();
    }
 
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
	    final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
        final String query = exchange.getRequestURI().getQuery();
        final int drawingPlayerID = this.controller.getDrawingPlayerID();

        if (this.controller.getPlayerID(clientIp) != drawingPlayerID) 
        {
            final String guess = query.replace("guess=", "");
            final String correctGuess = this.controller.getWord();
            if (guess.equalsIgnoreCase(correctGuess)) 
            {
                System.out.println("User got it correct!");
                this.controller.changePlayers();
                this.controller.newWord();
                this.controller.setCanvasData("clear");
            }
        }

        // Response
        String response = "";
        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
