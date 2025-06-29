package server;

import common.IStockService;
import common.tables.mainshop.Shop;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Création de l'objet distant
            IStockService service = new StockService();

            // Création du registre RMI (si ce n’est pas déjà fait)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Enregistrement du service sous le nom "StockService"
            registry.rebind("StockService", service);

            // Init database components\
            server.DbManager.init();

            // Init batch
            System.out.println("Update");
            service.updateDataBaseFromMain(new Shop(3, "Shop n°3 - Samsonite"));

            System.out.println("Serveur RMI démarré...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
