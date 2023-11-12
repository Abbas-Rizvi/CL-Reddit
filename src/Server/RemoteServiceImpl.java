package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServiceImpl extends Thread implements RemoteService{

    // public RemoteServiceImpl() throws RemoteException {

    //     // implement UnicastRemoteObject exportObject function
    //     // rather than extending class
    //     UnicastRemoteObject.exportObject(this, 0);
    // }


    @Override
    public String[] getSortedPosts() throws RemoteException {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getSortedPosts'");

        String[] test = {"this", "is", "test"};
        return test;
    }

    @Override
    public String[] getSearchedPosts(String searchTerm) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSearchedPosts'");
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
