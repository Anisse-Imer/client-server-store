package client.ui;

import common.IStockService;
import common.dao.InvoiceDAO;
import common.dao.InvoiceDetailDAO;
import common.tables.Family;
import common.tables.Invoice;
import common.tables.InvoiceDetail;
import common.tables.Product;
import org.w3c.dom.ls.LSOutput;
import server.StockService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockClientUI extends JFrame {
    private IStockService stockService;
    private JTabbedPane tabbedPane;
    private JPanel productsPanel;
    private JPanel invoicesPanel;
    private JPanel reportsPanel;
    private JPanel createProductPanel;
    private JPanel createFamilyPanel;

    // Components de l'onglet Produits
    private JTextField productIdField;
    private JTextField familyIdField;
    private JTable productsTable;
    private DefaultTableModel productsTableModel;
    private JTextField purchaseProductIdField;
    private JTextField purchaseQuantityField;
    private JTextField addStockProductIdField;
    private JTextField addStockQuantityField;
    private JTextField updatePriceProductIdField;
    private JTextField updatePriceField;

    // Components de l'onglet Créer Produit
    private JTextField newProductIdField;
    private JTextField newProductFamilyIdField;
    private JTextField newProductNameField;
    private JTextField newProductPriceField;
    private JTextField newProductQuantityField;

    // Components de l'onglet Créer Famille
    private JTextField newFamilyIdField;
    private JTextField newFamilyNameField;
    private JTextArea familyResultArea;

    // Components de l'onglet Factures
    private JTextField invoiceIdField;
    private JTextArea invoiceDetailsArea;
    private JTextField payInvoiceIdField;

    // Components de l'onglet Rapports
    private JTextField revenueDateField;
    private JLabel revenueResultLabel;
    private JButton saveInvoicesButton;
    private JPanel orderPanel;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTextField orderProductIdField;
    private JTextField orderQuantityField;
    private JTextArea orderSummaryArea;


    public StockClientUI() {
        initializeUI();
        connectToService();
    }

    private void connectToService() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            stockService = (IStockService) registry.lookup("StockService");
            JOptionPane.showMessageDialog(this, "Connexion au service de stock réussie !");
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion au service : " + e.getMessage(),
                    "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setTitle("Système de Gestion de Stock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        createProductsPanel();
        createInvoicesPanel();
        createReportsPanel();
        createNewProductPanel();
        createNewFamilyPanel();
        createOrderPanel();

        tabbedPane.addTab("Produits", productsPanel);
        tabbedPane.addTab("Créer Produit", createProductPanel);
        tabbedPane.addTab("Créer Famille", createFamilyPanel);
        tabbedPane.addTab("Factures", invoicesPanel);
        tabbedPane.addTab("Rapports", reportsPanel);
        tabbedPane.addTab("Commandes", orderPanel);

        add(tabbedPane);
    }

    private void createProductsPanel() {
        productsPanel = new JPanel(new BorderLayout(10, 10));
        productsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panneau supérieur pour la recherche de produits
        JPanel lookupPanel = new JPanel(new BorderLayout(10, 10));
        lookupPanel.setBorder(BorderFactory.createTitledBorder("Recherche de Produits"));

        JPanel lookupInputPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        lookupInputPanel.add(new JLabel("ID Produit :"));
        productIdField = new JTextField();
        lookupInputPanel.add(productIdField);
        JButton findProductButton = new JButton("Rechercher Produit");
        findProductButton.addActionListener(e -> findProductById());
        lookupInputPanel.add(findProductButton);

        lookupInputPanel.add(new JLabel("ID Famille :"));
        familyIdField = new JTextField();
        lookupInputPanel.add(familyIdField);
        JButton findByFamilyButton = new JButton("Rechercher par Famille");
        findByFamilyButton.addActionListener(e -> findProductsByFamily());
        lookupInputPanel.add(findByFamilyButton);

        lookupPanel.add(lookupInputPanel, BorderLayout.NORTH);

        // Tableau pour afficher les produits
        productsTableModel = new DefaultTableModel(
                new Object[]{"ID", "ID Famille", "Nom", "Prix", "Quantité"}, 0);
        productsTable = new JTable(productsTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        lookupPanel.add(tableScrollPane, BorderLayout.CENTER);

        productsPanel.add(lookupPanel, BorderLayout.CENTER);

        // Panneau inférieur pour les opérations sur les produits
        JPanel operationsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        operationsPanel.setBorder(BorderFactory.createTitledBorder("Opérations sur les Produits"));

        // Panneau d'achat de produit
        JPanel purchasePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        purchasePanel.add(new JLabel("ID Produit :"));
        purchaseProductIdField = new JTextField(5);
        purchasePanel.add(purchaseProductIdField);
        purchasePanel.add(new JLabel("Quantité :"));
        purchaseQuantityField = new JTextField(5);
        purchasePanel.add(purchaseQuantityField);
        JButton purchaseButton = new JButton("Acheter");
        purchaseButton.addActionListener(e -> purchaseProduct());
        purchasePanel.add(purchaseButton);
        operationsPanel.add(purchasePanel);

        // Panneau d'ajout de stock
        JPanel addStockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addStockPanel.add(new JLabel("ID Produit :"));
        addStockProductIdField = new JTextField(5);
        addStockPanel.add(addStockProductIdField);
        addStockPanel.add(new JLabel("Quantité :"));
        addStockQuantityField = new JTextField(5);
        addStockPanel.add(addStockQuantityField);
        JButton addStockButton = new JButton("Ajouter Stock");
        addStockButton.addActionListener(e -> addStock());
        addStockPanel.add(addStockButton);
        operationsPanel.add(addStockPanel);

        // Panneau de mise à jour du prix
        JPanel updatePricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePricePanel.add(new JLabel("ID Produit :"));
        updatePriceProductIdField = new JTextField(5);
        updatePricePanel.add(updatePriceProductIdField);
        updatePricePanel.add(new JLabel("Nouveau Prix :"));
        updatePriceField = new JTextField(5);
        updatePricePanel.add(updatePriceField);
        JButton updatePriceButton = new JButton("Mettre à jour Prix");
        updatePriceButton.addActionListener(e -> updateProductPrice());
        updatePricePanel.add(updatePriceButton);
        operationsPanel.add(updatePricePanel);

        productsPanel.add(operationsPanel, BorderLayout.SOUTH);
    }

    private void createNewProductPanel() {
        createProductPanel = new JPanel(new BorderLayout(10, 10));
        createProductPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Créer un Nouveau Produit"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Produit
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID Produit :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        newProductIdField = new JTextField(20);
        formPanel.add(newProductIdField, gbc);

        // ID Famille
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("ID Famille :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        newProductFamilyIdField = new JTextField(20);
        formPanel.add(newProductFamilyIdField, gbc);

        // Nom du Produit
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        newProductNameField = new JTextField(20);
        formPanel.add(newProductNameField, gbc);

        // Prix du Produit
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Prix :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        newProductPriceField = new JTextField(20);
        formPanel.add(newProductPriceField, gbc);

        // Quantité du Produit
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Quantité Initiale :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        newProductQuantityField = new JTextField(20);
        formPanel.add(newProductQuantityField, gbc);

        // Bouton Créer
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("Créer Produit");
        createButton.addActionListener(e -> createProduct());
        formPanel.add(createButton, gbc);

        // Ajouter le formulaire au panneau
        createProductPanel.add(formPanel, BorderLayout.NORTH);

        // Ajouter un panneau de résultats
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Résultats"));

        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        createProductPanel.add(resultPanel, BorderLayout.CENTER);
    }

    private void createNewFamilyPanel() {
        createFamilyPanel = new JPanel(new BorderLayout(10, 10));
        createFamilyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Créer une Nouvelle Famille"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID Famille
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID Famille :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        newFamilyIdField = new JTextField(20);
        formPanel.add(newFamilyIdField, gbc);

        // Nom de la Famille
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        newFamilyNameField = new JTextField(20);
        formPanel.add(newFamilyNameField, gbc);

        // Bouton Créer
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("Créer Famille");
        createButton.addActionListener(e -> createFamily());
        formPanel.add(createButton, gbc);

        // Ajouter le formulaire au panneau
        createFamilyPanel.add(formPanel, BorderLayout.NORTH);

        // Ajouter un panneau de résultats
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Résultats"));

        familyResultArea = new JTextArea(10, 30);
        familyResultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(familyResultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        createFamilyPanel.add(resultPanel, BorderLayout.CENTER);
    }

    private void createInvoicesPanel() {
        invoicesPanel = new JPanel(new BorderLayout(10, 10));
        invoicesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panneau de recherche de facture
        JPanel invoiceLookupPanel = new JPanel(new BorderLayout(10, 10));
        invoiceLookupPanel.setBorder(BorderFactory.createTitledBorder("Recherche de Facture"));

        JPanel lookupInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lookupInputPanel.add(new JLabel("ID Facture :"));
        invoiceIdField = new JTextField(10);
        lookupInputPanel.add(invoiceIdField);
        JButton findInvoiceButton = new JButton("Rechercher Facture");
        findInvoiceButton.addActionListener(e -> findInvoice());
        lookupInputPanel.add(findInvoiceButton);

        invoiceLookupPanel.add(lookupInputPanel, BorderLayout.NORTH);

        // Zone de détails de la facture
        invoiceDetailsArea = new JTextArea();
        invoiceDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(invoiceDetailsArea);
        invoiceLookupPanel.add(detailsScrollPane, BorderLayout.CENTER);

        invoicesPanel.add(invoiceLookupPanel, BorderLayout.CENTER);

        // Panneau de paiement de facture
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payer Facture"));
        paymentPanel.add(new JLabel("ID Facture :"));
        payInvoiceIdField = new JTextField(10);
        paymentPanel.add(payInvoiceIdField);
        JButton payInvoiceButton = new JButton("Payer Facture");
        payInvoiceButton.addActionListener(e -> payInvoice());
        paymentPanel.add(payInvoiceButton);

        invoicesPanel.add(paymentPanel, BorderLayout.SOUTH);
    }

    private void createReportsPanel() {
        reportsPanel = new JPanel(new BorderLayout(10, 10));
        reportsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panneau de calcul des revenus
        JPanel revenuePanel = new JPanel(new BorderLayout(10, 10));
        revenuePanel.setBorder(BorderFactory.createTitledBorder("Calcul des Revenus"));

        JPanel revenueInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        revenueInputPanel.add(new JLabel("Date (aaaa-MM-jj) :"));
        revenueDateField = new JTextField(10);
        revenueInputPanel.add(revenueDateField);
        JButton calculateButton = new JButton("Calculer Revenus");
        calculateButton.addActionListener(e -> calculateRevenue());
        revenueInputPanel.add(calculateButton);

        revenuePanel.add(revenueInputPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultPanel.add(new JLabel("Total des Revenus : "));
        revenueResultLabel = new JLabel("€0,00");
        resultPanel.add(revenueResultLabel);

        revenuePanel.add(resultPanel, BorderLayout.CENTER);

        reportsPanel.add(revenuePanel, BorderLayout.NORTH);

        // Panneau de sauvegarde des factures
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.setBorder(BorderFactory.createTitledBorder("Sauvegarder Factures"));
        saveInvoicesButton = new JButton("Sauvegarder Factures sur le Serveur");
        saveInvoicesButton.addActionListener(e -> saveInvoices());
        savePanel.add(saveInvoicesButton);

        reportsPanel.add(savePanel, BorderLayout.CENTER);
    }

    // Méthode Créer Famille
    private void createFamily() {
        try {
            // Valider les entrées
            if (newFamilyIdField.getText().trim().isEmpty() ||
                    newFamilyNameField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(newFamilyIdField.getText().trim());
            String name = newFamilyNameField.getText().trim();

            // Créer l'objet famille
            Family family = new Family(id, name);

            // Appeler la méthode distante
            boolean success = stockService.createFamily(family);

            if (success) {
                JOptionPane.showMessageDialog(this, "Famille créée avec succès !");
                // Afficher la famille créée
                familyResultArea.setText("Famille créée :\n" +
                        "ID : " + id + "\n" +
                        "Nom : " + name);
                // Vider le formulaire
                newFamilyIdField.setText("");
                newFamilyNameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la création de la famille ! Elle existe peut-être déjà.",
                        "Erreur de Création", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir une valeur numérique valide pour l'ID de la Famille !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode Créer Produit
    private void createProduct() {
        try {
            // Valider les entrées
            if (newProductIdField.getText().trim().isEmpty() ||
                    newProductFamilyIdField.getText().trim().isEmpty() ||
                    newProductNameField.getText().trim().isEmpty() ||
                    newProductPriceField.getText().trim().isEmpty() ||
                    newProductQuantityField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(newProductIdField.getText().trim());
            int familyId = Integer.parseInt(newProductFamilyIdField.getText().trim());
            String name = newProductNameField.getText().trim();
            double price = Double.parseDouble(newProductPriceField.getText().trim());
            int quantity = Integer.parseInt(newProductQuantityField.getText().trim());

            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être supérieur à 0 !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "La quantité ne peut pas être négative !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer l'objet produit
            Product product = new Product(id, familyId, name, price, quantity);

            // Appeler la méthode distante
            boolean success = stockService.createProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(this, "Produit créé avec succès !");
                // Vider le formulaire
                newProductIdField.setText("");
                newProductFamilyIdField.setText("");
                newProductNameField.setText("");
                newProductPriceField.setText("");
                newProductQuantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la création du produit ! Il existe peut-être déjà.",
                        "Erreur de Création", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir des valeurs numériques valides pour l'ID, l'ID Famille, le Prix et la Quantité !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthodes des produits
    private void findProductById() {
        try {
            int productId = Integer.parseInt(productIdField.getText().trim());
            Product product = stockService.getProductById(productId);

            if (product != null) {
                clearProductsTable();
                addProductToTable(product);
                JOptionPane.showMessageDialog(this, "Produit trouvé !");
            } else {
                JOptionPane.showMessageDialog(this, "Produit non trouvé !",
                        "Non Trouvé", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de produit valide !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findProductsByFamily() {
        try {
            int familyId = Integer.parseInt(familyIdField.getText().trim());
            List<Product> products = stockService.getProductsByFamily(familyId);

            clearProductsTable();

            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    addProductToTable(product);
                }
                JOptionPane.showMessageDialog(this, products.size() + " produits trouvés !");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun produit trouvé dans cette famille ou la famille n'existe pas !",
                        "Non Trouvé", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de famille valide !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void purchaseProduct() {
        try {
            int productId = Integer.parseInt(purchaseProductIdField.getText().trim());
            int quantity = Integer.parseInt(purchaseQuantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être supérieure à 0 !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.purchaseProduct(productId, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Achat réussi !");
                purchaseProductIdField.setText("");
                purchaseQuantityField.setText("");
                // Actualiser la vue du produit s'il s'agit du produit actuel
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Achat échoué ! Vérifiez la disponibilité du produit.",
                        "Erreur d'Achat", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de produit et une quantité valides !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock() {
        try {
            int productId = Integer.parseInt(addStockProductIdField.getText().trim());
            int quantity = Integer.parseInt(addStockQuantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être supérieure à 0 !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.addStock(productId, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Stock ajouté avec succès !");
                addStockProductIdField.setText("");
                addStockQuantityField.setText("");
                // Actualiser la vue du produit s'il s'agit du produit actuel
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout de stock ! Le produit n'existe peut-être pas.",
                        "Erreur de Stock", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de produit et une quantité valides !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProductPrice() {
        try {
            int productId = Integer.parseInt(updatePriceProductIdField.getText().trim());
            float newPrice = Float.parseFloat(updatePriceField.getText().trim());

            if (newPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être supérieur à 0 !",
                        "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.updateProductPrice(productId, newPrice);

            if (success) {
                JOptionPane.showMessageDialog(this, "Prix mis à jour avec succès !");
                updatePriceProductIdField.setText("");
                updatePriceField.setText("");
                // Actualiser la vue du produit s'il s'agit du produit actuel
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la mise à jour du prix ! Le produit n'existe peut-être pas.",
                        "Erreur de Mise à Jour", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de produit et un prix valides !",
                    "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur Distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Invoice methods
// Méthodes de facture
    private void findInvoice() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText().trim());
            Invoice invoice = stockService.getInvoiceById(invoiceId);
            System.out.println("invoice : " + invoice.getDate());

            if (invoice != null) {
                displayInvoice(invoice);
                JOptionPane.showMessageDialog(this, "Trouvé !");
            } else {
                invoiceDetailsArea.setText("Facture introuvable !");
                JOptionPane.showMessageDialog(this, "Facture introuvable !",
                        "Non trouvé", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de facture valide !",
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payInvoice() {
        try {
            int invoiceId = Integer.parseInt(payInvoiceIdField.getText().trim());
            boolean success = stockService.payInvoice(invoiceId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Facture payée avec succès !");
                payInvoiceIdField.setText("");
                // Actualiser la vue facture si c'était la facture courante
                if (invoiceIdField.getText().trim().equals(String.valueOf(invoiceId))) {
                    findInvoice();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Échec du paiement de la facture ! La facture n'existe peut-être pas ou est déjà payée.",
                        "Erreur de paiement", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un ID de facture valide !",
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthodes de rapport
    private void calculateRevenue() {
        try {
            String dateStr = revenueDateField.getText().trim();
            // Valider le format de date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date date = sdf.parse(dateStr);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir une date valide au format yyyy-MM-dd !",
                        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("date : " + dateStr);
            double revenue = stockService.calculateRevenue(dateStr);
            System.out.println("recette : " + revenue);
            revenueResultLabel.setText(String.format("%.2f€", revenue));

        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveInvoices() {
        try {
            boolean success = stockService.saveInvoicesToServer();

            if (success) {
                JOptionPane.showMessageDialog(this, "Factures sauvegardées avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la sauvegarde des factures !",
                        "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthodes utilitaires
    private void clearProductsTable() {
        while (productsTableModel.getRowCount() > 0) {
            productsTableModel.removeRow(0);
        }
    }

    private void addProductToTable(Product product) {
        productsTableModel.addRow(new Object[]{
                product.getId(),
                product.getId_family(),
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        });
    }

    private void displayInvoice(Invoice invoice) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        StringBuilder sb = new StringBuilder();
        sb.append("Détails de la facture :\n\n");
        sb.append("ID Facture : ").append(invoice.getId()).append("\n");
        sb.append("Date : ").append(invoice.getDate().format(dtf)).append("\n");
        sb.append("Montant total : ").append(String.format("%.2f€", invoice.getPrice())).append("\n");
        sb.append("Méthode de paiement : ").append(invoice.getPayment_method()).append("\n");
        sb.append("Statut : ").append(invoice.isPaid() ? "Payé" : "Non payé").append("\n");

        invoiceDetailsArea.setText(sb.toString());
    }

    private void createOrderPanel() {
        orderPanel = new JPanel(new BorderLayout(10, 10));
        orderPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Section de saisie en haut ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un produit à la commande"));

        inputPanel.add(new JLabel("ID Produit :"));
        orderProductIdField = new JTextField(5);
        inputPanel.add(orderProductIdField);

        inputPanel.add(new JLabel("Quantité :"));
        orderQuantityField = new JTextField(5);
        inputPanel.add(orderQuantityField);

        JButton addToOrderButton = new JButton("Ajouter à la commande");
        addToOrderButton.addActionListener(e -> addProductToOrder());
        inputPanel.add(addToOrderButton);

        orderPanel.add(inputPanel, BorderLayout.NORTH);

        // --- Tableau central ---
        orderTableModel = new DefaultTableModel(new Object[]{"ID Produit", "Nom", "Quantité", "Prix unitaire", "Total"}, 0);
        orderTable = new JTable(orderTableModel);
        JScrollPane tableScroll = new JScrollPane(orderTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Commande courante"));
        orderPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Résumé et boutons en bas ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        // Résumé
        orderSummaryArea = new JTextArea(5, 30);
        orderSummaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(orderSummaryArea);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Résumé de la commande"));
        bottomPanel.add(summaryScroll, BorderLayout.CENTER);

        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmOrderButton = new JButton("Confirmer la commande");
        confirmOrderButton.addActionListener(e -> {
            try {
                confirmOrder();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(confirmOrderButton);

        JButton clearOrderButton = new JButton("Vider la commande");
        clearOrderButton.addActionListener(e -> clearOrder());
        buttonPanel.add(clearOrderButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        orderPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addProductToOrder() {
        try {
            int productId = Integer.parseInt(orderProductIdField.getText().trim());
            int quantity = Integer.parseInt(orderQuantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être supérieure à 0 !",
                        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = stockService.getProductById(productId);
            if (product == null) {
                JOptionPane.showMessageDialog(this, "Produit introuvable !",
                        "Erreur produit", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double total = product.getPrice() * quantity;
            orderTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    total
            });

            updateOrderSummary();
            orderProductIdField.setText("");
            orderQuantityField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID produit ou quantité invalide !",
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur distante : " + e.getMessage(),
                    "Erreur distante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderSummary() {
        int rowCount = orderTableModel.getRowCount();
        double grandTotal = 0;

        for (int i = 0; i < rowCount; i++) {
            grandTotal += (double) orderTableModel.getValueAt(i, 4);
        }

        orderSummaryArea.setText("Articles dans la commande : " + rowCount + "\n");
        orderSummaryArea.append("Montant total : " + String.format("%.2f€", grandTotal));
    }

    private void confirmOrder() throws RemoteException, SQLException {
        int rowCount = orderTableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "Aucun article dans la commande !",
                    "Erreur de commande", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean allSuccessful = true;

        for (int i = 0; i < rowCount; i++) {
            int productId = (int) orderTableModel.getValueAt(i, 0);
            int quantity = (int) orderTableModel.getValueAt(i, 2);

            try {
                boolean success = stockService.purchaseProduct(productId, quantity);
                if (!success) allSuccessful = false;
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la confirmation de l'article : " + productId,
                        "Erreur distante", JOptionPane.ERROR_MESSAGE);
                allSuccessful = false;
            }
        }

        if (allSuccessful) {
            Long invoiceId = saveOrder();
            System.out.println("Invoice ID: " + invoiceId);
            //func param invoiceId
            saveOrderDetail(invoiceId);
            JOptionPane.showMessageDialog(this, "Commande confirmée avec succès !");
            clearOrder();
        } else {
            JOptionPane.showMessageDialog(this, "Certains articles n'ont pas pu être commandés !",
                    "Succès partiel", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Long saveOrder() throws RemoteException {
        String totalAmountString = orderSummaryArea.getText();
        float totalAmount = getAmountFromorderSummaryArea(totalAmountString);
        return stockService.saveInvoice(totalAmount);
    }

    public Long saveOrderDetail(Long invoiceId) throws RemoteException, SQLException {
        InvoiceDetailDAO dao = new InvoiceDetailDAO();
        List<String[]> rows = getAllOrderLines();

        for (String[] row : rows) {
            System.out.println("Ligne insérée: " + "Invoice id: " + invoiceId + "Produit: " + row[0] + ", Prix: " + row[3] + ", Quantité: " + row[2]);
            InvoiceDetail invoiceDetail = new InvoiceDetail(Integer.parseInt(row[0]), invoiceId, Float.parseFloat(row[3]), Integer.parseInt(row[2]));
            dao.addInvoiceDetail(invoiceDetail);

        }

        return invoiceId;
    }


    public List<String[]> getAllOrderLines() {
        List<String[]> rows = new ArrayList<>();
        int rowCount = orderTableModel.getRowCount();
        int columnCount = orderTableModel.getColumnCount();

        for (int i = 0; i < rowCount; i++) {
            String[] row = new String[columnCount];
            for (int j = 0; j < columnCount; j++) {
                Object value = orderTableModel.getValueAt(i, j);
                row[j] = value != null ? value.toString() : "";
            }
            rows.add(row);
        }

        return rows;
    }


    private float getAmountFromorderSummaryArea( String text){
        String[] lines = orderSummaryArea.getText().split("\n");
        float totalAmount = 0;

        for (String line : lines) {
            if (line.startsWith("Montant total :")) {
                String amountStr = line.replace("Montant total : ", "").replace("€", "").trim();
                amountStr = amountStr.replace(",", ".");
                totalAmount = Float.parseFloat(amountStr);
                break;
            }
        }
        return totalAmount;
    }

    private void clearOrder() {
        orderTableModel.setRowCount(0);
        orderSummaryArea.setText("");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StockClientUI().setVisible(true);
        });
    }
}