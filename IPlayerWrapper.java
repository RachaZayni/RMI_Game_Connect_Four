package GAME;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class IPlayerWrapper {
    private final IPlayer player;

    public IPlayerWrapper(IPlayer player) {
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        String s="";
        try {
        IServer server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");

            HashMap<Integer, Boolean> mapCon=server.Connect(player.getID(),false);
            boolean isconnected;
            isconnected=mapCon.getOrDefault(player.getID(),false);
            s= player.getName() + " - " +(isconnected?"Online":"Offline");
        } catch (RemoteException e) {
    } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        return s;
    }
}
