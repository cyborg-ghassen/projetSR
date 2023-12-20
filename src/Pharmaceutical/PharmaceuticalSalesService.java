package Pharmaceutical;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PharmaceuticalSalesService extends Remote {
    String getParapharmaceuticalInvoices() throws RemoteException;
}