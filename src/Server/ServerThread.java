package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.Console;
import java.util.Scanner;

public class ServerThread extends Thread {

    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;
    Socket s = null;

    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseConnect db = new DatabaseConnect("test.db");
    private static Console console = System.console();

    public ServerThread(Socket s) {
        this.s = s;
    }

    public void run() {

        // database variables
        db.setupDatabase();
        String username;
        char[] password;

        // setup buffered read write for input and output streams
        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        // main code
        // to be run on thread

        int userOption = mainMenu();

        switch (userOption) {

            case 1:
                System.out.println("Enter username: ");
                username = scanner.nextLine();

                password = console.readPassword("Enter password");

                db.loginUser(username, password.toString());

                break;

            case 2:

                // db.registerUser(username, password.toString());
                break;

            case 3:
                break;
        }

        try {

            // read line from user
            line = is.readLine();
            while (line.compareTo("QUIT") != 0) {

                os.println(line);
                os.flush();
                System.out.println("Response to Client  :  " + line);
                line = is.readLine();
            }

        } catch (IOException e) {

            line = this.getName(); // reused String line for getting thread name
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName(); // reused String line for getting thread name
            System.out.println("Client " + line + " Closed");
        }

        // close thread and server
        finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null) {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        } // end finally
    }

    // prints mainMenu
    // validates input
    public static int mainMenu() {

        System.out.println("--- Menu ---");
        System.out.println("1.Login");
        System.out.println("2.Register");
        System.out.println("3.Quit");

        int userInput;

        System.out.print("User select: ");
        userInput = scanner.nextInt();

        while (userInput > 3 && userInput <= 0) {
            System.out.print("Input not valid, please enter value as listed above");
            System.out.print("User select: ");
            userInput = scanner.nextInt();
        }

        return userInput;
    }

    // function for registering user
    // returns 0 for success
    // returns 1 for error
    public static int registerUser() {

        return 0;

    }

}