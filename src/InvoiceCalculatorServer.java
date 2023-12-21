import VehiculeSalesService.VehicleSalesService;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

// Implémentation du serveur RMI
public class InvoiceCalculatorServer extends UnicastRemoteObject implements InvoiceCalculator {

    protected InvoiceCalculatorServer() throws RemoteException {
        super();
    }

    // Implémentation de la méthode distante pour calculer le total des factures
    public double calculateTotalInvoices() throws SocketException {
        double udpSum = getUdpServerSum(); // Communication avec le serveur UDP
        double tcpSum = getTcpServerSum(); // Communication avec le serveur TCP
        return udpSum + tcpSum;
    }

    // Méthode pour obtenir la somme du serveur UDP
    public double getUdpServerSum() {
        String serverHostname = "localhost"; // Replace with the hostname or IP of SI Entr.1
        int port = 1234; // Port number should match the server's
        String filePath = "D:\\GLSI3\\projet SR\\src\\Pharmaceutical\\PharmaceuticalSalesInvoices.csv";
        double totalAmount = 0;
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverHostname);
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

        return totalAmount;
    }

    // Méthode pour obtenir la somme du serveur TCP
    private double getTcpServerSum() {
        double totalAmount = 0;
        try {
            String host = "localhost";
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            VehicleSalesService stub = (VehicleSalesService) registry.lookup("InvoiceService");
            totalAmount = stub.getTotalAmount();
            System.out.println("Total amount from invoices: " + totalAmount);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return totalAmount;
    }

    // Point d'entrée principal pour le serveur RMI
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1100); // Créer le registre RMI sur le port 1100
            InvoiceCalculatorServer obj = new InvoiceCalculatorServer();
            registry.rebind("InvoiceCalculator", obj); // Lier l'objet distant
            System.out.println("Proxy Server ready.");
        } catch (Exception e) {
            System.err.println("Proxy Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}