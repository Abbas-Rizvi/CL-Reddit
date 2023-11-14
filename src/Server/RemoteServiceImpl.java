package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServiceImpl extends Thread implements RemoteService{

    // public RemoteServiceImpl() throws RemoteException {

    //     // implement UnicastRemoteObject exportObject function
    //     // rather than extending class
    //     UnicastRemoteObject.exportObject(this, 0);
    // }

    private static DatabaseConnect db = new DatabaseConnect("test.db");

    @Override
    public String[] getSortedPosts() throws RemoteException {

        String[] posts = db.listSortedPosts();
        return posts;
    }

    @Override
    public String[] getSearchedPosts(String searchTerm) throws RemoteException {

        String[] posts = db.listSearchedPosts(searchTerm);
        return posts;
    }

    @Override
    public String[] myPosts(String username) throws RemoteException {

        String[] posts = db.listUserPosts(username);
        return posts;
    }

    @Override
    public int deletePost(String username, int id) throws RemoteException {

        int posts = db.deletePost(username, id);
        return  posts;
    }

    @Override
    public String[] viewFriendPosts(String username) {

        String[] posts = db.listUserPosts(username);
        return posts;
    }

}
