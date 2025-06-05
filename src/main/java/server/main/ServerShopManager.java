package server.main;

import common.IShopService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.main.DbManager;

public class ServerShopManager {
    public static void main(String[] args) {
        try {
            // Création de l'objet distant
            IShopService service = new ShopService();

            // Création du registre RMI (si ce n’est pas déjà fait)
            Registry registry = LocateRegistry.createRegistry(1098);

            // Enregistrement du service sous le nom "StockService"
            registry.rebind("ShopService", service);

            // Init database components\
            DbManager.init();

            System.out.println("Serveur RMI-ServerShopManager démarré...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
