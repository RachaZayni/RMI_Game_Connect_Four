package GAME;

import GAME.ConnectWindow;
import GAME.IServer;
import GAME.RegistrationWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class StartWindow extends JFrame {
    public IServer server;

    public StartWindow() {

        setTitle("Welcome to Connect Four");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // UI Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 30, 60));

        JLabel title = new JLabel("🎮 Welcome to Connect Four!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(60, 30, 300, 30);
        panel.add(title);

        JButton startBtn = new JButton("Start Game");
        JButton RegBtn = new JButton("Register");
        startBtn.setBounds(40, 120, 150, 40);
        RegBtn.setBounds(230, 120, 150, 40);
        startBtn.setBackground(new Color(70, 130, 180));
        startBtn.setForeground(Color.WHITE);
        RegBtn.setBackground(new Color(70, 130, 180));
        RegBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        RegBtn.setFocusPainted(false);
        startBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(startBtn);
        RegBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(RegBtn);

        add(panel);

        // Connect to server
        try {
            server = (IServer) Naming.lookup("rmi://127.0.0.1:2000/GameServer");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to connect to server.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        startBtn.addActionListener(e -> {
            new ConnectWindow();
            dispose();
        });


        RegBtn.addActionListener(e ->{
            new RegistrationWindow();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartWindow());

    }}