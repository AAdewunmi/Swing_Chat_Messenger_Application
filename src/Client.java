import javax.swing.*;

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



}
