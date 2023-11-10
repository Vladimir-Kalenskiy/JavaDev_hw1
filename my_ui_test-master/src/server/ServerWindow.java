package server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ServerWindow extends JFrame {
    private static int POS_X = 500;
    private static int POS_Y = 500;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    static final String LOG_FILE = "chat_log.txt";

    private final JButton btnStart = new JButton("Start.");
    private final JButton btnStop = new JButton("Stop.");
    protected static final JTextArea log = new JTextArea();
    private boolean isServerWorking;

    public static void main(String[] args) {
        new ServerWindow();
    }

    ServerWindow() {
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                log.append("Server stopped\n");
                saveMessageToFile("Server started\n");
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                new ClientWindow(100, 500);
                new ClientWindow(900, 500);
                log.append("Server started\n");
                saveMessageToFile("Server started\n");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server.");
        setAlwaysOnTop(true);
        JPanel panBottom = new JPanel(new GridLayout(1, 2));
        panBottom.add(btnStart);
        panBottom.add(btnStop);

        add(log);
        add(panBottom, BorderLayout.SOUTH);

        setVisible(true);
    }


    protected static void saveMessageToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
