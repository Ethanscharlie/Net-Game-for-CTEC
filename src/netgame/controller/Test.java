package netgame.controller;

/**
 * Unit testing
 */
public class Test {
    static private int testsPassed;

    /**
     * Runns the unit tests
     * @param controller
     */
    public static void run(Controller controller) 
    {
        testsPassed = 0;
        testPlayerMethods(controller);

        System.out.printf("All %d tests passed!\n", testsPassed);
    }

    /**
     * Runns the unit tests on the methods adding and removing players
     * @param controller
     */
    private static void testPlayerMethods(Controller controller)
    {
        final String testRoom = controller.addRoom();

        final String testIP = "877.1.1.0";
        final String testIP2 = "877.1.1.3";
        final String testName = "Ethan";

        controller.registerPlayer(testRoom, testIP);

        final int playerID = controller.getPlayerID(testRoom, testIP);
        mockAssert(playerID != -1, "Cannot find player with get player id"); 

        controller.setPlayerName(testRoom, playerID, testName);
        mockAssert(controller.getPlayerName(testRoom, playerID).equals(testName), "Player name did not set correctly");

        controller.registerPlayer(testRoom, testIP2);
        final int playerID2 = controller.getPlayerID(testRoom, testIP2);
        controller.setPlayerName(testRoom, playerID2, testName);

        controller.removePlayer(testRoom, testIP);
        controller.removePlayer(testRoom, testIP2);
        final int playerIDRemoved = controller.getPlayerID(testRoom, testIP);
        mockAssert(playerIDRemoved == -1, "Player was not properly removed from room"); 
    }

    private static void mockAssert(boolean expression, String ifWrong) 
    {
        testsPassed ++;
        if (!expression) {
            throw new java.lang.RuntimeException(ifWrong);
        }
    }
}
