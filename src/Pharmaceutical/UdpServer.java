package Pharmaceutical;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static void main(String[] args) {
        int port = 1234; // Replace with your port number
        byte[] buffer = new byte[1024]; // Adjust the size based on expected data

        try (DatagramSocket socket = new DatagramSocket(port)) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet); // Receive packet from client
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + receivedData);

                // Process data here
                try {
                    double processedData = Double.parseDouble(receivedData);
                    // Continue processing the numeric data
                    System.out.println("Processed data: " + processedData);
                } catch (NumberFormatException e) {
                    // Handle the case where the received data is not a valid double
                    System.err.println("Error: Received data is not a valid double");
                    e.printStackTrace();
                }

                // Optionally send a response back to the client
                String responseData = "Processed data";
                byte[] responseBuffer = responseData.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                        responseBuffer,
                        responseBuffer.length,
                        packet.getAddress(),
                        packet.getPort()
                );
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}