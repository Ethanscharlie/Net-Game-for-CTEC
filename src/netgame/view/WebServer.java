package netgame.view;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import netgame.controller.Controller;

/**
 * Contains the server 
 */
public class WebServer
{
	public final int port = 3000;

	private Controller controller;

	private HttpServer server;
	private String gameHTML;
	private String roomsHTML;

	/**
	 * Loads the HTML for the pages and opens the web server
	 * @param controller Uh the controller
	 */
	public WebServer(Controller controller)
	{
		this.controller = controller;

		final String css = Controller.readFromFile("style.css");
		this.gameHTML = Controller.readFromFile("game.html").replace("/* INSERSTYLEHERE */", css);
		this.roomsHTML = Controller.readFromFile("rooms.html").replace("/* INSERSTYLEHERE */", css);

		this.openWebserver();
	}

	/**
	 * Function to start the server, creates context at each url, giving our Handlers
	 */
	private void openWebserver()
	{
		try
		{
			this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
        this.server.createContext("/", new MainHandler(this));
        this.server.createContext("/getgamedata", new GameDataGetHandler(this));
        this.server.createContext("/guess", new GuessHandler(this));
        this.server.createContext("/leaveroom", new LeaveHandler(this));
        this.server.createContext("/getrooms", new GetRoomsHandler(this));
        this.server.createContext("/addroom", new AddRoomHandler(this));
        this.server.setExecutor(null); 
        this.server.start();
        
        System.out.printf("Server started on port %d\n", this.port);
	}

	/**
	 * @return The Controller
	 */
	public Controller getController(){
		return this.controller;
	}

	/**
	 * @return HTML page for the server
	 */
	public String getGameHTML()
	{
		return this.gameHTML;
	}

	public String getRoomsHTML()
	{
		return this.roomsHTML;
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * @param key The key
	 * @param item The value
	 * @return The JSON row as a string
	 */
	public static String generateJSONRow(String key, String item) 
	{
		return String.format("\"%s\":\"%s\",\n", key, item);
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * Doesn't add quotes to the value
	 * @param key The key
	 * @param item The value
	 * @return The JSON row as a string
	 */
	public static String generateJSONRowRaw(String key, String item) 
	{
		return String.format("\"%s\":%s,\n", key, item);
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * Doesn't add quotes to the value
	 * Doesn't add a comma to the end
	 * @param key The key
	 * @param item The value
	 * @return The JSON row as a string
	 */
	public static String generateJSONRowRawFinal(String key, String item) 
	{
		return String.format("\"%s\":%s\n", key, item);
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * @param key The key
	 * @param item The value as an int
	 * @return The JSON row as a string
	 */
	public static String generateJSONRow(String key, int item) 
	{
		return generateJSONRow(key, ((Integer) item).toString());
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * With no comma at the end
	 * @param key The key
	 * @param item The value
	 * @return The JSON row as a string
	 */
	public static String generateJSONRowFinal(String key, String item) 
	{
		return String.format("\"%s\":\"%s\"\n", key, item);
	}

	/**
	 * Generates JSON based on key and item so I don't have to type it out each time
	 * With no comma at the end
	 * @param key The key
	 * @param item The value as an int
	 * @return The JSON row as a string
	 */
	public static String generateJSONRowFinal(String key, int item) 
	{
		return generateJSONRowFinal(key, ((Integer) item).toString());
	}
	/**
	 * Stops the server
	 */
	public void stop()
	{
		this.server.stop(1);
	}
}
