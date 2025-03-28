package server;

import common.dao.ProductDAO;
import common.tables.*;
import common.IStockService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

// Étendre UnicastRemoteObject pour en faire un objet distant
public class StockService extends UnicastRemoteObject implements IStockService {

    // Constructeur requis pour UnicastRemoteObject
    protected StockService() throws RemoteException {
        super(); // Exportation automatique de l'objet
    }

    @Override
    public Product getProductById(int id) throws RemoteException {
        ProductDAO dao = new ProductDAO();
        return dao.getProductById(id);
    }

    @Override
    public List<Product> getProductsByFamily(int familyId) throws RemoteException {
        return List.of();
    }

    @Override
    public boolean purchaseProduct(int productId, int quantity) throws RemoteException {
        return false;
    }

    @Override
    public boolean payInvoice(int invoiceId) throws RemoteException {
        return false;
    }

    @Override
    public Invoice getInvoiceById(int invoiceId) throws RemoteException {
        return null;
    }

    @Override
    public double calculateRevenue(String date) throws RemoteException {
        return 0;
    }

    @Override
    public boolean addStock(int productId, int quantity) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateProductPrice(int productId, float newPrice) throws RemoteException {
        return false;
    }

    @Override
    public boolean saveInvoicesToServer() throws RemoteException {
        return false;
    }
}
