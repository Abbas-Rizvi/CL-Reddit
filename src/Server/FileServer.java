package Server;

import java.io.Console;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class FileServer {

    static final int PORT = 6666;

    // create scanner object

    public static void main(String[] args) {

        // create server socket
        ServerSocket serverSock = null;
        Socket sock = null;

        // connect to server
        try {

            serverSock = new ServerSocket(PORT);
            System.out.println("Server active...");

        } catch (Exception e) {
            System.out.println(e);
        }

        while (true) {
            try {

                // establishes connection
                sock = serverSock.accept();
                System.out.println("Client has connected");

                ServerThread st = new ServerThread(sock);
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            } 

        }

    }

}