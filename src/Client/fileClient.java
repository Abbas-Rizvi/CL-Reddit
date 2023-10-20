package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class fileClient {

    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            // create socket, reader and output printer
            // for server connection
            Socket s = new Socket("localhost", 6666);
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter output = new PrintWriter(s.getOutputStream(), true);


            // use variable to pass data to server
            // written in format 'OPTION:VALUE'
            String userInput;
            output.flush();


            // Loop receivng user input
            // invoke relevant function as needed
            while (true) {

                userInput = mainMenu();

                output.println(userInput);

                switch (userInput) {

                    case "MENU;1":
                        loginUser(input, output);
                        break;

                    case "MENU;2":
                        registerUser(input, output);
                        break;

                    case "MENU;3":
                        listPosts(input, output);
                        break;
                    case "MENU;4":
                        upVotePost(input, output);
                        break;

                    case "MENU;5":
                        newPost(input, output);

                        break;
                    case "MENU;6":
                        s.close();
                        break;
                    default:
                        break;

                }


            }

        } catch (Exception e) {
            // error handling
            System.out.println(e);
        }

    }

    // new post, used to create post in database
    // parameters passed to corresponding code on server
    private static void newPost(BufferedReader input, PrintWriter output) throws IOException {


        // read initial message from server
        // used to determine if client allowed to progress
        String line = input.readLine();
        System.out.println(line);
        output.flush();

        // if user not logged in, skip body of code
        if (line.equals("User has not been logged in!")) {

        } else {

            // request subject and body for post
            // other parameters are handled by server using stored credentials
            System.out.print("Subject: ");
            String subject = scan.next();

            System.out.print("Body: ");
            String body = scan.next();


            // print data to server
            output.println(subject);
            output.flush();

            output.println(body);
            output.flush();


            // output server response to client
            System.out.println(input.readLine());

        }

    }

    // up vote post, used to select post and increase score
    // parameters passed to corresponding code on server
    private static void upVotePost(BufferedReader input, PrintWriter output) throws IOException {
        System.out.println("Enter post ID to upvote: ");

        int usrInput = -1;
        boolean valid = false;

        // validate input to ensure only integer is being entered
        while (valid == false) {
            System.out.print("User Selection: ");

            if (scan.hasNextInt()) {
                usrInput = scan.nextInt();
                valid = true;

            } else {
                System.out.println("Invalid input");
                scan.next();
            }
        }

        // pass paremeter to server and output server response
        output.println(usrInput);
        System.out.println(input.readLine());

    }

    
    // list posts, used to show all posts on server and creator
    // parameters passed to corresponding code on server
    private static void listPosts(BufferedReader input, PrintWriter output) throws IOException {

        String line;

        // print headers for table
        System.out.println("ID\t" +
                "USER\t\t" +
                "SCORE\t" +
                "SUBJECT\t\t" +
                "BODY\t\t");

        // create array to store data values for each part of data line
        String[] compononts = new String[5];

        //loop through until all lines are read
        while (!(line = input.readLine()).equals("FINISHED")) {
 
            // split the packaged data String using the delimeter 
            try {
                compononts = line.split(";", 5);
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            // print in tabular format
            System.out.println(compononts[0] + "\t" +
                    compononts[1] + "\t\t" +
                    compononts[2] + "\t" +
                    compononts[3] + "\t\t" +
                    compononts[4] + "\t\t");

        }

    }

    // prints main menu for user options
    // returns value for otpion pass from user
    public static String mainMenu() {
 
        String welcome = "\n\nWelcome to CL-Reddit!\n" +
                "You will be able to view and post messages to the forum, all over the command line!";

        String menu = "--- Menu ---\n" +
                "1.Login\n" +
                "2.Register\n" +
                "3.Read Posts\n" +
                "4.Upvote Post\n" +
                "5.New Post\n" +

                "6.Quit\n";

        System.out.println(welcome);
        System.out.println(menu);

        int input;
        boolean valid = false;

        // validate user option input
        while (valid == false) {
            System.out.print("User Selection: ");

            // if user enters integer
            // record value
            if (scan.hasNextInt()) {
                input = scan.nextInt();

                if (input > 0 && input <= 6) {
                    valid = true;
                    return "MENU;" + input;
                } else {
                    System.out.println("Invalid input, please enter number from menu");
                }
            } else {
                System.out.println("Invalid input, please enter number from menu");
                scan.next();
            }
        }

        return null;

    }

    
    // register user, used to register a new user on server
    // parameters passed to corresponding code on server
    public static void registerUser(BufferedReader input, PrintWriter output) throws IOException {


        // read in username and password
        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();

        //send to server and print response
        output.println(username);
        output.println(password);

        System.out.println(input.readLine());
    }

    // login user, used to login existing user
    // parameters passed to corresponding code on server
    public static void loginUser(BufferedReader input, PrintWriter output) throws IOException {

        // read in username and password
        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();


        //send to server and print response
        output.println(username);
        output.println(password);

        System.out.println(input.readLine());
    }
}