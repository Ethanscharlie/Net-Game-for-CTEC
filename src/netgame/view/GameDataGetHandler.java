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

        // Request
        final String query = exchange.getRequestURI().getQuery();
        final String[] querys = query.split("&");
        final String room = querys[0].replace("room=", "");
        final String name = querys[1].replace("name=", "");
        final String specString = querys[2].replace("spec=", "");
        final String canvasData = querys[3].replace("canvas=", "");

        final int drawingPlayerID = this.controller.getDrawingPlayerID(room);

        String word = "noneyo";
        if (this.controller.getPlayerID(room, clientIp) == drawingPlayerID) {
            word = this.controller.getWord(room);
        }

        this.controller.setPlayerName(room, this.controller.getPlayerID(room, clientIp), name);

        // Response
        String response = "";
        response += String.format("yourID=%d\n", this.controller.getPlayerID(room, clientIp));
        response += String.format("drawingPlayerID=%s\n", drawingPlayerID);
        response += String.format("canvas=%s\n", this.controller.getCanvasData(room));
        response += String.format("word=%s\n", word);
        response += String.format("guesses=%s\n", this.controller.getGuesses(room));
        response += String.format("players=%s\n", this.controller.getPlayerList(room));

        exchange.sendResponseHeaders(200, response.getBytes().length);  
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        if (this.controller.getPlayerID(room ,clientIp) == drawingPlayerID) {
            this.controller.setCanvasData(room, canvasData);
        }

    }
}
