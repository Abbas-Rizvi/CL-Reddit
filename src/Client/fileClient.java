package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            // System.out.println(input.readLine());

            // use variable to pass data to server
            // written in format 'OPTION:VALUE'
            // will start by creating and initializing to main menu selection

            String userInput;
            out.flush();

            Boolean menu = false;

            while (true) {

                if (!menu){
                    menu = true;
                    userInput = mainMenu();
                } else
                    userInput = scan.nextLine();

                if (input.ready()){
                    System.out.print(input.readLine());
                }

                // print to server & flush buffer
                out.println(userInput.trim());
                out.flush();

            }

            // System.out.println("Done");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // prints main menu for user options
    // returns value for otpion pass from user
    public static String mainMenu() {

        String welcome = "Welcome to CL-Reddit!\n" +
                "You will be able to view and post messages to the forum, all over the command line!";

        String menu = "--- Menu ---\n" +
                "1.Login\n" +
                "2.Register\n" +
                "3.Read Posts\n" +
                "4.Quit\n";

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

                if (input > 0 && input <= 4) {
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

}