package GAME;

public class IdGenerator {
    static int lastId;
    static IdGenerator instance;
    private IdGenerator()
    {
        lastId=0;
    }

    public static IdGenerator getInstance()
    {
        if (instance==null)
            instance=new IdGenerator();
        return instance;
    }

    public int getNextId()
    {
        lastId++;
        if (lastId==777) lastId++;
        return lastId;
    }
}
