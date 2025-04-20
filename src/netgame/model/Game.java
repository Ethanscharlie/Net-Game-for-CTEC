package netgame.model;

import java.util.ArrayList;

public class Game 
{
	public ArrayList<String> players;
	public int drawingPlayerID = 0;
	public String canvasData;

    public Game() 
    {
        players = new ArrayList<>();
    }
}
