import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InvoiceCalculatorClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            InvoiceCalculator stub = (InvoiceCalculator) registry.lookup("InvoiceCalculator");
            double total = stub.calculateTotalInvoices();
            System.out.println("Total invoices sum: " + total);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}