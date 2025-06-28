package server.main;

import common.IShopService;
import common.dao.mainshop.ShopDAO;
import common.dao.mainshop.CProductDAO;
import common.tables.Invoice;
import common.tables.InvoiceDetail;
import common.tables.mainshop.CProduct;
import common.tables.mainshop.Shop;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ShopService extends UnicastRemoteObject implements IShopService {

    private CProductDAO productDAO;
    private ShopDAO shopDAO;

    protected ShopService() throws RemoteException {
        super();
        this.shopDAO = new ShopDAO();
        this.productDAO = new CProductDAO();
    }

    protected ShopService(int port) throws RemoteException {
        super(port);
        this.shopDAO = new ShopDAO();
        this.productDAO = new CProductDAO();
    }

    protected ShopService(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        this.shopDAO = new ShopDAO();
        this.productDAO = new CProductDAO();
    }

    @Override
    public boolean insertShop(Shop shop) {
        try {
            // Validation
            if (shop == null) {
                System.out.println("Erreur: Shop est null");
                return false;
            }

            if (shop.getShopName() == null || shop.getShopName().trim().isEmpty()) {
                System.out.println("Erreur: Nom du shop ne peut pas être vide");
                return false;
            }

            // Check if shop already exists
            if (shopDAO.shopExists(shop.getShopName().trim())) {
                System.out.println("Erreur: Un shop avec ce nom existe déjà: " + shop.getShopName());
                return false;
            }

            // Insert shop
            Long generatedId = shopDAO.addShop(shop);

            if (generatedId != null && generatedId > 0) {
                System.out.println("Shop inséré avec succès. ID: " + generatedId);
                return true;
            } else {
                System.out.println("Erreur lors de l'insertion du shop");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'insertion du shop: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertInvoice(Shop shop, Invoice invoice, List<InvoiceDetail> invoiceDetails) {
        return false;
    }

    @Override
    public List<CProduct> getAllProducts() {
        try {
            List<CProduct> products = productDAO.getAllProducts();

            if (products != null && !products.isEmpty()) {
                System.out.println("Successfully retrieved " + products.size() + " products");
                return products;
            } else {
                System.out.println("No products found in database");
                return new ArrayList<>(); // Return empty list instead of null
            }

        } catch (Exception e) {
            System.err.println("Error retrieving all products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list on error instead of null
        }
    }
}