package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FileServer {

    static final int PORT = 6666;

    // create scanner object

    public static void main(String[] args) throws IOException {

        // create server socket
        ServerSocket serverSock = null;
        Socket sock = null;

        try {
            // create file interface
            RemoteService rs = new RemoteServiceImpl();
            // Naming.rebind("//127.0.0.1/FileServer", rs);
            RemoteService stub = (RemoteService) UnicastRemoteObject.exportObject(rs, 0);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            // Registry registry = LocateRegistry.getRegistry(1099);
            registry.bind("RemoteService", stub);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();

        }

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

            } finally {
                serverSock.close();
            }
        }

    }

}