package GAME;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConnectWindow extends JFrame {
    int id = 0;
    public ConnectWindow() {
        try {
            String name="";
            IServer server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");

        setTitle("Connect");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(25, 25, 50));

            JButton continueBtnComp = new JButton("Play with Computer");
            continueBtnComp.setBounds(110, 80, 220, 40);
            continueBtnComp.setBackground(new Color(46, 139, 87));
            continueBtnComp.setForeground(Color.WHITE);
            continueBtnComp.setFont(new Font("Arial", Font.BOLD, 16));
            continueBtnComp.setFocusPainted(false);
            panel.add(continueBtnComp);

            JButton NewGameBtn = new JButton("New Game");
            NewGameBtn.setBounds(110, 145, 220, 40);
            NewGameBtn.setBackground(new Color(46, 139, 87));
            NewGameBtn.setForeground(Color.WHITE);
            NewGameBtn.setFont(new Font("Arial", Font.BOLD, 16));
            NewGameBtn.setFocusPainted(false);
            panel.add(NewGameBtn);

        JButton continueBtnShared = new JButton("Enter Shared Game");
        continueBtnShared.setBounds(110, 220, 220, 40);
        continueBtnShared.setBackground(new Color(46, 139, 87));
        continueBtnShared.setForeground(Color.WHITE);
        continueBtnShared.setFont(new Font("Arial", Font.BOLD, 16));
        continueBtnShared.setFocusPainted(false);
        panel.add(continueBtnShared);







            String input = JOptionPane.showInputDialog("Enter your ID:");
            if (input != null) {
                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No input detected.");
            }

        if(!server.isRegistered(id) && id!=0) {
            JOptionPane.showMessageDialog(null, "You need to register before playing!");

            new RegistrationWindow();
            return;
        }
        else{
             name=server.getNameFromID(id);

        }
            JLabel welcomeLabel = new JLabel("Welcome, " + name + "!");
            welcomeLabel.setForeground(Color.WHITE);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
            welcomeLabel.setBounds(70, 40, 280, 30);
            panel.add(welcomeLabel);



            continueBtnComp.addActionListener(e -> {
                try {

                  GameSession game= server.computerGame(id);
                  System.out.println("Game id of computerGAME"+game.getId());
                   server.Connect(id,true);
                    new ConnectFourGUI(id,game.getId());  // Pass playerName and server
                    dispose(); // Close this window

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });




            NewGameBtn.addActionListener(e -> {

                try {
                    GameSession game=server.addGameSession(id,false);

                JOptionPane.showMessageDialog(null, "Share this id with your friend: "+game.getId());
                server.Connect(id,true);

                new ConnectFourGUI(id,game.getId());  // Pass playerName and server
                dispose(); // Close this window
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });

            continueBtnShared.addActionListener(e -> {

                int idGame = Integer.parseInt(JOptionPane.showInputDialog("Enter Game Id:"));
                try {
                    if(!server.getAllGames().keySet().stream().toList().contains(idGame))
                    {
                        JOptionPane.showMessageDialog(null, "This game is not available!");

                    }

                    // Launch ConnectFour GUI
                   else{
                       server.Connect(id,true);



                        server.addPlayerToGameSession(idGame,id);

                       new ConnectFourGUI(id,idGame);  // Pass playerName and server
                         dispose(); // Close this window
            }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });


            add(panel);
        setVisible(true);
    }  catch (NotBoundException e) {
        throw new RuntimeException(e);
    } catch (MalformedURLException e) {
        throw new RuntimeException(e);
    } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
}}
