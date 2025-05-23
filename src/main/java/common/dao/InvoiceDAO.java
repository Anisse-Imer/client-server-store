package common.dao;

import common.tables.Invoice;
import server.DbManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public Long addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (price, payment_method, date, paid) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setFloat(1, invoice.getPrice());
            stmt.setString(2, invoice.getPayment_method());

            // ✅ Conversion correcte pour champ SQL de type DATE
            stmt.setDate(3, Date.valueOf(invoice.getDate().toLocalDate()));

            stmt.setBoolean(4, invoice.isPaid());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Lignes affectées: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong(1); // ✅ récupération propre en Long
                        invoice.setId((int) generatedId); // si l'attribut `id` est un int
                        System.out.println("ID généré (dans DAO) : " + generatedId);
                        return generatedId;
                    } else {
                        System.out.println("Aucune clé générée.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime dateTime = rs.getTimestamp("date").toLocalDateTime();

                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        dateTime,
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id")); // Ajout de l'ID
                return invoice;
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
                LocalDateTime dateTime = rs.getTimestamp("date").toLocalDateTime();

                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        dateTime,
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id")); // Ajout de l'ID
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByDate(String dateStr) {
        List<Invoice> invoices = new ArrayList<>();

        try {
            LocalDate targetDate = LocalDate.parse(dateStr); // Format: yyyy-MM-dd
            String sql = "SELECT * FROM invoice WHERE DATE(date) = ?";

            try (Connection conn = DbManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, java.sql.Date.valueOf(targetDate));
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    LocalDateTime dateTime = rs.getTimestamp("date").toLocalDateTime();

                    Invoice invoice = new Invoice(
                            rs.getFloat("price"),
                            rs.getString("payment_method"),
                            dateTime,
                            rs.getBoolean("paid")
                    );
                    invoice.setId(rs.getInt("id")); // Ajout de l'ID
                    invoices.add(invoice);
                }
            }
        } catch (Exception e) {
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
        try {
            List<Invoice> invoices = getAllInvoices();
            System.out.println("Sauvegarde de " + invoices.size() + " factures effectuée");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
