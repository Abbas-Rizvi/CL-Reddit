package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class fileClient {

    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Socket s = new Socket("localhost", 6666);
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter output = new PrintWriter(s.getOutputStream(), true);

            // System.out.println(input.readLine());

            // use variable to pass data to server
            // written in format 'OPTION:VALUE'
            // will start by creating and initializing to main menu selection

            String userInput;
            String serverOutput;
            output.flush();

            Boolean menu = true;

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

                // out.flush();

                // if (menu){
                // menu = false;
                // userInput = mainMenu();
                // } else
                // userInput = scan.nextLine();

                // // print to server & flush buffer
                // out.println(userInput.trim());
                // out.flush();

                // while (input.ready()){
                // serverOutput = input.readLine();

                // if (serverOutput.equals("PRINT_MENU")){
                // menu =true;
                // } else {
                // System.out.println(serverOutput);
                // }
                // }

            }

            // System.out.println("Done");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static void newPost(BufferedReader input, PrintWriter output) throws IOException {

        String line = input.readLine();

        System.out.println(line);
        output.flush();

        if (line.equals("User has not been logged in!")) {

        } else {

            System.out.print("Subject: ");
            String subject = scan.next();

            System.out.print("Body: ");
            String body = scan.next();

            output.println(subject);
            output.flush();

            output.println(body);
            output.flush();


            System.out.println(input.readLine());

        }

    }

    private static void upVotePost(BufferedReader input, PrintWriter output) throws IOException {
        System.out.println("Enter post ID to upvote: ");

        int usrInput = -1;
        boolean valid = false;

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

        output.println(usrInput);
        System.out.println(input.readLine());

    }

    private static void listPosts(BufferedReader input, PrintWriter output) throws IOException {

        String line;

        System.out.println("ID\t" +
                "USER\t\t" +
                "SCORE\t" +
                "SUBJECT\t\t" +
                "BODY\t\t");

        String[] compononts = new String[5];

        while (!(line = input.readLine()).equals("FINISHED")) {
            // System.out.println(line);

            try {
                compononts = line.split(";", 5);
            } catch (ArrayIndexOutOfBoundsException e) {

            }

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

    public static void registerUser(BufferedReader input, PrintWriter output) throws IOException {

        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();

        output.println(username);
        output.println(password);

        System.out.println(input.readLine());
    }

    public static void loginUser(BufferedReader input, PrintWriter output) throws IOException {

        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();

        output.println(username);
        output.println(password);

        System.out.println(input.readLine());
    }
}