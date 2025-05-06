package netgame.controller;

import java.util.Scanner;

public class Runner
{
    public static void main(String[] args)
    {
        Controller app = new Controller();

        // Run tests
        Test.run(app);

        // Handle closing
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type anything to close");
        scanner.nextLine();
        app.stopServer();
        System.out.println("Server Closed.");
        scanner.close();
    }
}
