package presentation;

import businesslogic.ClientBL;
import businesslogic.OrderBL;
import businesslogic.ProductBL;
import model.Client;
import model.Product;
import model.ProductOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Graphical User Interface (GUI) window for managing orders and checkout operations.
 * Coordinates input data from client and product drop-down selection options, handles
 * quantity processing validations, and uses dynamic tables to log active sales records.
 * * @author Yasmina
 * @version 1.0
 */
public class OrderView extends JFrame {
    private JComboBox<String> cbClients, cbProducts;
    private JTextField txtQuantity;
    private JButton btnPlaceOrder, btnViewAll;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    private OrderBL orderBL;
    private ClientBL clientBL;
    private ProductBL productBL;

    /**
     * Constructs the OrderView frame window.
     * Sets up window frames, registers business processing services, populates data selections
     * from data records, and configures the graphical split view grid layout.
     */
    public OrderView() {
        orderBL = new OrderBL();
        clientBL = new ClientBL();
        productBL = new ProductBL();

        this.setTitle("Order Management & Checkout");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Select Client:"));
        cbClients = new JComboBox<>();
        inputPanel.add(cbClients);

        inputPanel.add(new JLabel("Select Product:"));
        cbProducts = new JComboBox<>();
        inputPanel.add(cbProducts);

        inputPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField(10);
        inputPanel.add(txtQuantity);

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.add(inputPanel, BorderLayout.NORTH);
        this.add(leftWrapper, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnPlaceOrder = new JButton("Place Order");
        btnViewAll = new JButton("View All Orders");

        buttonPanel.add(btnPlaceOrder);
        buttonPanel.add(btnViewAll);

        this.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"Order ID", "Client ID", "Product ID", "Quantity"}, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        this.add(scrollPane, BorderLayout.CENTER);

        populateDropdowns();
        setupActionListeners();
    }

    /**
     * Queries database systems to load existing client and product list details.
     * Generates a string template for each option item formatted as "ID - Name"
     * to ensure visual information displays alongside extractable record IDs.
     */
    private void populateDropdowns() {
        List<Client> clients = clientBL.findAllClients();
        if (clients != null) {
//            for (Client c : clients) {
//                cbClients.addItem(c.getId() + " - " + c.getName());
//            }
            clients.stream()
                    .map(c -> c.getId() + " - " + c.getName())
                    .forEach(cbClients::addItem);
        }

        List<Product> products = productBL.findAllProducts();
        if (products != null) {
//            for (Product p : products) {
//                cbProducts.addItem(p.getId() + " - " + p.getName() + " (Stock: " + p.getStock() + ")");
//            }
            products.stream()
                    .map(p -> p.getId() + " - " + p.getName() + " (Stock: " + p.getStock() + ")")
                    .forEach(cbProducts::addItem);
        }
    }

    /**
     * Attaches execution logic routines to operational user controls.
     * Manages string token parsing rules to extract tracking integers from selection labels,
     * validates transaction constraints, delegates execution to order business systems,
     * and handles status popups or stock depletion warnings safely.
     */
    private void setupActionListeners() {
        btnPlaceOrder.addActionListener(e -> {
            try {
                if (cbClients.getSelectedItem() == null || cbProducts.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "You must select a Client and a Product!");
                    return;
                }
                String clientSelection = cbClients.getSelectedItem().toString();
                int clientId = Integer.parseInt(clientSelection.split(" - ")[0]);

                String productSelection = cbProducts.getSelectedItem().toString();
                int productId = Integer.parseInt(productSelection.split(" - ")[0]);

                int quantity = Integer.parseInt(txtQuantity.getText());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                    return;
                }

                ProductOrder newOrder = new ProductOrder(clientId, productId, quantity);
                orderBL.placeOrder(newOrder);

                JOptionPane.showMessageDialog(this, "Order Placed Successfully!");
                refreshTable();
                cbProducts.removeAllItems();
                populateDropdowns();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Quantity must be a valid whole number!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Checkout Failed: " + ex.getMessage());
            }
        });
        btnViewAll.addActionListener(e -> refreshTable());
    }

    /**
     * Refreshes data row records visible within the display table grid view.
     * Pulls active tracking collections from business management layers and submits
     * them to reflection mapping engines to generate dynamic interface columns.
     */
    private void refreshTable() {
        List<ProductOrder> orders = orderBL.findAllOrders();
        if (orders != null && !orders.isEmpty()) {
            tableModel = TableReflection.buildTableModel(orders);
            orderTable.setModel(tableModel);
        } else {
            tableModel.setRowCount(0);
        }
    }
}
