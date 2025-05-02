package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

/**
 * Handler to guess a string and check if it right
 */
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
        final String[] querys = exchange.getRequestURI().getQuery().split("&");
        final String room = querys[1].replace("room=", "");

        final int drawingPlayerID = this.controller.getDrawingPlayerID(room);

        if (this.controller.getPlayerID(room, clientIp) != drawingPlayerID) 
        {
            final String guess = querys[0].replace("guess=", "");
            this.controller.addGuess(room, guess, this.controller.getPlayerID(room, clientIp));

            final String correctGuess = this.controller.getWord(room);

            if (guess.equalsIgnoreCase(correctGuess)) 
            {
                System.out.println("User got it correct!");
                this.controller.changePlayers(room);
                this.controller.newWord(room);
                this.controller.setCanvasData(room, "clear");
                this.controller.resetGuesses(room);
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
