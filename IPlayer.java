package GAME;
import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPlayer extends Remote {
    void handleMove( int col) throws RemoteException;
    void simulateScores() throws RemoteException;
    void receive_invitation(int senderid,int playerid, int gameid) throws RemoteException;
    int getID() throws RemoteException;
    String getName() throws RemoteException;
    void simulateGameInfo() throws RemoteException;
    void notifyWinner (String name) throws  RemoteException;

}