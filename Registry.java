package GAME;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Registry { //Create an RMI registry on the server to listen for incoming client connections.
    public static void main(String[] args) throws RemoteException {

        try {
            int port=2000;
            LocateRegistry.createRegistry(port);
            (new Scanner(System.in)).nextLine();

    } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
