package GAME;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Player extends UnicastRemoteObject implements IPlayer,Serializable {
    int id;
    String Name;
    IServer server;

    public Player(String Name) throws RemoteException {
            super();
        if(Objects.equals(Name, "Computer")) this.id=777;
        else
        {this.id = IdGenerator.getInstance().getNextId();}
        this.Name = Name;
    }
    {
        try {
            server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



    public String getName() {
        return Name;
    }

    @Override
    public void simulateGameInfo() throws RemoteException {

    }

    @Override
    public void notifyWinner(String name) throws RemoteException {

    }


    @Override
    public void handleMove(int col) throws RemoteException {

    }

    @Override
    public void simulateScores() throws RemoteException {

    }

    @Override
    public void receive_invitation(int senderid,int idplayer,int gameid) throws RemoteException {

    }

    @Override
    public int getID() throws RemoteException {
        return id;
    }
}
