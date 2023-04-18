import java.io.*;
import java.net.*;
import java.util.logging.*;

public class TCPClient {
    private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

    public static void main(String args[]) {
        String host = args[0];
        int port = Integer.valueOf(args[1]);

        logger.info("Connecting to " + host + ":" + port);
        try (Socket socket = new Socket(host, port)) {
            // Create a TCP socket and connect to the server

            // Wait for a response from the server
            InputStream inputStream = socket.getInputStream();
            byte[] responseBytes = new byte[1024];
            int bytesRead = inputStream.read(responseBytes);
            String response = new String(responseBytes, 0, bytesRead);
            System.out.println("Received response from server: " + response);

            System.out.println("Press enter to disconnect from server.");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();
            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception occurred", ex);
        }
    }
}

