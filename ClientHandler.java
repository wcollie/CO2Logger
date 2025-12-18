import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

//class that handles communication with a client
public class ClientHandler extends Thread {

    private final Socket socket; //socket represents connection between server and client


    public ClientHandler(Socket socket) { //assigns socket for client
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
            BufferedReader in = new BufferedReader( //input stream to recieve messages from client
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            out.println("Welcome to the CO2 logging server"); //welcome message for user

            //userID validation
            String userID;
            while (true) {
                out.println("Enter User ID:"); //tells user to enter user ID
                userID = in.readLine();

                if (userID != null && !userID.trim().isEmpty()) {
                    break;
                }
                out.println("Invalid User ID. Please try again."); //validation message for user
            }

            //postcode validation
            String postcode;
            while (true) {
                out.println("Enter Postcode (5-7 characters, spaces allowed):"); //tells user to enter postcode 5-7 and spaces are alllowed
                postcode = in.readLine();

                if (postcode == null) {
                    continue;
                }

                //remove spaces and to uppercase for uniformity
                postcode = postcode.replaceAll("\\s+", "").toUpperCase();

                //check for length and only letters and numbers
                if (postcode.length() >= 5 && postcode.length() <= 7 && postcode.matches("[A-Z0-9]+")) {
                break;
                }
                out.println("Invalid postcode. Must be 5-7 characters."); //tells user that inputted value is invalid
            }

            //validation for CO2
            double co2ppm;
            while (true) {
                out.println("Enter CO2 concentration (ppm):");
                try {
                    co2ppm = Double.parseDouble(in.readLine());
                    if (co2ppm > 0) {
                        break;
                    }
                } catch (NumberFormatException ignored) {}

                out.println("Invalid CO2 value. Please enter a positive number.");
            }

            //save data
            DataRecord record = new DataRecord(
                LocalDateTime.now(), userID, postcode, co2ppm
            );

            CSVLogger.writeRecord(record); //write in data

            out.println("Data recorded successfully. Thank you, goodbye.");

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            CO2Server.clientDisconnected();
        }
    }
}
