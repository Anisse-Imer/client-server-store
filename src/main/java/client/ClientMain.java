package client;

import common.IStockService;
import common.tables.Product;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) {
        try {
            // Connexion au registre RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Recherche du service
            IStockService service = (IStockService) registry.lookup("StockService");

            // Appel d'une méthode distante
            Product product = service.getProductById(1);
            System.out.println("Produit récupéré : " + product);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}