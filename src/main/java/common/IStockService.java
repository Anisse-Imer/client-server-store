package common;

import common.tables.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

// Remote Interface defining available methods
public interface IStockService extends Remote {
    boolean createProduct(Product product) throws RemoteException;
    boolean createFamily(Family family) throws RemoteException;

    // Consultation du stock d'un article
    Product getProductById(int id) throws RemoteException;

    // Recherche d'articles par famille
    List<Product> getProductsByFamily(int familyId) throws RemoteException;

    List<Product> getAllProducts() throws RemoteException;

    boolean updateProduct(Product product) throws RemoteException;

    // Achat d'un article
    boolean purchaseProduct(int productId, int quantity) throws RemoteException;

    // Paiement d'une facture
    boolean payInvoice(int invoiceId) throws RemoteException;

    // Consultation d'une facture
    Invoice getInvoiceById(int invoiceId) throws RemoteException;

    // Calcul du chiffre d'affaires à une date donnée
    double calculateRevenue(String date) throws RemoteException;

    // Ajout d'un produit en stock
    boolean addStock(int productId, int quantity) throws RemoteException;

    // Mise à jour des prix
    boolean updateProductPrice(int productId, float newPrice) throws RemoteException;

    // Sauvegarde des factures
    boolean saveInvoicesToServer() throws RemoteException;

    // Sauvegarde des factures
    boolean saveInvoice(float totalAmount) throws RemoteException;
}