import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Server Class
 * Server Socket running at port 2020
 */

public class Server {

    static Vector<Client_Handler> sockets = new Vector<>();

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(2020);
        int i = 0;
        System.out.println("Server is waiting for Connections!");
        while(true){
            System.out.println("Current Client Count -> " + sockets.size());
            Socket connection = socket.accept();
            String clientName = "Client - " + i;
            PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Client_Handler clientHandler = new Client_Handler(clientName, connection, output, input);
            sockets.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();
            ++i;
        }
    }
}

class Client_Handler implements Runnable{

    Socket connection;
    PrintWriter output;
    BufferedReader input;
    String clientName;

    public Client_Handler(String clientName, Socket connection, PrintWriter output, BufferedReader input) {
        this.connection = connection;
        this.output = output;
        this.input = input;
        this.clientName = clientName;
    }

    String receiveMessage() throws IOException{
        return input.readLine();
    }

    void sendMessage(String message){
        output.println(message);
    }

    void closeStreams() throws IOException{
        output.close();
        input.close();
        connection.close();
        Server.sockets.remove(this);
        System.out.println("Current Client Count - " + Server.sockets.size());
    }

    @Override
    public void run() {
        try{
            while(true){
                String message = receiveMessage();
                if (message.endsWith("Client_wants_to_end_the_connection")) {
                    message = message.substring(0, message.indexOf('-')) + " has left the Chat!";
                    for (Client_Handler clientHandler: Server.sockets){
                        if (!clientHandler.clientName.equals(clientName)) {clientHandler.sendMessage(message);}
                    }
                    closeStreams();
                    break;
                }
                for (Client_Handler clientHandler: Server.sockets) {
                    if (!clientHandler.clientName.equals(clientName)) {clientHandler.sendMessage(message);}
                }
            }
        }catch(Exception e){
            System.out.println("Error: run()");
            e.printStackTrace();
        }
    }
}

