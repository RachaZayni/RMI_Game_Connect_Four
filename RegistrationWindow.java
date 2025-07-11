package GAME;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RegistrationWindow extends JFrame {

    public RegistrationWindow() {

        try {
           IServer server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");


            setTitle("Registration");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(35, 35, 70));

            JLabel title = new JLabel("New Player Registration");
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 20));
            title.setBounds(80, 30, 250, 30);
            panel.add(title);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setForeground(Color.LIGHT_GRAY);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            nameLabel.setBounds(50, 120, 80, 25);
            panel.add(nameLabel);

            JTextField nameField = new JTextField();
            nameField.setBounds(120, 120, 200, 25);
            panel.add(nameField);

            JButton registerBtn = new JButton("Register");
            registerBtn.setBounds(120, 180, 150, 40);
            registerBtn.setBackground(new Color(70, 130, 180));
            registerBtn.setForeground(Color.WHITE);
            registerBtn.setFont(new Font("Arial", Font.BOLD, 16));
            registerBtn.setFocusPainted(false);
            panel.add(registerBtn);

            registerBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "name cannot be empty.");


                }
                else {
                    try {
                        int id=server.registerPlayer(name);
                        JOptionPane.showMessageDialog(this, "Your ID is: "+id+"\n Remember it to be able to enter next time!");

                        new ConnectWindow();
                        dispose();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "❌ Registration failed."+ex);
                    }
                }});


            add(panel);
            setVisible(true);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}