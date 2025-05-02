package netgame.model;

import java.util.ArrayList;

/**
 * This class contians the game data, each rooms has an instance of game
 */
public class Game 
{
	public ArrayList<Player> players;
	public int drawingPlayerID = 0;
	public String canvasData;
    public String[] words;
    public String word;
    public String guesses;

    /**
     * Loads from the things to draw text file and initalizes data members
     * @param words the list of words to pick from
     */
    public Game(String[] words) 
    {
        players = new ArrayList<>();
        this.words = words;
    }
}
