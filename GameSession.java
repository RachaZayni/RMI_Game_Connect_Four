package GAME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSession implements Serializable {
    int id;
    List<Integer> Players = new ArrayList<>();


    public GameSession(boolean computer) {
        if(computer){
            this.id=777;
        }
        else {
            this.id = IdGenerator.getInstance().getNextId() + 2000;
        }
    }
    public void addPlayer(int id){
        this.Players.add(id);


    }

    public List<Integer> getIdPlayerList() {
        return Players;
    }

    public int getId(){
        return id;
    }
    public void CompPlayer(){
        id=777;
    }
}

