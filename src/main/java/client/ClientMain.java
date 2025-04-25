package client;

import client.ui.StockClientUI;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StockClientUI().setVisible(true);
        });
    }
}