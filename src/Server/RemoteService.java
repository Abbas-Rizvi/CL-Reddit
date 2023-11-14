package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote{

    // get posts sorted by score
    String[] getSortedPosts() throws RemoteException;

    // get posts matching search criteria
    String[] getSearchedPosts(String searchTerm) throws RemoteException;

    // get posts created by user
    String[] myPosts(String username) throws RemoteException;

    // delete post
    int deletePost(String username, int id) throws RemoteException;

    // show friend posts
    String[] viewFriendPosts(String username) throws RemoteException;
    

}