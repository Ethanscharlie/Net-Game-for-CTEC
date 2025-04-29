package netgame.controller;

import java.util.HashMap;
import java.util.Scanner;
import netgame.model.Game;
import netgame.model.Player;
import netgame.view.WebServer;

public class Controller
{
	private HashMap<String, Game> rooms;
	private WebServer server;

	public Controller()
	{
		this.server = new WebServer(this);
		this.rooms = new HashMap<>();

		this.rooms.put("0", new Game());
		this.rooms.put("1", new Game());
		this.rooms.put("2", new Game());
		this.rooms.put("3", new Game());
		this.newWord("0");
		this.newWord("1");
		this.newWord("2");
		this.newWord("3");

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
    public int registerPlayer(String room, String ip)
    {
        if (getPlayerID(room, ip) != -1) 
        {
            System.out.println("Couldn't register player, player already has been registered");
            return -1;
        }

        rooms.get(room).players.add(new Player(ip, ""));
        return rooms.get(room).players.size() - 1;
    }

	/**
	 * @param ip The local ip of the client
	 * @return THe ID given to the player
	 */
	public int getPlayerID(String room, String ip) 
    {
		for (int index = 0; index < rooms.get(room).players.size(); index ++)
		{
			if (!rooms.get(room).players.get(index).ip.equals(ip))
			{
				continue;
			}

			return index;
		}

		return -1;
	}

	public void setPlayerName(String room, int id, String name) 
	{
		rooms.get(room).players.get(id).name = name;
	}
	
	public String getPlayerName(String room, int id) 
	{
		return rooms.get(room).players.get(id).name;
	}

	public String getPlayerList(String room) 
	{
		String playerList = "";

		for (Player player : rooms.get(room).players) 
		{
			playerList += player.name + ",";
		}

		return playerList;
	}
	
    /**
     * @return String URL of html canvas data
     */
    public String getCanvasData(String room)
    {
        return rooms.get(room).canvasData;
    }

    /**
     * @param canvasData Set the string URL of html canvas data
     */
    public void setCanvasData(String room, String canvasData) 
    {
        rooms.get(room).canvasData = canvasData;
    }

	/**
	 * @return The id of the player who is the current drawer
	 */
	public int getDrawingPlayerID(String room)
	{
		return rooms.get(room).drawingPlayerID;
	}

	public String newWord(String room) 
	{
		int index = (int)(Math.random() * rooms.get(room).words.size()); 
		rooms.get(room).word = rooms.get(room).words.get(index);
		return rooms.get(room).word;
	}

	public String getWord(String room)
	{
		return rooms.get(room).word;
	}

	public void changePlayers(String room)
	{
		rooms.get(room).drawingPlayerID  ++;
		while (rooms.get(room).players.get(rooms.get(room).drawingPlayerID).spec) {
			rooms.get(room).drawingPlayerID  ++;

			if (rooms.get(room).drawingPlayerID >= rooms.get(room).players.size()) 
			{
				break;
			}
		}

		if (rooms.get(room).drawingPlayerID >= rooms.get(room).players.size()) 
		{
			rooms.get(room).drawingPlayerID = 0;
		}
	}

	public void resetGuesses(String room)
	{
		rooms.get(room).guesses = "";
	}

	public void addGuess(String room, String guess, int userID)
	{
		rooms.get(room).guesses += guess + "  #  " + getPlayerName(room, userID) + ",";
	}

	public String getGuesses(String room)
	{
		return rooms.get(room).guesses;
	}

	public void setPlayerSpec(String room, int id, boolean spec) {
		rooms.get(room).players.get(id).spec = spec;
	}
}
