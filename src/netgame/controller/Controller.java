package netgame.controller;

import netgame.model.Game;
import netgame.view.WebServer;

import java.util.Scanner;

public class Controller
{
	private Game game;
	private WebServer server;

	public Controller()
	{
		this.server = new WebServer(this);
		this.game = new Game();
		this.newWord();

		// Handle closing
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type anything to close");
		scanner.nextLine();
		server.stop();
		System.out.println("Server Closed.");
		scanner.close();
	}

    /**
	 * Adds a client to the players list
	 * 
     * @param ip Local ip address of the client
     * @return The ID given to the player
     */
    public int registerPlayer(String ip)
    {
        if (this.game.players.contains(ip)) 
        {
            System.out.println("Couldn't register player, player already has been registered");
            return -1;
        }

        this.game.players.add(ip);
        return this.game.players.size() - 1;
    }

	/**
	 * @param ip The local ip of the client
	 * @return THe ID given to the player
	 */
	public int getPlayerID(String ip) 
    {
		int id = this.game.players.indexOf(ip);
		return id;
	}
	
    /**
     * @return String URL of html canvas data
     */
    public String getCanvasData()
    {
        return this.game.canvasData;
    }

    /**
     * @param canvasData Set the string URL of html canvas data
     */
    public void setCanvasData(String canvasData) 
    {
        this.game.canvasData = canvasData;
    }

	/**
	 * @return The id of the player who is the current drawer
	 */
	public int getDrawingPlayerID()
	{
		return this.game.drawingPlayerID;
	}

	public String newWord() 
	{
		int index = (int)(Math.random() * this.game.words.size()); 
		this.game.word = this.game.words.get(index);
		return this.game.word;
	}

	public String getWord()
	{
		return this.game.word;
	}

	public void changePlayers()
	{
		final int currentPlayerID = this.game.drawingPlayerID;

		int index = currentPlayerID; 
		while (index == currentPlayerID) 
		{
			index = (int)(Math.random() * this.game.players.size()); 
		}

		this.game.drawingPlayerID = index;
	}
}
