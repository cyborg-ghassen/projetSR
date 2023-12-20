package Pharmaceutical;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClient {
    public static void main(String[] args) {
        String serverHostname = "localhost"; // Replace with the hostname or IP of SI Entr.1
        int port = 1234; // Port number should match the server's
        String filePath = "D:\\GLSI3\\projet SR\\src\\Pharmaceutical\\PharmaceuticalSalesInvoices.csv";

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverHostname);
            double totalAmount = 0.0;
            File csvFile = new File(filePath);
            try (Scanner scanner = new Scanner(csvFile)) {
                scanner.nextLine(); // Skip the header line

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    totalAmount += Double.parseDouble(values[2]); // Assuming the amount is in the 3rd column
                }
            } catch (FileNotFoundException e) {
                System.err.println("CSV file not found: " + e.getMessage());
                return; // Exit if the file is not found
            }
            String message = "Total amount of invoices: " + totalAmount;
            byte[] buffer = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet); // Send packet to server
            System.out.println("Sent total amount to the server: " + totalAmount);

            // Optionally wait for a response from the server
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String receivedResponse = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server response: " + receivedResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
