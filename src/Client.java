import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
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

    private final JTextField userText;
    private final JTextArea chatWindow;
    private PrintWriter output;
    private BufferedReader input;
    private final String serverIP;
    private Socket connection;
    private String name;
    private final JFrame f1;
    private final JFrame f2;

    public Client_SetUp(String host){

        f1 = new JFrame();
        serverIP = host;
        f1.setSize(300, 500);
        JLabel jLabel = new JLabel("Enter Your Name: ");
        JTextField jTextField = new JTextField(20);
        JButton jButton = new JButton("Submit");
        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        jPanel.add(jTextField);
        jPanel.add(jButton);
        f1.add(jPanel);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = jTextField.getText();
                f1.setVisible(false);
                f1.dispose();
                f2.setVisible(true);
            }
        });

        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        f2 = new JFrame();
        chatWindow = new JTextArea();
        userText = new JTextField();
        Button buttonEnd = new Button("End");
        userText.setEditable(true);
        chatWindow.setEditable(false);
        userText.addActionListener(e -> {
            sendMessage(e.getActionCommand());
            userText.setText("");
        });
        buttonEnd.addActionListener(e -> {
            sendMessage("Client_wants_to_end_the_connection");
            closeStreams();
            f2.dispose();
            System.exit(0);
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        mainPanel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        bottomPanel.add(userText, BorderLayout.CENTER);
        bottomPanel.add(buttonEnd, BorderLayout.LINE_END);
        f2.getContentPane().add(mainPanel);
        f2.setSize(300, 500);
        f2.setVisible(false);
        f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    } // End Constructor

    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException e){
            showMessage("\n Server not found!");
        }catch(IOException e){
            System.out.println("Error startRunning()");
            e.printStackTrace();
        }finally{
            closeStreams();
        }
    }

    private void connectToServer() throws IOException {
        // Localhost connection
        showMessage("Attention Connection ... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 2020);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());

        // Ngrok connection
        /*showMessage("Attention Connection ... \n");
        connection = new Socket(InetAddress.getByName(serverIP), XXXXX);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());*/
    }

    private void setupStreams() throws IOException{
        output = new PrintWriter(connection.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        showMessage("\n Your streams are now good to go \n");
    }

    private void whileChatting() throws IOException{
        while(true){
            try{
                String message = input.readLine();
                showMessage("\n" + message);
            }catch(Exception e){
                showMessage("Some Error Occurred: whileChatting()");
                e.printStackTrace();
            }
        }
    }

    private void closeStreams(){
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException e){
            System.out.println("Error: closeStreams()");
            e.printStackTrace();
        }
    }

    private void sendMessage(String message){
        try{
            output.println(name + " - " + message);
            showMessage("\n" + name + " - " + message);
        }catch(Exception e){
            chatWindow.append("\n Message not sent, some error occurred!");
        }
    }

    private void showMessage(final String string){
        SwingUtilities.invokeLater(() -> chatWindow.append(string));
    }

} // End Client_SetUp
