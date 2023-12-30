import VehiculeSalesService.VehicleSalesService;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class InvoiceCalculatorServer extends UnicastRemoteObject implements InvoiceCalculator {

    protected InvoiceCalculatorServer() throws RemoteException {
        super();
    }

    public double calculateTotalInvoices() throws SocketException {
        double udpSum = getUdpServerSum();
        double tcpSum = getTcpServerSum();
        return udpSum + tcpSum;
    }

    // Méthode pour obtenir la somme du serveur UDP
    public double getUdpServerSum() {
        String serverHostname = "localhost";
        int port = 1234;
        String filePath = "D:\\GLSI3\\projet SR\\src\\Pharmaceutical\\PharmaceuticalSalesInvoices.csv";
        double totalAmount = 0;
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(serverHostname);
            File csvFile = new File(filePath);
            try (Scanner scanner = new Scanner(csvFile)) {
                scanner.nextLine();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    totalAmount += Double.parseDouble(values[2]);
                }
            } catch (FileNotFoundException e) {
                System.err.println("CSV file not found: " + e.getMessage());
            }
            String message = "Total amount of invoices: " + totalAmount;
            byte[] buffer = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
            System.out.println("Sent total amount to the server: " + totalAmount);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String receivedResponse = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server response: " + receivedResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalAmount;
    }

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

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1100);
            InvoiceCalculatorServer obj = new InvoiceCalculatorServer();
            registry.rebind("InvoiceCalculator", obj);
            System.out.println("Proxy Server ready.");
        } catch (Exception e) {
            System.err.println("Proxy Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}