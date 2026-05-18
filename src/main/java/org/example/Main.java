package org.example;

import dataaccess.ConnectionFactory;
import presentation.ClientView;
import presentation.OrderView;
import presentation.ProductView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;


public class Main {
    public static void main(String[] args) {
        JFrame mainMenu = new JFrame("Main Menu");
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize(300, 200);
        mainMenu.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnClients = new JButton("Manage Clients");
        btnClients.addActionListener(e -> new ClientView().setVisible(true));

        JButton btnProducts = new JButton("Manage Inventory");
        btnProducts.addActionListener(e -> new ProductView().setVisible(true));

        JButton btnOrders = new JButton("Place Order");
        btnOrders.addActionListener(e -> new OrderView().setVisible(true));

        mainMenu.add(btnClients);
        mainMenu.add(btnProducts);
        mainMenu.add(btnOrders);

        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }
}