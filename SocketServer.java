import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class SocketServer {
    static ExecutorService executor = Executors.newFixedThreadPool(5);;

    public static void handleRequest(Socket socket) {
        executor = Executors.newFixedThreadPool(5);

        try {
            System.out.println("INCOMING CLIENT REQUEST: ");
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            int x = 0;
            while ((x = input.read()) != '\n') {
                System.out.println((char)x);
                output.write((char)x);
            }
            System.out.println();
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) throws Exception {
        ServerSocket server = new ServerSocket(80);
        Socket socket = null;
        while ((socket = server.accept()) != null) {
            System.out.println("Accepted client request");
            final Socket threadSocket = socket;
            // new Thread(() -> handleRequest(threadSocket)).start();
            executor.submit(() -> handleRequest(threadSocket));
        }
        server.close();
    }
}