package client;

import client.ui.StockClientUI;
import common.IStockService;
import common.tables.Product;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StockClientUI().setVisible(true);
        });
    }
}