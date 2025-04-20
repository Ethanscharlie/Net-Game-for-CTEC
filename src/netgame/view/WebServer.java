package netgame.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import com.sun.net.httpserver.HttpServer;

import netgame.controller.Controller;

public class WebServer
{
	public final int port = 3000;

	private Controller controller;

	private HttpServer server;
	private String html;

	public WebServer(Controller controller)
	{
		this.controller = controller;
		this.html = readHTMLFromFile("index.html");

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
	private static String readHTMLFromFile(String path) {
		String html = "";

		try 
		{
			Scanner scanner = new Scanner(new File(path));

			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();
				html += line;
			}

			scanner.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		return html;
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
	public String getHTML()
	{
		return this.html;
	}

	/**
	 * Stops the server
	 */
	public void stop()
	{
		this.server.stop(1);
	}
}
