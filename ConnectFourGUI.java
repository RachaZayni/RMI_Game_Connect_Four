package GAME;

import javax.swing.*;
import javax.swing.Timer;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ConnectFourGUI extends JFrame implements IPlayer{

    IServer server;
   int currGameId;
   int currentPlayerid;
   IPlayer me=this;

    int index;
    {
        try {
            server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");

         // me.receive_invitation(1);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private DefaultListModel<IPlayerWrapper> playerListModel = new DefaultListModel<>();
    private JList<IPlayerWrapper> playerList = new JList<>(playerListModel);
    private JTextArea gameInfoArea = new JTextArea(5, 30);
    private JTextArea scoreArea = new JTextArea(5, 30);
    private JButton inviteButton = new JButton("🎮 Invite Player");
    private JButton refreshButton = new JButton("🔄 Refresh List");
    private JButton exitButton = new JButton("❌ Exit Game");
    private JPanel gameBoardPanel = new JPanel(new GridLayout(6, 7));
    private JLabel statusLabel = new JLabel("✅ Status: Connected to Server", JLabel.CENTER);
    private Color[] PLAYER_COLORS;
    private int[][] board = new int[6][7];
    private JPanel[][] cells = new JPanel[6][7];
    IPlayer stub;
    public ConnectFourGUI(int current_Player_id, int current_Game_id) {
        currGameId=current_Game_id;
        currentPlayerid=current_Player_id;
        try {

         stub = (IPlayer) UnicastRemoteObject.exportObject(this, 0);

            server.add_Game_IPlayer(currGameId,stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }



        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        String name= null;
        try {
            name = server.getNameFromID(current_Player_id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        setTitle("Connect Four Multiplayer Platform - "+name);
        setSize(800, 400);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(24, 26, 48));
        JLabel titleLabel = new JLabel("Connect Four - Multiplayer", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(270, 600));
        leftPanel.setBackground(new Color(33, 37, 66));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "👥 Players", 0, 0, new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));

        playerList.setFont(new Font("Verdana", Font.PLAIN, 14));
        playerList.setBackground(new Color(50, 55, 90));
        playerList.setForeground(Color.WHITE);
        JScrollPane playerScroll = new JScrollPane(playerList);
        leftPanel.add(playerScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(new Color(33, 37, 66));
        inviteButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        inviteButton.setBackground(new Color(243, 156, 18));
        inviteButton.setForeground(Color.BLACK);
        refreshButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        refreshButton.setBackground(new Color(41, 128, 185));
        refreshButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        exitButton.setBackground(new Color(192, 57, 43)); // Red background
        exitButton.setForeground(Color.WHITE);
        buttonPanel.add(inviteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);






        gameBoardPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "🎲 Game Board", 0, 0, new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));
        gameBoardPanel.setBackground(new Color(26, 29, 53));




        try {

            IdList = server.getAllGames().get(currGameId).getIdPlayerList();
            p = server.getPlayersNameFromGameID(currGameId);
            index= IdList.indexOf(currentPlayerid); //for indicating color
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JPanel cellPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(getBackground());
                        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                    }
                };
                cellPanel.setOpaque(false);
                cellPanel.setBackground(new Color(100, 100, 150));
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                final int finalCol = col;
                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        try {
                            if(currGameId!=777) {
                                if (server.Turn(current_Game_id, false) == index) {
                                    server.Board_BroadCast(current_Game_id, finalCol);
                                    server.Turn(current_Game_id, true);

                                } else {
                                    JOptionPane.showMessageDialog(null, "It's not your turn!", "Warning", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                            else {
                                handleCompGameMove(finalCol);
                                simulateCompGameInfo();
                            }



                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }


                    }
                });
                gameBoardPanel.add(cellPanel);
                cells[row][col] = cellPanel;
            }
        }

        add(gameBoardPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rightPanel.setPreferredSize(new Dimension(270, 600));
        rightPanel.setBackground(new Color(33, 37, 66));

        JPanel gameInfoPanel = new JPanel(new BorderLayout());
        gameInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "📜 Current Game Info", 0, 0, new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));
        gameInfoPanel.setBackground(new Color(33, 37, 66));
        gameInfoArea.setEditable(false);
        gameInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        gameInfoArea.setBackground(new Color(50, 55, 90));
        gameInfoArea.setForeground(Color.WHITE);
        gameInfoPanel.add(new JScrollPane(gameInfoArea), BorderLayout.CENTER);

        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "🏆 Scoreboard", 0, 0, new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));
        scorePanel.setBackground(new Color(33, 37, 66));
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scoreArea.setBackground(new Color(50, 55, 90));
        scoreArea.setForeground(Color.WHITE);
        scorePanel.add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        rightPanel.add(gameInfoPanel);
        rightPanel.add(scorePanel);

        add(rightPanel, BorderLayout.EAST);

        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(173, 216, 230));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);

        inviteButton.addActionListener(e -> invitePlayer());
        refreshButton.addActionListener(e -> {
            try {
            simulatePlayers();server.turn_broadcast(currGameId);

                simulateScores();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        exitButton.addActionListener(e -> {
            try {
                server.disconnect(currGameId,stub);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        });


        simulatePlayers();
        try {

        if(current_Game_id!=777) simulateGameInfo();
        else simulateCompGameInfo();


            simulateScores();


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        //setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
    }

    List<Integer> IdList= null;
    int playerturn=0;
    @Override
    public void handleMove(int col) throws RemoteException{

        playerturn= server.Turn(currGameId,false);
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = playerturn+1;

                cells[row][col].setBackground(PLAYER_COLORS[playerturn]);
                cells[row][col].repaint();
                if (checkWin(row, col)) {
                    List<Integer> IdList=server.getAllGames().get(currGameId).getIdPlayerList();

                    int turn=server.Turn(currGameId,false);
                    int serv=server.IdFromTurn(turn,currGameId);
                    if(currentPlayerid==serv){
                       server.Update_Score(currentPlayerid,true);
                       server.scores_broadcast(currGameId);
                    }
                    server.Notify_winner(currGameId,server.getNameFromID(IdList.get(server.Turn(currGameId,false))));
                    System.out.println(server.getNameFromID(IdList.get(server.Turn(currGameId,false))));
                    resetBoard();

                } else {
                    try {
                        p = server.getPlayersNameFromGameID(currGameId);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    server.turn_broadcast(currGameId);

                }
                break;
            }
        }
    }




    public void handleCompGameMove(int col){

        try {
            if(playerturn==0){
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = playerturn+1;

                cells[row][col].setBackground(PLAYER_COLORS[playerturn]);
                cells[row][col].repaint();
                if (checkWin(row, col)) {
                    List<Integer> IdList=server.getAllGames().get(currGameId).getIdPlayerList();

                    int turn=server.Turn(currGameId,false);
                    int serv=server.IdFromTurn(turn,currGameId);
                    if(currentPlayerid==serv){
                        server.Update_Score(currentPlayerid,true);
                        simulateScores();

                    }
                    JOptionPane.showMessageDialog(null, server.getNameFromID(IdList.get(server.Turn(currGameId,false)))+" Wins! ", "Winning ", JOptionPane.INFORMATION_MESSAGE);

                    resetBoard();

                } else {
                    playerturn=1;

                    simulateCompGameInfo();
                    Timer timer = new Timer(2000, e -> {
                       computerMove();
                        ((Timer) e.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();



                }
                break;
            }

        }
            }

      } catch (RemoteException e) {
        throw new RuntimeException(e);
    }
}

    private void computerMove() {

        Random rand = new Random();
        int col;

        // Try to find a valid column
        List<Integer> validCols = new ArrayList<>();
        for (int c = 0; c < 7; c++) {
            if (board[0][c] == 0) {
                validCols.add(c);
            }
        }

        if (validCols.isEmpty()) return; // Board full

        col = validCols.get(rand.nextInt(validCols.size()));

        // Find the lowest empty row in this column
        int row = -1;
        for (int r = 6 - 1; r >= 0; r--) {
            if (board[r][col] == 0) {
                row = r;
                break;
            }
        }

        if (row != -1) {
            board[row][col] = 2; // Assuming 2 means computer
            cells[row][col].setBackground(PLAYER_COLORS[1]);
            cells[row][col].repaint();

            if (checkWin(row, col)) {
                List<Integer> IdList = null;
                try {
                    IdList = server.getAllGames().get(currGameId).getIdPlayerList();
                    int turn = server.Turn(currGameId, false);
                    int serv = server.IdFromTurn(turn, currGameId);

                    if (currentPlayerid == serv) {
                        server.Update_Score(currentPlayerid, true);
                        server.scores_broadcast(currGameId);
                    }

                   // JOptionPane.showMessageDialog(null,
                    //        server.getNameFromID(IdList.get(server.Turn(currGameId, false))) + " Wins!",
                     //       "Winning", JOptionPane.INFORMATION_MESSAGE);

                    resetBoard();


            }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
            }
             else {
                playerturn = 0; // Back to human
        }
    }
        simulateCompGameInfo();
    }



    private boolean checkWin(int row, int col) {
        int player = board[row][col];
        return checkDirection(row, col, 1, 0, player) + checkDirection(row, col, -1, 0, player) > 2 ||
                checkDirection(row, col, 0, 1, player) + checkDirection(row, col, 0, -1, player) > 2 ||
                checkDirection(row, col, 1, 1, player) + checkDirection(row, col, -1, -1, player) > 2 ||
                checkDirection(row, col, 1, -1, player) + checkDirection(row, col, -1, 1, player) > 2;
    }

    private int checkDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 0;
        for (int i = 1; i < 4; i++) {
            int r = row + dRow * i;
            int c = col + dCol * i;
            if (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == player) count++;
            else break;
        }
        return count;
    }

    private void resetBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                board[row][col] = 0;
                cells[row][col].setBackground(new Color(100, 100, 150));
                cells[row][col].repaint();
            }
        }


    }


    private Color generateColor(int index) {
        // Spread hues evenly across the HSB spectrum
        float hue = (index * 0.618f) % 1.0f; // Golden ratio-based hue distribution
        return Color.getHSBColor(hue, 0.85f, 0.85f);
    }

    private void setPlayerColors(int numPlayers) {
        PLAYER_COLORS = new Color[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            PLAYER_COLORS[i] = generateColor(i);
        }
    }

    private void simulatePlayers() {
        playerListModel.clear();
        try {
            HashMap<Integer, IPlayer > allplayers = server.getAllPlayer();

            System.out.println(allplayers);

            List<String> onplayers = server.getOnlinePlayers();
            System.out.println("online "+ onplayers);
            for (Map.Entry<Integer,IPlayer> entry : allplayers.entrySet()) {
                IPlayer p = entry.getValue();
            if (onplayers.contains(p.getName()) && p.getID()!=777 )// && server.getAllGames().get(currGameId).getIdPlayerList().contains(player.getKey()))
            {
                playerListModel.addElement(new IPlayerWrapper(p));
            }
            else if (!Objects.equals(p.getName(), "Computer")){
                playerListModel.addElement(new IPlayerWrapper(p));
            }
        }
           // }
    } catch (RemoteException e) {
        throw new RuntimeException(e);
    }

    }

    List<String> p;

    private void simulateCompGameInfo() {

        try {
            System.out.println(playerturn);
            setPlayerColors(p.size());
            StringBuilder display = new StringBuilder("Current Players:\n");

            String computerName = "Computer";
            String humanName = server.getNameFromID(currentPlayerid);

            display.append(playerturn == 1 ? "→ " : "  ").append(computerName).append("\n");
            display.append(playerturn == 0 ? "→ " : "  ").append(humanName);



            gameInfoArea.setText(display.toString());

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public void simulateGameInfo() throws RemoteException{

        try {

            int turn = server.Turn(currGameId, false);
            List<Integer> IdList = server.getAllGames().get(currGameId).getIdPlayerList();

            p = server.getPlayersNameFromGameID(currGameId);
            int index = IdList.indexOf(currentPlayerid);
            setPlayerColors(p.size());
            StringBuilder display = new StringBuilder("Current Players:\n");
            for (int i = 0; i < p.size(); i++) {
                if (IdList.indexOf(IdList.get(i)) == (turn+1)%IdList.size()) {//Objects.equals(p.get(i),server.getNameFromID(turn+1))) {
                    display.append("→ ");
                } else {
                    display.append("   ");
                }
                display.append(p.get(i)).append("\n");
            }

            gameInfoArea.setText(display.toString());

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyWinner(String name) throws RemoteException {
       // JOptionPane.showMessageDialog(this, name+" Wins! ", "Winning ", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(name+" Wins!!");
    }

    @Override
    public void simulateScores() throws RemoteException {
        try {

                List<Integer> IdList = server.getAllGames().get(currGameId).getIdPlayerList();

                StringBuilder display = new StringBuilder("Scores:\n");
                for (int i : IdList) {
                    display.append(server.getNameFromID(i)).append(": ").append(server.Update_Score(i, false)).append("\n");
                }
                scoreArea.setText(String.valueOf(display));

     } catch (RemoteException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public void receive_invitation(int senderid, int idplayer,int idgame) throws RemoteException {
        String senderName = server.getNameFromID(senderid);
        System.out.println(senderName + " invites you to play!");

        String message = senderName + " invites you to play!";
        String title = "Game Invitation";

        // Define custom buttons
        Object[] options = { "Enter Game", "Decline" };

        // Show the option dialog
        int response = JOptionPane.showOptionDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // default button
        );


        if (response == JOptionPane.YES_OPTION) {
            System.out.println("✅ Player accepted the invitation.");
            server.addPlayerToGameSession(idgame,idplayer);
            server.disconnect(currGameId,stub);
            server.Connect(idplayer,true);
            new ConnectFourGUI(idplayer,idgame);
            this.dispose();

        } else {
            System.out.println("❌ Player declined the invitation.");
        }
    }


    @Override
    public int getID() throws RemoteException {
        return currentPlayerid;
    }

    private void invitePlayer() {
        try {
            IPlayerWrapper wrapper = (IPlayerWrapper) playerList.getSelectedValue();
            IPlayer selected = wrapper.getPlayer();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a player to invite.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }


         server.invitePlayer(selected.getID(),currentPlayerid,currGameId);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
       // JOptionPane.showMessageDialog(this, "Invitation sent to " + selected.Name, "Invite Sent", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnectFourGUI(1,1).setVisible(true));
    }
}
