import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//server for handling CO2 data from clients
public class CO2Server {

    private static final int MAX_CLIENTS = 4; //max 4 clients
    private static int clientCount = 0; //current number of clients using the server at one time
    public static synchronized void clientDisconnected() {
        clientCount--; //if a client disconnects decrease the client count
    }
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java CO2Server <port>");
            return; //exit program
        }

        int port = Integer.parseInt(args[0]);
        System.out.println("CO2 Server starting on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            //loop to accept more clients
            while (true) {
                Socket clientSocket = serverSocket.accept();

                synchronized (CO2Server.class) {
                    if (clientCount >= MAX_CLIENTS) {
                        clientSocket.close();
                        continue;
                    }
                    clientCount++; //increment client count for each new connection
                }

                //create new thread for client
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage()); //tells user of server error with the message
        }
    }
}
