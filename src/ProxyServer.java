import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {
    public static void main(String[] args) throws Exception {
        int tcpPort = 1100; // TCP port for client connections
        int udpPort = 1234; // UDP port for SI Entr.1 communication

        // Start TCP server for client connections
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            System.out.println("Proxy Server is listening on TCP port " + tcpPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, udpPort, (args.length < 1) ? null : args[0], 1099)).start();
            }
        }
    }
}