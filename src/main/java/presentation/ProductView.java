package presentation;

import businesslogic.ProductBL;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Graphical User Interface (GUI) window for managing the product inventory.
 * Provides entry fields, action triggers, and a dynamic data grid to execute
 * complete CRUD operations on Product entities.
 * * @author Yasmina
 * @version 1.0
 */
public class ProductView extends JFrame {
    private JTextField txtName, txtPrice, txtStock;
    private JButton btnAdd, btnEdit, btnDelete, btnViewAll;
    private JTable productTable;
    private DefaultTableModel tableModel;

    private ProductBL productBL;

    /**
     * Constructs the ProductView frame window.
     * Sets up window geometries, creates the split layout panels for input forms,
     * registers operation buttons, configures the data table, and hooks up event systems.
     */
    public ProductView() {
        productBL = new ProductBL();

        this.setTitle("Product Inventory Management");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Product Name:"));
        txtName = new JTextField(15);
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Price ($):"));
        txtPrice = new JTextField(15);
        inputPanel.add(txtPrice);

        inputPanel.add(new JLabel("Stock Quantity:"));
        txtStock = new JTextField(15);
        inputPanel.add(txtStock);

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.add(inputPanel, BorderLayout.NORTH);
        this.add(leftWrapper, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnAdd = new JButton("Add Product");
        btnEdit = new JButton("Edit Product");
        btnDelete = new JButton("Delete Product");
        btnViewAll = new JButton("View Inventory");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewAll);

        this.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        this.add(scrollPane, BorderLayout.CENTER);
        setupActionListeners();
    }

    /**
     * Binds action event listeners to control elements.
     * Attaches specific input sanitization and transaction triggers to execution buttons
     * (Add, Edit, Delete, View All) and wires grid tracking to sync fields on select.
     */
    private void setupActionListeners() {
        btnAdd.addActionListener(e -> {
            try {
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int stock = Integer.parseInt(txtStock.getText());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a product name!");
                    return;
                }

                Product newProduct = new Product(name, price, stock);
                productBL.insertProduct(newProduct);

                JOptionPane.showMessageDialog(this, "Product Added Successfully!");
                refreshTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Price must be a decimal and Stock must be a whole number!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnEdit.addActionListener(e -> {
            try {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a product from the table to edit!");
                    return;
                }

                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int stock = Integer.parseInt(txtStock.getText());

                Product updatedProduct = new Product(id, name, price, stock);
                productBL.updateProduct(updatedProduct);

                JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                refreshTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid number format for Price or Stock!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a product from the table to delete!");
                    return;
                }

                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    productBL.deleteProduct(id);
                    JOptionPane.showMessageDialog(this, "Product Deleted!");
                    refreshTable();

                    txtName.setText("");
                    txtPrice.setText("");
                    txtStock.setText("");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnViewAll.addActionListener(e -> refreshTable());

        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtPrice.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtStock.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    /**
     * Refreshes data components within the display frame.
     * Fetches current item records from business layers and targets them through
     * TableReflection engine pipelines to construct a dynamic user layout grid.
     */
    private void refreshTable() {
        List<Product> products = productBL.findAllProducts();
        if (products != null && !products.isEmpty()) {
            tableModel = TableReflection.buildTableModel(products);
            productTable.setModel(tableModel);
        } else {
            tableModel.setRowCount(0);
        }
    }
}
