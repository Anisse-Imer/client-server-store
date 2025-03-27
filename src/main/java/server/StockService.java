package server;

import common.tables.*;
import common.IStockService;

import java.rmi.RemoteException;
import java.util.List;

// implémenter l'interface créée à l'étape 1
public class StockService implements IStockService {
    @Override
    public Product getProductById(int id) throws RemoteException {
        return new Product(0, 0, "", 4.54, 0);
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