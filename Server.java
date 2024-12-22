import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javafx.scene.input.KeyEvent;

import java.awt.Font;

public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Delcare compoents
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            // createGUI();
            // handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                // System.err.println("key released"+e.getKeyCode());
                // System.out.println("key relesed"+e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("You have pressed enter button");
                    String contententToSend = messageInput.getText();
                    messagArea.append("Me: " + contententToSend + "\n");
                    out.println(contententToSend);
                    out.flush();
                    messageInput.setText(" ");
                    messageInput.requestFocus();

                }
            }

        });
    }

    private void createGUI() {
        this.setTitle("Server Message [END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Componet
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // frame ka set layout
        this.setLayout(new BorderLayout());
        // adding the compoents to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(messagArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started..");

            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client : " + msg);
                    messagArea.append("Client : " + msg + "\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection closed");
            }
        };
        new Thread(r1).start();

    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("writer started...");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("Exit")) {
                        socket.close();
                        break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is sarver goging to start server");
          Server s=  new Server();
          s.createGUI();
           s.handleEvents();
    }

}
