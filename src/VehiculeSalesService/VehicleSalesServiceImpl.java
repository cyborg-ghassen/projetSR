package VehiculeSalesService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class VehicleSalesServiceImpl extends UnicastRemoteObject implements VehicleSalesService {

    public VehicleSalesServiceImpl() throws RemoteException {
    }

    public double getTotalAmount() throws RemoteException {
        double totalAmount = 0;
        String dbUrl = "jdbc:mysql://localhost:3306/mysql";
        String dbUser = "vehiculesales";
        String dbPassword = "root";

        String query = "SELECT SUM(AmountPaid) as Total FROM invoices";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                totalAmount = rs.getDouble("Total");
                System.out.println("Total amount calculated on the server: " + totalAmount); // Affichage sur le serveur
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
        return totalAmount;
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started.");

            String name = "InvoiceService";
            VehicleSalesServiceImpl service = new VehicleSalesServiceImpl();
            java.rmi.Naming.rebind(name, service);
            System.out.println(name + " server is ready.");
        } catch (Exception e) {
            System.err.println("InvoiceService exception:");
            e.printStackTrace();
        }
    }
}