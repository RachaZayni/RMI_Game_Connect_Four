package GAME;

// ConnectFourServerImpl.java
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.List;

public class Server_Impl extends UnicastRemoteObject implements IServer {
    private HashMap<Integer, IPlayer > allPlayers = new HashMap<>();
    private HashMap<Integer, GameSession > Games = new HashMap<>();
    private HashMap<Integer, Boolean> OnPlayer =new HashMap<>();
    private Map<Integer, List<IPlayer>> gameIPlayers = new HashMap<>();
    private Map<Integer,Integer>Scores=new HashMap<>();

    private Map<Integer, Integer> turns = new HashMap<>();

    protected Server_Impl() throws RemoteException {
        super();
    }


    @Override
    public  HashMap<Integer, Boolean> Connect(int id,Boolean con) throws RemoteException {
       if(con){ OnPlayer.put(id, true);}
        return OnPlayer;
    }

    @Override
    public void disconnect(int idgame,IPlayer p) throws RemoteException {
        OnPlayer.put(p.getID(), false);
        List<IPlayer> IPlayersList = gameIPlayers.getOrDefault(idgame, new ArrayList<>());
        IPlayersList.remove(p);
        gameIPlayers.put(idgame, IPlayersList);
    }

    @Override
    public int registerPlayer(String name) throws RemoteException {
        IPlayer p=new Player(name);
        allPlayers.put(p.getID(),p);
        return p.getID();

    }


    public GameSession computerGame(int playerid) throws RemoteException{
        GameSession game=null;
        try {
            game =addGameSession(playerid,true);
            game.addPlayer(777);
            IPlayer p=new Player("Computer");
            allPlayers.put(p.getID(),p);
            Scores.put(777,0);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return game;
    }



    @Override
    public List<String> getOnlinePlayers() throws RemoteException {
        List<String> onlinePlayerNames = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : OnPlayer.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                IPlayer player = allPlayers.get(entry.getKey());
                if (player != null) {
                    onlinePlayerNames.add(player.getName());
                }
            }
        }
        return onlinePlayerNames;
    }



    @Override
    public HashMap<Integer, IPlayer > getAllPlayer() throws RemoteException {
        return allPlayers;
    }

    @Override
    public void invitePlayer(int idplayer,int senderId,int idgame) throws RemoteException {

        IPlayer pp=null;
        List<IPlayer> l=new ArrayList<>(allPlayers.values());
        for (IPlayer p :l){
            if(p.getID()==idplayer) pp=p;
        }

        for (Map.Entry<Integer, List<IPlayer>> entry : gameIPlayers.entrySet()) {
            List<IPlayer> playersList = entry.getValue();
            for (IPlayer player : playersList) {
                try {
                    // Example: assuming IPlayer has a method getID()
                    if (player.getID() == idplayer) {
                        player.receive_invitation(senderId,idplayer,idgame);
                        System.out.println("✅ Invitation sent to player ID " + idplayer + " in game " + entry.getKey());
                        return; // Stop after first match
                    }
                } catch (RemoteException e) {
                    System.err.println("❌ RemoteException when sending invitation to player " + idplayer);
                    e.printStackTrace();
                }
            }
        }
        System.out.println("⚠️ Player ID " + idplayer + " not found in any game.");


    }


    @Override
    public boolean isRegistered(int id) throws RemoteException {
        return allPlayers.containsKey(id);
    }



    @Override
    public String getNameFromID(int id) throws RemoteException {
        return allPlayers.get(id).getName();
    }



    @Override
    public HashMap<Integer, GameSession> getAllGames() throws RemoteException {
        return Games;
    }


    @Override
    public void addPlayerToGameSession(int idGame, int playerId) throws RemoteException {
        GameSession game = Games.get(idGame);
        if (game != null) {
            game.addPlayer(playerId);
            Scores.put(playerId,0);
        }
    }



    @Override
    public int Turn(int gameid,boolean inc) throws RemoteException {
        int size = Games.get(gameid).getIdPlayerList().size();
        int currentTurn = turns.getOrDefault(gameid, 0);
        if(inc==true) {
            currentTurn = (currentTurn + 1) % size;
            turns.put(gameid, currentTurn);
        }
        return currentTurn;
    }
    @Override
    public int IdFromTurn(int currentTurn,int gameid) throws RemoteException{
        return Games.get(gameid).getIdPlayerList().get(currentTurn);
    }




    @Override
    public void Board_BroadCast(int current_Game_id, int finalCol) throws RemoteException {
        List<IPlayer> l=gameIPlayers.get(current_Game_id);
        for (IPlayer p :l){
            p.handleMove(finalCol);
        }
    }

    @Override
    public void add_Game_IPlayer(int idgame, IPlayer p) throws RemoteException {
        List<IPlayer> IPlayersList = gameIPlayers.getOrDefault(idgame, new ArrayList<>());
        IPlayersList.add(p);
        gameIPlayers.put(idgame,IPlayersList);
    }

    @Override
    public Integer Update_Score(Integer idp, boolean update) throws RemoteException {
        int new_score=Scores.getOrDefault(idp,0);
        System.out.println(new_score);
        if(update){
            new_score++;
            Scores.put(idp,new_score);}
        System.out.println(Scores+" "+update);
        return Scores.get(idp);

    }

    @Override
    public void scores_broadcast(int current_game_id) throws RemoteException {
        List<IPlayer> l=gameIPlayers.get(current_game_id);
        for (IPlayer p :l){
            p.simulateScores();
        }
    }
    @Override
    public void turn_broadcast(int current_game_id) throws RemoteException {
        List<IPlayer> l=gameIPlayers.get(current_game_id);
        for (IPlayer p :l){
            p.simulateGameInfo();
        }
    }

    @Override
    public void Notify_winner(int current_game_id,String name) throws RemoteException {
        List<IPlayer> l=gameIPlayers.get(current_game_id);
        for (IPlayer p :l){
            p.notifyWinner(name);
        }
    }



    @Override
    public GameSession addGameSession(int PlayerId,boolean computer) throws RemoteException {
        GameSession game=new GameSession(computer);
        game.addPlayer(PlayerId);
        Games.put(game.getId(),game);
        Scores.put(PlayerId,0);
        return game;
    }


    // @Override
    //public void CompPlayer(GameSession game) throws RemoteException {

     //   Games.put(game.getId(), game);
      //  System.out.println("From ComPlayer game id : "+game.getId());
    //}

    @Override
    public List<String> getPlayersNameFromGameID(int idGame) throws RemoteException {
        GameSession game=Games.get(idGame);
        List<Integer> a  = new ArrayList<>(game.getIdPlayerList());
        List<String> p = new ArrayList<>();
        for (Integer pid : a) {
            p.add(allPlayers.get(pid).getName());
        }
        return p;
    }



    public static void main(String[] args) {
        try {
            Naming.rebind("rmi://127.0.0.1:2000/GameServer",new Server_Impl());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
