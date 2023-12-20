package Pharmaceutical;

import Pharmaceutical.PharmaceuticalSalesService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PharmaceuticalSalesServiceImpl extends UnicastRemoteObject implements PharmaceuticalSalesService {
    public PharmaceuticalSalesServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String getParapharmaceuticalInvoices() throws RemoteException {
        return null;
    }
}