import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client Class
 * Client Socket running at port 2020
 * Ngrok Config: Server Address -> "X.tcp.ngrok.io" // Port Number ->
 */
public class Client {
    public static void main(String[] args) {

        // Localhost Configuration
        Client_SetUp clientSetUp = new Client_SetUp("localhost");
        clientSetUp.startRunning();

        // Ngrok Configuration
        /*Client_SetUp clientSetUp = new Client_SetUp("X.tcp.ngrok.io");
        clientSetUp.startRunning();*/

    }
}

class Client_SetUp extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private PrintWriter output;
    private BufferedReader input;
    private String message;
    private String serverIP;
    private Socket connection;
    private String name;
    private JFrame f1, f2;


}
