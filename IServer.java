package GAME;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

// Remote Interface for Game Service
public interface IServer extends Remote {
    HashMap<Integer, Boolean> Connect(int id,Boolean con) throws RemoteException;
    void disconnect(int idgame,IPlayer p) throws RemoteException;
    int registerPlayer(String username) throws RemoteException;
    List<String> getOnlinePlayers() throws RemoteException;
    HashMap<Integer, IPlayer > getAllPlayer() throws RemoteException;
    void invitePlayer(int idplayer,int senderID,int idgame) throws RemoteException;
    boolean isRegistered(int id) throws RemoteException;
    void turn_broadcast(int current_game_id) throws RemoteException;
    String getNameFromID(int id)throws RemoteException;
    HashMap<Integer, GameSession> getAllGames()throws RemoteException;
    void Notify_winner(int current_game_id,String name) throws RemoteException;
    GameSession computerGame(int playerid) throws RemoteException;
    List<String> getPlayersNameFromGameID(int id) throws RemoteException;
    GameSession addGameSession(int PlayerId,boolean computer) throws RemoteException;
    void addPlayerToGameSession(int idGame, int playerId) throws RemoteException;
    int Turn(int gameid,boolean inc) throws RemoteException;
    void Board_BroadCast(int current_Game_id, int finalCol) throws RemoteException;
    void add_Game_IPlayer(int idgame,IPlayer p) throws RemoteException;
    Integer Update_Score(Integer idp,boolean update)throws RemoteException;
    void scores_broadcast(int current_game_id)throws RemoteException;
    int IdFromTurn(int currentTurn,int gameid) throws RemoteException;
}
