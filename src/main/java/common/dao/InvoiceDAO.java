package common.dao;

import common.tables.Invoice;
import server.DbManager;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceDAO {

    public boolean addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (price, payment_method, date, paid) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setFloat(1, invoice.getPrice());
            stmt.setString(2, invoice.getPayment_method());
            stmt.setTimestamp(3, new Timestamp(invoice.getDate().getTime()));
            stmt.setBoolean(4, invoice.isPaid());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Invoice(
                        rs.getInt("id"),
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        new Date(rs.getTimestamp("date").getTime()),
                        rs.getBoolean("paid")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                invoices.add(new Invoice(
                        rs.getInt("id"),
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        new Date(rs.getTimestamp("date").getTime()),
                        rs.getBoolean("paid")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByDate(String dateStr) {
        List<Invoice> invoices = new ArrayList<>();

        // Format de date attendu: "yyyy-MM-dd"
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);

            // Obtenir le début et la fin de la journée
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String sql = "SELECT * FROM invoice WHERE DATE(date) = ?";

            try (Connection conn = DbManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, sqlDate);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    invoices.add(new Invoice(
                            rs.getInt("id"),
                            rs.getFloat("price"),
                            rs.getString("payment_method"),
                            new Date(rs.getTimestamp("date").getTime()),
                            rs.getBoolean("paid")
                    ));
                }
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public boolean updateInvoicePaymentStatus(int id, boolean paid) {
        String sql = "UPDATE invoice SET paid = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, paid);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInvoice(int id) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveAllInvoices() {
        // Cette méthode simule la sauvegarde des factures sur le serveur du siège
        // Dans une implémentation réelle, vous pourriez:
        // 1. Exporter les factures dans un fichier
        // 2. Envoyer ce fichier à un autre serveur via FTP, SFTP, etc.
        // 3. Ou synchroniser directement avec une base de données distante

        try {
            List<Invoice> invoices = getAllInvoices();
            // Ici, vous pourriez implémenter la logique de sauvegarde réelle
            // Par exemple, écrire dans un fichier, envoyer à un autre serveur, etc.

            System.out.println("Sauvegarde de " + invoices.size() + " factures effectuée");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}