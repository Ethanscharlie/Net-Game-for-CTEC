package netgame.view;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import netgame.controller.Controller;

public class WebServer
{
	public final int port = 3001;

	private Controller controller;

	private HttpServer server;
	private String gameHTML;
	private String roomsHTML;

	public WebServer(Controller controller)
	{
		this.controller = controller;

		final String css = readFromFile("style.css");
		this.gameHTML = readFromFile("game.html").replace("INSERSTYLEHERE", css);
		this.roomsHTML = readFromFile("rooms.html").replace("INSERSTYLEHERE", css);

		this.openWebserver();
	}

	/**
	 * Function to start the server
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
        this.server.setExecutor(null); 
        this.server.start();
        
        System.out.printf("Server started on port %d\n", this.port);
	}

	/**
	 * Reads HTML data into a string from a file
	 * 
	 * @param path The path to the .html file
	 * @return HTML as a string
	 */
	private static String readFromFile(String path) {
		String data = "";

		try 
		{
			Scanner scanner = new Scanner(new File(path));

			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();
				data += line;
			}

			scanner.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		return data;
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
	 * Stops the server
	 */
	public void stop()
	{
		this.server.stop(1);
	}
}
