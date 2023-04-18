import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPUDP {
	public static void main(String[] args) {
		// TCP
		new Thread(new Runnable() {
			public void run() {
				ExecutorService executor = null;
				try (ServerSocket server = new ServerSocket(17)) {
					executor = Executors.newFixedThreadPool(5);
					System.out.println("Listening on TCP port 17");

					while (true) {
						final Socket socket = server.accept();
						executor.execute(new Runnable() {
                        public void run() {
                            String input = "";
                            // System.out.println(socket.toString() + " ~> connected");

                            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                                // while (true) {
                                    
                                    try {
                                        OutputStream output = socket.getOutputStream();
                                        PrintWriter writer = new PrintWriter(output, true);
                                        // Echo server...
                                        while (true) {
                                            writer.println(getRandomQuote());
                                            Thread.sleep(10000);
                                            System.out.println("TCP response sent to client.");
                                        }
                                    } catch (InterruptedException ie) {
                                        System.err.println(ie);
                                    }
                                    
                                    

                                // }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } finally {
                                try {
                                    System.err.println(socket.toString()
                                            + " ~> closing");
                                    socket.close();
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            }
                        }
						});
					}
				} catch (IOException ioe) {
					System.err.println("Cannot open the port on TCP");
					ioe.printStackTrace();
				} finally {
					System.out.println("Closing TCP server");
					if (executor != null) {
						executor.shutdown();
					}
				}
			}
		}).start();

		// UDP
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (DatagramSocket socket = new DatagramSocket(17)) {
					byte[] buf = new byte[socket.getReceiveBufferSize()];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);

					System.out.println("Listening on UDP port 17");
					while (true) {
						socket.receive(packet);
                        System.out.println("Datagram response sent to client");
                        // QOTD Server
                        InetAddress clientAddress = packet.getAddress();
                        int clientPort = packet.getPort();

                        byte[] buffer = getRandomQuote().getBytes();
                        DatagramPacket response = new DatagramPacket(buffer, 0, buffer.length, clientAddress, clientPort);
                        
						socket.send(response);
					}
				} catch (IOException ioe) {
					System.err.println("Cannot open the port on UDP");
					ioe.printStackTrace();
				} finally {
					System.out.println("Closing UDP server");
				}
			}
		}).start();
	}
 
    private static String getRandomQuote() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("quotes.txt"));
        Random random = new Random();
        String randomQuote = lines.get(random.nextInt(lines.size()));
        return randomQuote;
    }
}

