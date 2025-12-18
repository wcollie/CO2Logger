import java.io.*; //input/output streams
import java.net.Socket;
import java.util.Scanner; //to read user input from the console

public class CO2Client {

    public static void main(String[] args) {

        //checks command line for server ip and port
        if (args.length != 2) {
            System.out.println("Usage: java CO2Client <server_ip> <port>");
            return;
        }

        String serverIP = args[0]; //server ip address
        int port = Integer.parseInt(args[1]); //server port number

        try (
            Socket socket = new Socket(serverIP, port); //connect to the server at given ip and port
            BufferedReader in = new BufferedReader( 
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter( //semd message to the server
                socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in) //scanner to read user input
        ) {

            String message;
            //loop to read messages from server
            while ((message = in.readLine()) != null) {
                System.out.println(message); //print server messages
                if (message.endsWith(":")) { //messages ending in : wait for user input
                    out.println(scanner.nextLine()); //read line and send to server
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage()); //print the client error with the error message
        }
    }
}
