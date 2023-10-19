package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Console;
import java.util.Scanner;

public class ServerThread extends Thread {

    String line = null;
    BufferedReader input = null;
    PrintWriter output = null;
    Socket s = null;
    static boolean exit;
    private static String username;
    private static String password;
    private static Boolean loggedIn = false;

    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseConnect db = new DatabaseConnect("test.db");
    private static Console console = System.console();

    public ServerThread(Socket s) {
        this.s = s;
    }

    public void run() {

        // database variables
        db.setupDatabase();

        // flag for exiting program
        boolean exit = false;

        // setup buffered read write for input and output streams
        try {
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {

            // read line from user
            // line = input.readLine();
            String option;
            String value;

            // output.println("Connected!");
            while ((line = input.readLine()) != null) {

                output.flush();
                String[] parts = line.split(";");

                try {
                    option = parts[0]; // "MENU"
                    value = parts[1]; // "1"
                } catch (ArrayIndexOutOfBoundsException e) {
                    // output.println("PRINT_MENU");
                    // printMenu(output);
                    continue;
                }

                // System.out.println(option);
                // System.out.println(value);

                switch (option) {

                    case ("MENU"):

                        switch (value) {

                            case "1":

                                username = input.readLine();
                                password = input.readLine();

                                if (db.loginUser(username, password.toString()) == 0) {
                                    loggedIn = true;
                                    output.println("Looged In!");
                                } else {
                                    output.println("Login Failed");
                                }

                                break;

                            case "2":

                                username = input.readLine();
                                password = input.readLine();

                                if (db.registerUser(username, password.toString()) == 0) {
                                    loggedIn = true;
                                    output.println("User " + username + " Registered!");
                                } else {
                                    output.println("Registration Failed");
                                }

                                break;

                            case "3":

                                if (loggedIn) {

                                    System.out.println("Reading Posts...");
                                    String[] posts = db.listPosts();
                                    String postList = "";

                                    for (String str : posts) {
                                        output.println(str);
                                        output.flush();
                                    }

                                    output.println("FINISHED");

                                } else {
                                    output.println("User has not been logged in!");
                                }

                                break;

                            case "4":

                                if (loggedIn) {

                                    String line = null;
                                    // while (input.readLine() == null)
                                    //     line = input.readLine();

                                    int postId = Integer.parseInt(input.readLine());

                                    db.upVotePost(postId);
                                    System.out.println("Upvote req sent");
                                    output.println("Post has been upvoted!");

                                } else {
                                    output.println("User has not been logged in!");
                                }

                                break;

                            case "5":
                                if (loggedIn) {

                                    output.println("New Post:");

                                    String subject = input.readLine();
                                    String body = input.readLine();
    
                                    db.newPost(username, subject, body);
                                    output.println("Post Created!");


                                } else {
                                    output.println("User has not been logged in!");
                                }

                                break;
                        }

                        break;

                    default:
                        break;
                }

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
                loggedIn=false;
                input.close();
                s.close();
                output.close();
                System.out.println("Connection Closed!");
            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }

    }

}