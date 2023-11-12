package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerThread extends Thread implements RemoteService{

    // create variables for recieving input and storing creds
    String line = null;
    BufferedReader input = null;
    PrintWriter output = null;
    Socket s = null;
    static boolean exit;

    // credentials
    private static String username;
    private static String password;
    private static Boolean loggedIn = false;

    // dataabase connection
    private static DatabaseConnect db = new DatabaseConnect("test.db");

    // socket connection
    public ServerThread(Socket s) {
        this.s = s;
    }

    public ServerThread() throws RemoteException {

        // implement UnicastRemoteObject exportObject function
        // rather than extending class
        UnicastRemoteObject.exportObject(this, 0);
    }

    // runs on each thread
    public void run() {

        // database setup
        db.setupDatabase();

        // setup buffered read write for input and output streams
        try {
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {

            // read line from user
            // store option and value as seperate
            // input for menu options passed in form
            // OPTION;VALUE
            String option;
            String value;

            // loop for each input
            while ((line = input.readLine()) != null) {

                output.flush();

                // split the menu option into subparts
                String[] parts = line.split(";");

                try {
                    option = parts[0]; // "MENU"
                    value = parts[1]; // "1"
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }

                // handle user option
                // currently only menu implemented
                switch (option) {

                    case ("MENU"):

                        // user selection from menu
                        switch (value) {

                            // login user
                            // read username and passowrd from client
                            // pass to database for validation
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

                            // register user
                            // read username and passowrd from client
                            // pass to database for validation and signup
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

                            // read posts
                            // reads all posts stored on database
                            // prints in form of bundled string, to be processed by client
                            case "3":
                                if (loggedIn) {

                                    System.out.println("Reading Posts...");
                                    String[] posts = db.listPosts();

                                    for (String str : posts) {
                                        output.println(str);
                                        output.flush();
                                    }

                                    // output once all posts have been sent
                                    output.println("FINISHED");

                                } else {
                                    output.println("User has not been logged in!");
                                }

                                break;

                            // upvote post
                            // receives the post ID from user
                            // sends command to databse to update
                            case "4":
                                if (loggedIn) {

                                    // process user input as integer
                                    // validate on client side
                                    int postId = Integer.parseInt(input.readLine());

                                    //send to database and output result
                                    db.upVotePost(postId);
                                    System.out.println("Upvote req sent");
                                    output.println("Post has been upvoted!");

                                } else {
                                    output.println("User has not been logged in!");
                                }

                                break;

                            // create new post
                            // receives the post information from user
                            // sends command to database to create
                            // uses stored ussr cred as login required
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

            // handle all execeptions
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
                loggedIn = false;
                input.close();
                s.close();
                output.close();
                System.out.println("Connection Closed!");
            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }

    }

    @Override
    public String[] getSortedPosts() throws RemoteException {

        String output[] = db.listSortedPosts();

        return output;
    }

    @Override
    public String[] getSearchedPosts(String searchTerm) throws RemoteException {

        String output[] = db.listSearchedPosts(searchTerm);

        return output;
    }

    @Override
    public String[] myPosts(String username) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'myPosts'");
    }

    @Override
    public int deletePost(String username, int id) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePost'");
    }

}