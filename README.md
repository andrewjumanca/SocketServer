# SocketServer
QOTD

To run this server, clone the repository and navigate to within the SocketServer folder.
Open a terminal and run:

javac TCPUDP.java
java TCPUDP

To connect to the server and send TCP requests, use the TCPClient.

javac TCPClient.java
java TCPClient localhost 17

To send datagram packets via UDP, use the UDPClient.

javac UDPClient.java
java UDPClient localhost 17
