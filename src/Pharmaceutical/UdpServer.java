package Pharmaceutical;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static void main(String[] args) {
        int port = 1234;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + receivedData);

                try {
                    double processedData = Double.parseDouble(receivedData);
                    System.out.println("Processed data: " + processedData);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Received data is not a valid double");
                    e.printStackTrace();
                }

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