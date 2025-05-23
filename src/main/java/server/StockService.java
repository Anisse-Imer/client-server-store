package server;

import common.dao.FamilyDAO;
import common.dao.InvoiceDAO;
import common.dao.InvoiceDetailDAO;
import common.dao.ProductDAO;
import common.tables.*;
import common.IStockService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

// Étendre UnicastRemoteObject pour en faire un objet distant
public class StockService extends UnicastRemoteObject implements IStockService {

    // Map pour stocker les factures en mémoire (pour la démo)
    private Map<Integer, Invoice> invoices = new HashMap<>();

    // Constructeur requis pour UnicastRemoteObject
    public StockService() throws RemoteException {
        super(); // Exportation automatique de l'objet
    }

    @Override
    public boolean createProduct(Product product) throws RemoteException {
        ProductDAO dao = new ProductDAO();
        return dao.addProduct(product);
    }

    @Override
    public boolean createFamily(Family family) throws RemoteException {
        FamilyDAO dao = new FamilyDAO();
        return dao.addFamily(family);
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
    public List<Product> getAllProducts() throws RemoteException {
        ProductDAO dao = new ProductDAO();
        return dao.getAllProducts();
    }

    @Override
    public boolean updateProduct(Product product) throws RemoteException {
        ProductDAO dao = new ProductDAO();
        return dao.updateProduct(product);
    }


    // Méthodes auxiliaires nécessaires

    private int generateOrderId() {
        // Génère un ID unique basé sur le timestamp et un nombre aléatoire
        return (int) (System.currentTimeMillis() % 1000000) + new Random().nextInt(1000);
    }

    private Product findProductByName(String productName) throws RemoteException {
        // Recherche un produit par son nom exact
        List<Product> allProducts = getAllProducts();

        for (Product product : allProducts) {
            if (product.getName().equalsIgnoreCase(productName.trim())) {
                return product;
            }
        }
        return null;
    }

    private boolean saveInvoice(Invoice invoice) throws RemoteException {
        try {
            // Sauvegarder la facture dans la base de données
            // Implémentation dépendante de votre système de persistance

            // Exemple avec une base de données:
            /*
            String sql = "INSERT INTO invoices (id, date, total_amount, details, paid) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, invoice.getId());
            stmt.setTimestamp(2, new Timestamp(invoice.getDate().getTime()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setString(4, invoice.getDetails());
            stmt.setBoolean(5, invoice.isPaid());

            return stmt.executeUpdate() > 0;
            */

            // Pour la démo, nous ajoutons la facture à une collection en mémoire
            invoices.put(invoice.getId(), invoice);
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde de la facture: " + e.getMessage());
            return false;
        }
    }

    private void saveOrderToFile(int orderId, String date, List<String> lines, double totalAmount) {
        try {
            // Créer le fichier de log s'il n'existe pas
            File ordersFile = new File("client_orders.log");

            try (FileWriter writer = new FileWriter(ordersFile, true)) {
                writer.write("====== NOUVELLE COMMANDE ======\n");
                writer.write("ID Commande: " + orderId + "\n");
                writer.write("Date: " + date + "\n");
                writer.write("Articles:\n");

                for (String line : lines) {
                    writer.write("  - " + line + "\n");
                }

                writer.write("Total: €" + String.format("%.2f", totalAmount) + "\n");
                writer.write("===============================\n\n");
                writer.flush();
            }

            System.out.println("Commande sauvegardée dans le fichier: " + ordersFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier de commandes: " + e.getMessage());
        }
    }

    private void rollbackStockChanges(List<String> lines) throws RemoteException {
        // Restaure les quantités de stock en cas d'erreur
        System.out.println("Tentative de restauration des stocks...");

        for (String line : lines) {
            try {
                String[] parts = line.split(" x");
                if (parts.length != 2) continue;

                String productName = parts[0].trim();
                String[] quantityAndPrice = parts[1].split(" - €");
                if (quantityAndPrice.length != 2) continue;

                int quantity = Integer.parseInt(quantityAndPrice[0].trim());

                Product product = findProductByName(productName);
                if (product != null) {
                    product.setQuantity(product.getQuantity() + quantity);
                    updateProduct(product);
                    System.out.println("Stock restauré pour: " + productName + " (+" + quantity + ")");
                }

            } catch (Exception e) {
                System.err.println("Erreur lors de la restauration du stock pour: " + line);
            }
        }
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

    @Override
    public Long saveInvoice(float totalAmount) throws RemoteException {
        String payment_method = "Card";
        boolean paid = true;
        InvoiceDAO dao = new InvoiceDAO();
        Invoice invoice = new Invoice(totalAmount,payment_method,LocalDateTime.now(),paid);
        return  dao.addInvoice(invoice);

    }

    @Override
    public Long saveInvoiceDetails(int productId, Long invoiceId, float price, int quantity) throws RemoteException {
    try {
        //        Invoice
        InvoiceDetail invoiceDetail = new InvoiceDetail(productId, invoiceId, price, quantity);
        InvoiceDetailDAO dao = new InvoiceDetailDAO();
        return dao.addInvoiceDetail(invoiceDetail);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }

}