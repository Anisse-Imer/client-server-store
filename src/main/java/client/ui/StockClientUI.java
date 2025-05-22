package client.ui;

import common.IStockService;
import common.tables.Family;
import common.tables.Invoice;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StockClientUI extends JFrame {
    private IStockService stockService;
    private JTabbedPane tabbedPane;
    private JPanel productsPanel;
    private JPanel invoicesPanel;
    private JPanel reportsPanel;
    private JPanel createProductPanel;
    private JPanel createFamilyPanel; // New panel for creating families

    // Products tab components
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

    // Create Product tab components
    private JTextField newProductIdField;
    private JTextField newProductFamilyIdField;
    private JTextField newProductNameField;
    private JTextField newProductPriceField;
    private JTextField newProductQuantityField;

    // Create Family tab components
    private JTextField newFamilyIdField;
    private JTextField newFamilyNameField;
    private JTextArea familyResultArea;

    // Invoices tab components
    private JTextField invoiceIdField;
    private JTextArea invoiceDetailsArea;
    private JTextField payInvoiceIdField;

    // Reports tab components
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
            JOptionPane.showMessageDialog(this, "Connected to Stock Service successfully!");
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to service: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setTitle("Stock Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        createProductsPanel();
        createInvoicesPanel();
        createReportsPanel();
        createNewProductPanel();
        createNewFamilyPanel(); // Create the new family panel
        createOrderPanel();

        tabbedPane.addTab("Products", productsPanel);
        tabbedPane.addTab("Create Product", createProductPanel);
        tabbedPane.addTab("Create Family", createFamilyPanel); // Add the new family tab
        tabbedPane.addTab("Invoices", invoicesPanel);
        tabbedPane.addTab("Reports", reportsPanel);
        tabbedPane.addTab("Orders", orderPanel);


        add(tabbedPane);
    }

    private void createProductsPanel() {
        productsPanel = new JPanel(new BorderLayout(10, 10));
        productsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel for product lookup
        JPanel lookupPanel = new JPanel(new BorderLayout(10, 10));
        lookupPanel.setBorder(BorderFactory.createTitledBorder("Product Lookup"));

        JPanel lookupInputPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        lookupInputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        lookupInputPanel.add(productIdField);
        JButton findProductButton = new JButton("Find Product");
        findProductButton.addActionListener(e -> findProductById());
        lookupInputPanel.add(findProductButton);

        lookupInputPanel.add(new JLabel("Family ID:"));
        familyIdField = new JTextField();
        lookupInputPanel.add(familyIdField);
        JButton findByFamilyButton = new JButton("Find By Family");
        findByFamilyButton.addActionListener(e -> findProductsByFamily());
        lookupInputPanel.add(findByFamilyButton);

        lookupPanel.add(lookupInputPanel, BorderLayout.NORTH);

        // Table for displaying products
        productsTableModel = new DefaultTableModel(
                new Object[]{"ID", "Family ID", "Name", "Price", "Quantity"}, 0);
        productsTable = new JTable(productsTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        lookupPanel.add(tableScrollPane, BorderLayout.CENTER);

        productsPanel.add(lookupPanel, BorderLayout.CENTER);

        // Bottom panel for product operations
        JPanel operationsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        operationsPanel.setBorder(BorderFactory.createTitledBorder("Product Operations"));

        // Purchase product panel
        JPanel purchasePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        purchasePanel.add(new JLabel("Product ID:"));
        purchaseProductIdField = new JTextField(5);
        purchasePanel.add(purchaseProductIdField);
        purchasePanel.add(new JLabel("Quantity:"));
        purchaseQuantityField = new JTextField(5);
        purchasePanel.add(purchaseQuantityField);
        JButton purchaseButton = new JButton("Purchase");
        purchaseButton.addActionListener(e -> purchaseProduct());
        purchasePanel.add(purchaseButton);
        operationsPanel.add(purchasePanel);

        // Add stock panel
        JPanel addStockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addStockPanel.add(new JLabel("Product ID:"));
        addStockProductIdField = new JTextField(5);
        addStockPanel.add(addStockProductIdField);
        addStockPanel.add(new JLabel("Quantity:"));
        addStockQuantityField = new JTextField(5);
        addStockPanel.add(addStockQuantityField);
        JButton addStockButton = new JButton("Add Stock");
        addStockButton.addActionListener(e -> addStock());
        addStockPanel.add(addStockButton);
        operationsPanel.add(addStockPanel);

        // Update price panel
        JPanel updatePricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePricePanel.add(new JLabel("Product ID:"));
        updatePriceProductIdField = new JTextField(5);
        updatePricePanel.add(updatePriceProductIdField);
        updatePricePanel.add(new JLabel("New Price:"));
        updatePriceField = new JTextField(5);
        updatePricePanel.add(updatePriceField);
        JButton updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(e -> updateProductPrice());
        updatePricePanel.add(updatePriceButton);
        operationsPanel.add(updatePricePanel);

        productsPanel.add(operationsPanel, BorderLayout.SOUTH);
    }

    private void createNewProductPanel() {
        createProductPanel = new JPanel(new BorderLayout(10, 10));
        createProductPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Product"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Product ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        newProductIdField = new JTextField(20);
        formPanel.add(newProductIdField, gbc);

        // Family ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Family ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        newProductFamilyIdField = new JTextField(20);
        formPanel.add(newProductFamilyIdField, gbc);

        // Product Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        newProductNameField = new JTextField(20);
        formPanel.add(newProductNameField, gbc);

        // Product Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        newProductPriceField = new JTextField(20);
        formPanel.add(newProductPriceField, gbc);

        // Product Quantity
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Initial Quantity:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        newProductQuantityField = new JTextField(20);
        formPanel.add(newProductQuantityField, gbc);

        // Create Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("Create Product");
        createButton.addActionListener(e -> createProduct());
        formPanel.add(createButton, gbc);

        // Add form to panel
        createProductPanel.add(formPanel, BorderLayout.NORTH);

        // Add a result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Results"));

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
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Family"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Family ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Family ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        newFamilyIdField = new JTextField(20);
        formPanel.add(newFamilyIdField, gbc);

        // Family Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        newFamilyNameField = new JTextField(20);
        formPanel.add(newFamilyNameField, gbc);

        // Create Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("Create Family");
        createButton.addActionListener(e -> createFamily());
        formPanel.add(createButton, gbc);

        // Add form to panel
        createFamilyPanel.add(formPanel, BorderLayout.NORTH);

        // Add a result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Results"));

        familyResultArea = new JTextArea(10, 30);
        familyResultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(familyResultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        createFamilyPanel.add(resultPanel, BorderLayout.CENTER);
    }

    private void createInvoicesPanel() {
        invoicesPanel = new JPanel(new BorderLayout(10, 10));
        invoicesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Invoice lookup panel
        JPanel invoiceLookupPanel = new JPanel(new BorderLayout(10, 10));
        invoiceLookupPanel.setBorder(BorderFactory.createTitledBorder("Invoice Lookup"));

        JPanel lookupInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lookupInputPanel.add(new JLabel("Invoice ID:"));
        invoiceIdField = new JTextField(10);
        lookupInputPanel.add(invoiceIdField);
        JButton findInvoiceButton = new JButton("Find Invoice");
        findInvoiceButton.addActionListener(e -> findInvoice());
        lookupInputPanel.add(findInvoiceButton);

        invoiceLookupPanel.add(lookupInputPanel, BorderLayout.NORTH);

        // Invoice details area
        invoiceDetailsArea = new JTextArea();
        invoiceDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(invoiceDetailsArea);
        invoiceLookupPanel.add(detailsScrollPane, BorderLayout.CENTER);

        invoicesPanel.add(invoiceLookupPanel, BorderLayout.CENTER);

        // Invoice payment panel
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Pay Invoice"));
        paymentPanel.add(new JLabel("Invoice ID:"));
        payInvoiceIdField = new JTextField(10);
        paymentPanel.add(payInvoiceIdField);
        JButton payInvoiceButton = new JButton("Pay Invoice");
        payInvoiceButton.addActionListener(e -> payInvoice());
        paymentPanel.add(payInvoiceButton);

        invoicesPanel.add(paymentPanel, BorderLayout.SOUTH);
    }

    private void createReportsPanel() {
        reportsPanel = new JPanel(new BorderLayout(10, 10));
        reportsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Revenue calculation panel
        JPanel revenuePanel = new JPanel(new BorderLayout(10, 10));
        revenuePanel.setBorder(BorderFactory.createTitledBorder("Revenue Calculation"));

        JPanel revenueInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        revenueInputPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        revenueDateField = new JTextField(10);
        revenueInputPanel.add(revenueDateField);
        JButton calculateButton = new JButton("Calculate Revenue");
        calculateButton.addActionListener(e -> calculateRevenue());
        revenueInputPanel.add(calculateButton);

        revenuePanel.add(revenueInputPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultPanel.add(new JLabel("Total Revenue: "));
        revenueResultLabel = new JLabel("€0.00");
        resultPanel.add(revenueResultLabel);

        revenuePanel.add(resultPanel, BorderLayout.CENTER);

        reportsPanel.add(revenuePanel, BorderLayout.NORTH);

        // Save invoices panel
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.setBorder(BorderFactory.createTitledBorder("Save Invoices"));
        saveInvoicesButton = new JButton("Save Invoices to Server");
        saveInvoicesButton.addActionListener(e -> saveInvoices());
        savePanel.add(saveInvoicesButton);

        reportsPanel.add(savePanel, BorderLayout.CENTER);
    }

    // Create Family method
    private void createFamily() {
        try {
            // Validate inputs
            if (newFamilyIdField.getText().trim().isEmpty() ||
                    newFamilyNameField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please fill all fields!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(newFamilyIdField.getText().trim());
            String name = newFamilyNameField.getText().trim();

            // Create family object
            Family family = new Family(id, name);

            // Call remote method
            boolean success = stockService.createFamily(family);

            if (success) {
                JOptionPane.showMessageDialog(this, "Family created successfully!");
                // Display the created family
                familyResultArea.setText("Family created:\n" +
                        "ID: " + id + "\n" +
                        "Name: " + name);
                // Clear the form
                newFamilyIdField.setText("");
                newFamilyNameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create family! It might already exist.",
                        "Creation Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for Family ID!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create Product method
    private void createProduct() {
        try {
            // Validate inputs
            if (newProductIdField.getText().trim().isEmpty() ||
                    newProductFamilyIdField.getText().trim().isEmpty() ||
                    newProductNameField.getText().trim().isEmpty() ||
                    newProductPriceField.getText().trim().isEmpty() ||
                    newProductQuantityField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please fill all fields!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(newProductIdField.getText().trim());
            int familyId = Integer.parseInt(newProductFamilyIdField.getText().trim());
            String name = newProductNameField.getText().trim();
            double price = Double.parseDouble(newProductPriceField.getText().trim());
            int quantity = Integer.parseInt(newProductQuantityField.getText().trim());

            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create product object
            Product product = new Product(id, familyId, name, price, quantity);

            // Call remote method
            boolean success = stockService.createProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(this, "Product created successfully!");
                // Clear the form
                newProductIdField.setText("");
                newProductFamilyIdField.setText("");
                newProductNameField.setText("");
                newProductPriceField.setText("");
                newProductQuantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create product! It might already exist.",
                        "Creation Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Family ID, Price, and Quantity!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Product methods
    private void findProductById() {
        try {
            int productId = Integer.parseInt(productIdField.getText().trim());
            Product product = stockService.getProductById(productId);

            if (product != null) {
                clearProductsTable();
                addProductToTable(product);
                JOptionPane.showMessageDialog(this, "Product found!");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!",
                        "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid product ID!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, products.size() + " products found!");
            } else {
                JOptionPane.showMessageDialog(this, "No products found in this family or family does not exist!",
                        "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid family ID!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void purchaseProduct() {
        try {
            int productId = Integer.parseInt(purchaseProductIdField.getText().trim());
            int quantity = Integer.parseInt(purchaseQuantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.purchaseProduct(productId, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Purchase successful!");
                purchaseProductIdField.setText("");
                purchaseQuantityField.setText("");
                // Refresh product view if it was the current product
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Purchase failed! Check product availability.",
                        "Purchase Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid product ID and quantity!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock() {
        try {
            int productId = Integer.parseInt(addStockProductIdField.getText().trim());
            int quantity = Integer.parseInt(addStockQuantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.addStock(productId, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Stock added successfully!");
                addStockProductIdField.setText("");
                addStockQuantityField.setText("");
                // Refresh product view if it was the current product
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add stock! Product may not exist.",
                        "Stock Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid product ID and quantity!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProductPrice() {
        try {
            int productId = Integer.parseInt(updatePriceProductIdField.getText().trim());
            float newPrice = Float.parseFloat(updatePriceField.getText().trim());

            if (newPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = stockService.updateProductPrice(productId, newPrice);

            if (success) {
                JOptionPane.showMessageDialog(this, "Price updated successfully!");
                updatePriceProductIdField.setText("");
                updatePriceField.setText("");
                // Refresh product view if it was the current product
                if (productIdField.getText().trim().equals(String.valueOf(productId))) {
                    findProductById();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update price! Product may not exist.",
                        "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid product ID and price!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Invoice methods
    private void findInvoice() {
        try {
            int invoiceId = Integer.parseInt(invoiceIdField.getText().trim());
            Invoice invoice = stockService.getInvoiceById(invoiceId);

            if (invoice != null) {
                displayInvoice(invoice);
                JOptionPane.showMessageDialog(this, "Invoice found!");
            } else {
                invoiceDetailsArea.setText("Invoice not found!");
                JOptionPane.showMessageDialog(this, "Invoice not found!",
                        "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid invoice ID!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payInvoice() {
        try {
            int invoiceId = Integer.parseInt(payInvoiceIdField.getText().trim());
            boolean success = stockService.payInvoice(invoiceId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Invoice paid successfully!");
                payInvoiceIdField.setText("");
                // Refresh invoice view if it was the current invoice
                if (invoiceIdField.getText().trim().equals(String.valueOf(invoiceId))) {
                    findInvoice();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to pay invoice! Invoice may not exist or is already paid.",
                        "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid invoice ID!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Report methods
    private void calculateRevenue() {
        try {
            String dateStr = revenueDateField.getText().trim();
            // Validate date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date date = sdf.parse(dateStr);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in format yyyy-MM-dd!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double revenue = stockService.calculateRevenue(dateStr);
            revenueResultLabel.setText(String.format("€%.2f", revenue));

        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveInvoices() {
        try {
            boolean success = stockService.saveInvoicesToServer();

            if (success) {
                JOptionPane.showMessageDialog(this, "Invoices saved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save invoices!",
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper methods
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice Details:\n\n");
        sb.append("Invoice ID: ").append(invoice.getId()).append("\n");
        sb.append("Date: ").append(sdf.format(invoice.getDate())).append("\n");
        sb.append("Total Amount: €").append(String.format("%.2f", invoice.getPrice())).append("\n");
        sb.append("Payment Method: ").append(invoice.getPayment_method()).append("\n");
        sb.append("Status: ").append(invoice.isPaid() ? "Paid" : "Unpaid").append("\n");

        invoiceDetailsArea.setText(sb.toString());
    }
    private void createOrderPanel() {
        orderPanel = new JPanel(new BorderLayout(10, 10));
        orderPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Top Input Section ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product to Order"));

        inputPanel.add(new JLabel("Product ID:"));
        orderProductIdField = new JTextField(5);
        inputPanel.add(orderProductIdField);

        inputPanel.add(new JLabel("Quantity:"));
        orderQuantityField = new JTextField(5);
        inputPanel.add(orderQuantityField);

        JButton addToOrderButton = new JButton("Add to Order");
        addToOrderButton.addActionListener(e -> addProductToOrder());
        inputPanel.add(addToOrderButton);

        orderPanel.add(inputPanel, BorderLayout.NORTH);

        // --- Center Table ---
        orderTableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Quantity", "Unit Price", "Total"}, 0);
        orderTable = new JTable(orderTableModel);
        JScrollPane tableScroll = new JScrollPane(orderTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Current Order"));
        orderPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Bottom Summary + Buttons ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        // Summary
        orderSummaryArea = new JTextArea(5, 30);
        orderSummaryArea.setEditable(false);
        JScrollPane summaryScroll = new JScrollPane(orderSummaryArea);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        bottomPanel.add(summaryScroll, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmOrderButton = new JButton("Confirm Order");
        confirmOrderButton.addActionListener(e -> confirmOrder());
        buttonPanel.add(confirmOrderButton);

        JButton clearOrderButton = new JButton("Clear Order");
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
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = stockService.getProductById(productId);
            if (product == null) {
                JOptionPane.showMessageDialog(this, "Product not found!",
                        "Product Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Invalid product ID or quantity!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Remote error: " + e.getMessage(),
                    "Remote Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderSummary() {
        int rowCount = orderTableModel.getRowCount();
        double grandTotal = 0;

        for (int i = 0; i < rowCount; i++) {
            grandTotal += (double) orderTableModel.getValueAt(i, 4);
        }

        orderSummaryArea.setText("Items in Order: " + rowCount + "\n");
        orderSummaryArea.append("Total Amount: €" + String.format("%.2f", grandTotal));
    }

    private void confirmOrder() {
        int rowCount = orderTableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No items in the order!",
                    "Order Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Error confirming item: " + productId,
                        "Remote Error", JOptionPane.ERROR_MESSAGE);
                allSuccessful = false;
            }
        }

        if (allSuccessful) {
            JOptionPane.showMessageDialog(this, "Order confirmed successfully!");
            clearOrder();
        } else {
            JOptionPane.showMessageDialog(this, "Some items could not be ordered!",
                    "Partial Success", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void saveOrder() throws RemoteException {
          String totalAmountString = orderSummaryArea.getText();
          float totalAmount = getAmountFromorderSummaryArea(totalAmountString);
          boolean save = stockService.saveInvoice(totalAmount);
    }

    private float getAmountFromorderSummaryArea( String text){
        String[] lines = orderSummaryArea.getText().split("\n");
        float totalAmount = 0;

        for (String line : lines) {
            if (line.startsWith("Total Amount:")) {
                String amountStr = line.replace("Total Amount: €", "").trim();
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