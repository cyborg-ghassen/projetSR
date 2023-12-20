import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final int udpPortSIEntr1;
    private final String hostSIEntr2;
    private final int tcpPortSIEntr2;

    public ClientHandler(Socket socket, int udpPortSIEntr1, String hostSIEntr2, int tcpPortSIEntr2) {
        this.clientSocket = socket;
        this.udpPortSIEntr1 = udpPortSIEntr1;
        this.hostSIEntr2 = hostSIEntr2;
        this.tcpPortSIEntr2 = tcpPortSIEntr2;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                DatagramSocket udpSocket = new DatagramSocket()
        ) {
            String request = in.readLine(); // Read the request from the client
            double totalRevenue = 0.0;

            if ("GET_PARAPHARMACEUTICAL_SALES_INVOICES".equals(request)) {
                // Prepare and send a UDP packet to SI Entr.1
                InetAddress address = InetAddress.getByName("localhost"); // Replace with actual address
                byte[] buf = new byte[256]; // Adjust buffer size as needed
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, udpPortSIEntr1);
                udpSocket.send(packet);

                // Receive the response from SI Entr.1
                packet = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                totalRevenue += parseRevenue(received); // Implement this method to parse revenue
            }

            if ("GET_VEHICLE_SALES_INVOICES".equals(request)) {
                // Connect to SI Entr.2's web service via TCP and get vehicle sales invoices
                try (Socket tcpSocket = new Socket(hostSIEntr2, tcpPortSIEntr2);
                     BufferedReader tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                     PrintWriter tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true)
                ) {
                    tcpOut.println(request); // Send request to SI Entr.2
                    String response = tcpIn.readLine(); // Read the response from SI Entr.2
                    totalRevenue += parseRevenue(response); // Implement this method to parse revenue
                }
            }

            // Send the total revenue back to the client
            out.println("Total Revenue: " + totalRevenue);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Ensure the client socket is closed
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double parseRevenue(String data) {
        // Implement parsing logic based on the data format from SI Entr.1 and SI Entr.2
        return 0.0; // This is a placeholder
    }
}
