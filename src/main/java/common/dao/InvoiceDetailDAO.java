package common.dao;

import common.tables.InvoiceDetail;
import server.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {

    public InvoiceDetailDAO() {}

    public Long addInvoiceDetail(InvoiceDetail detail) throws SQLException {
        String sql = "INSERT INTO invoice_detail (product_id, id_invoice, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detail.getId_product());
            stmt.setLong(2, detail.getId_invoice());
            stmt.setFloat(3, detail.getPrice());
            stmt.setInt(4, detail.getQuantity());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return Long.getLong(String.valueOf(detail.getId()));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InvoiceDetail getInvoiceDetailById(Connection connection, int id) throws SQLException {
        String sql = "SELECT * FROM invoice_detail WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToInvoiceDetail(rs);
            }
        }
        return null;
    }

    public List<InvoiceDetail> getAllInvoiceDetails(Connection connection) throws SQLException {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToInvoiceDetail(rs));
            }
        }
        return list;
    }

    public void updateInvoiceDetail(Connection connection, InvoiceDetail detail) throws SQLException {
        String sql = "UPDATE invoice_detail SET product_id = ?, id_invoice = ?, price = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detail.getId_product());
            stmt.setLong(2, detail.getId_invoice());
            stmt.setFloat(3, detail.getPrice());
            stmt.setInt(4, detail.getQuantity());
            stmt.setInt(5, detail.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteInvoiceDetail(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM invoice_detail WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(rs.getInt("id"));
        detail.setId_product(rs.getInt("id_product"));
        detail.setId_invoice(rs.getLong("id_invoice"));
        detail.setPrice(rs.getFloat("price"));
        detail.setQuantity(rs.getInt("quantity"));
        return detail;
    }
}
