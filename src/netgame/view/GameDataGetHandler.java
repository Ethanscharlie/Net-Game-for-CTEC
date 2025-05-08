package netgame.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import netgame.controller.Controller;

/**
 * Handler to get the game state, should to accessed on an interval from the client
 */
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

        // Request
        final String query = exchange.getRequestURI().getQuery();
        final String[] querys = query.split("&");
        final String room = querys[0].replace("room=", "");
        final String name = querys[1].replace("name=", "");
        final String canvasData = querys[3].replace("canvas=", "");

        final int drawingPlayerID = this.controller.getDrawingPlayerID(room);

        String word = "noneyo";
        if (this.controller.getPlayerID(room, clientIp) == drawingPlayerID) {
            word = this.controller.getWord(room);
        }

        this.controller.setPlayerName(room, this.controller.getPlayerID(room, clientIp), name);

        // Response
        String response = "";
        response += "{";
        response += Controller.generateJSONRow("yourID", this.controller.getPlayerID(room, clientIp));
        response += Controller.generateJSONRow("drawingPlayerID", drawingPlayerID);
        response += Controller.generateJSONRow("canvas", this.controller.getCanvasData(room));
        response += Controller.generateJSONRow("word", word);
        response += Controller.generateJSONRow("guesses", this.controller.getGuesses(room));
        response += Controller.generateJSONRowRawFinal("players", this.controller.getPlayerList(room));
        response += "}";

        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        if (this.controller.getPlayerID(room ,clientIp) == drawingPlayerID) {
            this.controller.setCanvasData(room, canvasData);
        }

    }
}
