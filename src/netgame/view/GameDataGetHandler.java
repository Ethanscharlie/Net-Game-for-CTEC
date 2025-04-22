package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

class GameDataGetHandler implements HttpHandler {
    @SuppressWarnings("unused")
    private WebServer webServer;
    private Controller controller;
    
    public GameDataGetHandler(WebServer webServer)
    {
		this.webServer = webServer;
		this.controller = webServer.getController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
        final int drawingPlayerID = this.controller.getDrawingPlayerID();

        // Request
        final String query = exchange.getRequestURI().getQuery();
        final String[] querys = query.split("&");
        final String name = querys[0].replace("name=", "");
        final String canvasData = querys[1].replace("canvas=", "");

        String word = "noneyo";
        if (this.controller.getPlayerID(clientIp) == drawingPlayerID) {
            word = this.controller.getWord();
        }

        this.controller.setPlayerName(this.controller.getPlayerID(clientIp), name);

        // Response
        String response = "";
        response += String.format("yourID=%d\n", this.controller.getPlayerID(clientIp));
        response += String.format("drawingPlayerID=%s\n", drawingPlayerID);
        response += String.format("canvas=%s\n", this.controller.getCanvasData());
        response += String.format("word=%s\n", word);
        response += String.format("guesses=%s", this.controller.getGuesses());

        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        if (this.controller.getPlayerID(clientIp) == drawingPlayerID) {
            this.controller.setCanvasData(canvasData);
        }

    }
}
