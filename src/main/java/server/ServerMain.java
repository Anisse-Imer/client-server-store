package server;

import common.IStockService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.DbManager;

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
            DbManager.init();

            System.out.println("Serveur RMI démarré...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
