package server;

import common.dao.InvoiceDAO;
import common.dao.ProductDAO;
import common.tables.*;
import common.IStockService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

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
        ProductDAO dao = new ProductDAO();
        List<Product> allProducts = dao.getAllProducts();
        return allProducts.stream()
                .filter(p -> p.getId_family() == familyId && p.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public boolean purchaseProduct(int productId, int quantity) throws RemoteException {
        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductById(productId);

        if (product == null || product.getQuantity() < quantity) {
            return false; // Produit inexistant ou stock insuffisant
        }

        // Mise à jour du stock
        int newQuantity = product.getQuantity() - quantity;
        return dao.updateProductQuantity(productId, newQuantity);
    }

    @Override
    public boolean payInvoice(int invoiceId) throws RemoteException {
        InvoiceDAO dao = new InvoiceDAO();
        Invoice invoice = dao.getInvoiceById(invoiceId);

        if (invoice == null || invoice.isPaid()) {
            return false;
        }

        return dao.updateInvoicePaymentStatus(invoiceId, true);
    }

    @Override
    public Invoice getInvoiceById(int invoiceId) throws RemoteException {
        InvoiceDAO dao = new InvoiceDAO();
        return dao.getInvoiceById(invoiceId);
    }

    @Override
    public double calculateRevenue(String date) throws RemoteException {
        return 0;
    }

    @Override
    public boolean addStock(int productId, int quantity) throws RemoteException {
        if (quantity <= 0) {
            return false;
        }

        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductById(productId);

        if (product == null) {
            return false;
        }

        int newQuantity = product.getQuantity() + quantity;
        return dao.updateProductQuantity(productId, newQuantity);
    }

    @Override
    public boolean updateProductPrice(int productId, float newPrice) throws RemoteException {
        if (newPrice <= 0) {
            return false;
        }

        ProductDAO dao = new ProductDAO();
        return dao.updateProductPrice(productId, newPrice);
    }

    @Override
    public boolean saveInvoicesToServer() throws RemoteException {
        InvoiceDAO dao = new InvoiceDAO();
        return dao.saveAllInvoices();
    }
}
