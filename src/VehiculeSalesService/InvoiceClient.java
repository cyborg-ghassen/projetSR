package VehiculeSalesService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InvoiceClient {

    public static void main(String[] args) {
        try {
            String host = (args.length < 1) ? null : args[0];
            Registry registry = LocateRegistry.getRegistry(host);
            VehicleSalesService stub = (VehicleSalesService) registry.lookup("InvoiceService");
            double totalAmount = stub.getTotalAmount();
            System.out.println("Total amount from invoices: " + totalAmount);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
