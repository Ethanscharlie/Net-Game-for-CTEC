package netgame.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import netgame.model.Game;
import netgame.model.Player;
import netgame.view.WebServer;

/**
 * Contains all the methods for the sever to interact with the game
 */
public class Controller
{
	private HashMap<String, Game> rooms;
	private WebServer server;

	/**
	 * Constructor for Controller
	 * 
	 * Initalized server, rooms, and opens a scanner to handle server closing
	 */
	public Controller()
	{
		this.server = new WebServer(this);
		this.rooms = new HashMap<>();

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
	 * @param room the room name
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
	 * @param room the room name
	 * @param ip The local ip of the client
	 */
    public void removePlayer(String room, String ip)
    {
		int target = -1;
		for (int index = 0; index < rooms.get(room).players.size(); index ++) {
			Player player = rooms.get(room).players.get(index);
			if (player.ip.equals(ip)) {
				target = index;
			}
		}

		if (target == -1) {
            System.out.println("Couldn't remove player, player is not in room");
			return;
		}

        rooms.get(room).players.remove(target);
    }

	/**
	 * @param room the room name
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

	/**
	 * @param room the room name
	 * @param ip The local ip of the client
	 * @param name The name for the player
	 */
	public void setPlayerName(String room, int id, String name) 
	{
		rooms.get(room).players.get(id).name = name;
	}
	
	/**
	 * @param room the room name
	 * @param ip The local ip of the client
	 * @return The players name
	 */
	public String getPlayerName(String room, int id) 
	{
		return rooms.get(room).players.get(id).name;
	}

	/**
	 * @param room the room name
	 * @return All the players in a single string sperated by commas 
	 */
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
	 * @param room the room name
     * @return String URL of html canvas data
     */
    public String getCanvasData(String room)
    {
        return rooms.get(room).canvasData;
    }

    /**
	 * @param room the room name
     * @param canvasData Set the string URL of html canvas data
     */
    public void setCanvasData(String room, String canvasData) 
    {
        rooms.get(room).canvasData = canvasData;
    }

	/**
	 * @param room the room name
	 * @return The id of the player who is the current drawer
	 */
	public int getDrawingPlayerID(String room)
	{
		return rooms.get(room).drawingPlayerID;
	}

	/**
	 * Grabs a new random word to set as the rooms word, and returns what that word is
	 * @param room the room name
	 * @return The new word
	 */
	public String newWord(String room) 
	{
		int index = (int)(Math.random() * rooms.get(room).words.size()); 
		rooms.get(room).word = rooms.get(room).words.get(index);
		return rooms.get(room).word;
	}

	/**
	 * @param room the room name
	 * @return The current word to be guessed
	 */
	public String getWord(String room)
	{
		return rooms.get(room).word;
	}

	/**
	 * Goes to the next player, if their at the end of the list then it goes to the first player
	 * @param room the room name
	 */
	public void changePlayers(String room)
	{
		rooms.get(room).drawingPlayerID  ++;

		if (rooms.get(room).drawingPlayerID >= rooms.get(room).players.size()) 
		{
			rooms.get(room).drawingPlayerID = 0;
		}
	}

	/**
	 * Resets the history of guesses in the guesses panel
	 * @param room the room name
	 */
	public void resetGuesses(String room)
	{
		rooms.get(room).guesses = "";
	}

	/**
	 * Adds a guess to the guess history
	 * @param room the room name
	 * @param guess the guess to add to the history
	 * @param userID the id of the user
	 */
	public void addGuess(String room, String guess, int userID)
	{
		rooms.get(room).guesses += guess + "  #  " + getPlayerName(room, userID) + ",";
	}

	/**
	 * @param room the room name
	 * @return The guesses history seperated by commas
	 */
	public String getGuesses(String room)
	{
		return rooms.get(room).guesses;
	}

	/**
	 * When called will remove all empty rooms from the rooms map
	 */
	public void removeEmptyRooms()
	{
		for (Map.Entry<String, Game> entry : rooms.entrySet()) {
			final String name = entry.getKey();
			final Game game = entry.getValue();

			if (game.players.isEmpty()) 
			{
				rooms.remove(name);
			}
		}
	}

	/**
	 * @return All room names seperated by commas
	 */
	public String getRoomsString()
	{
		removeEmptyRooms();

		String str = "";

		for (Map.Entry<String, Game> entry : rooms.entrySet()) {
			final String name = entry.getKey();
			final Game game = entry.getValue();

			str += name;
			str += ",";
		}

		return str;
	}

	/**
	 * Adds a new room with a randomly generated name
	 * @return The new rooms name
	 */
	public String addRoom()
	{
		Random r = new Random();
		Integer num = r.nextInt(10000);

		Game newGame = new Game();
		rooms.put(num.toString(), newGame);

		newWord(num.toString());
		return num.toString();
	}
}
