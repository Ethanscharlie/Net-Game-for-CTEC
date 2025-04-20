package netgame.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game 
{
	public ArrayList<String> players;
	public int drawingPlayerID = 0;
	public String canvasData;
    public ArrayList<String> words;
    public String word;

    public Game() 
    {
        players = new ArrayList<>();
        
        words = new ArrayList<>();

        Scanner fileScanner;
        try 
        {
            fileScanner = new Scanner(new File("thingstodraw.txt"));
            fileScanner.nextLine(); // Skip the first line since its just a github link

		    while (fileScanner.hasNextLine()) {
		        String line = fileScanner.nextLine();
		    	words.add(line);
		    }

		    fileScanner.close();
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
    }
}
