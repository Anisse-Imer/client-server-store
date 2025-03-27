package server;

import server.StockService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            StockService service = new StockService();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("StockService", service);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
