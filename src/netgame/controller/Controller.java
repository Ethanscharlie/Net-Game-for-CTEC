package netgame.controller;

import java.util.Scanner;
import netgame.model.Game;
import netgame.model.Player;
import netgame.view.WebServer;

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
        if (getPlayerID(ip) != -1) 
        {
            System.out.println("Couldn't register player, player already has been registered");
            return -1;
        }

        this.game.players.add(new Player(ip, ""));
        return this.game.players.size() - 1;
    }

	/**
	 * @param ip The local ip of the client
	 * @return THe ID given to the player
	 */
	public int getPlayerID(String ip) 
    {
		for (int index = 0; index < game.players.size(); index ++)
		{
			if (!game.players.get(index).ip.equals(ip))
			{
				continue;
			}

			return index;
		}

		return -1;
	}

	public void setPlayerName(int id, String name) 
	{
		this.game.players.get(id).name = name;
	}
	
	public String getPlayerName(int id) 
	{
		return this.game.players.get(id).name;
	}

	public String getPlayerList() 
	{
		String playerList = "";

		for (Player player : this.game.players) 
		{
			playerList += player.name + "#" + this.game.players.indexOf(player) + ",";
		}

		return playerList;
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

	public void resetGuesses()
	{
		this.game.guesses = "";
	}

	public void addGuess(String guess, int userID)
	{
		this.game.guesses += guess + "  #  " + getPlayerName(userID) + ",";
	}

	public String getGuesses()
	{
		return this.game.guesses;
	}
}
