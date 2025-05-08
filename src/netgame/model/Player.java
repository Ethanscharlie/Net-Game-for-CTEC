package netgame.model;

/**
 * Class fro the player, has a name and ip addr
 */
public class Player {
    public String ip = "";
    public String name = "";
    public int score = 0;

    public Player(String ip, String name) 
    {
        this.ip = ip;
        this.name = name;
    }
}
