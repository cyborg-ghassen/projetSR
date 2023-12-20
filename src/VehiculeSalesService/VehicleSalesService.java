package VehiculeSalesService;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VehicleSalesService extends Remote {
    double getTotalAmount() throws RemoteException;
}