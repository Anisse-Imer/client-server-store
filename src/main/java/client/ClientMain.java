package client;

import common.IStockService;
import common.tables.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            IStockService service = (IStockService) registry.lookup("StockService");
            Product response = service.getProductById(2);
            System.out.println("Server response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
