import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InvoiceCalculator extends Remote {
    double calculateTotalInvoices() throws RemoteException, SocketException;
}