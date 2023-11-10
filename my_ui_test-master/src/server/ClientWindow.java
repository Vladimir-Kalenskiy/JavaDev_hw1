package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static server.ServerWindow.LOG_FILE;


public class ClientWindow extends JFrame {
    private static int POS_X = 500;
    private static int POS_Y = 500;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JButton btnSend = new JButton("Send");
    private final JButton btnLogin = new JButton("Login");
    private final JTextArea textOutput = new JTextArea("");
    private final JTextField textInput = new JTextField();
    private String strLogin = "";

    public static void setPosX(int posX) {
        POS_X = posX;
    }

    public static void setPosY(int posY) {
        POS_Y = posY;
    }

    public static void main(String[] args) {

        new ClientWindow();
    }

    protected ClientWindow(int POS_X, int POS_Y) {
        setPosX(POS_X);
        setPosY(POS_Y);
        new ClientWindow();
    }

    protected ClientWindow() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client.");
        setAlwaysOnTop(true);

        loadChatHistory();

        JPanel topPanel = new JPanel(new GridLayout(2, 3));
        JTextField login = new JTextField("Login: ");
        topPanel.add(login);
        JTextField ipAdress = new JTextField("IP address: ");
        topPanel.add(ipAdress);
        JTextField port = new JTextField("Port: ");
        topPanel.add(port);
        JTextField passwordField = new JPasswordField("Password: ");
        topPanel.add(passwordField);
        topPanel.add(btnLogin);


        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(strLogin);
            }
        });
        textInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(strLogin);
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strLogin = login.getText().trim();
                textOutput.append("Connecting " + strLogin.trim() + "\n");
                ServerWindow.log.append("Connecting " + strLogin.trim() + "\n");
            }
        });


        JPanel centerPanel = new JPanel(new GridLayout());
        centerPanel.add(textOutput);

        JPanel downPanel = new JPanel(new GridLayout(1, 2));
        downPanel.add(textInput);
        downPanel.add(btnSend);


        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);

    }

    private void sendMessage(String login) {
        String message = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss")) + " " + login + " : " + textInput.getText() + "\n";
        textOutput.append(message);
        ServerWindow.saveMessageToFile(message);
        textInput.setText("");
    }

    private void loadChatHistory() {
        File file = new File(LOG_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    textOutput.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
