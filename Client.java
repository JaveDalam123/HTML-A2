import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // Delcare Components
    private JLabel heading = new JLabel("client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Arial", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done..");
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());

            // createGUI();
            // handleEvents();
            startReading();
            // this.startWriting();

        } catch (Exception e) {
            // TODO: handle Exception
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
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
        this.setTitle("clint Message [END]");
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
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEditable(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server : " + msg);
                    messagArea.append("Server :" + msg + "\n");

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

                System.out.println("connection is closed");

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client....");
        Client c = new Client();
        c.createGUI();
        c.handleEvents();

    }

}
