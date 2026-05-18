package presentation;

import businesslogic.ClientBL;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Graphical User Interface (GUI) window for managing client profiles.
 * Provides direct user data forms, execution buttons, and a data table
 * to handle complete CRUD workflows for Client entities.
 * * @author Yasmina
 * @version 1.0
 */
public class ClientView extends JFrame {

    private JTextField txtName, txtEmail, txtAddress;
    private JButton btnAdd, btnEdit, btnDelete, btnViewAll;
    private JTable clientTable;
    private DefaultTableModel tableModel;

    private ClientBL clientBL;
    /**
     * Constructs the ClientView window frame.
     * Initializes window geometries, structures input field sheets, configures
     * database operations interaction buttons, and sets up the central data table.
     */
    public ClientView() {
        clientBL = new ClientBL();

        this.setTitle("Client Management");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField(15);
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(15);
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField(15);
        inputPanel.add(txtAddress);

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.add(inputPanel, BorderLayout.NORTH);

        this.add(inputPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnAdd = new JButton("Add Client");
        btnEdit = new JButton("Edit Client");
        btnDelete = new JButton("Delete Client");
        btnViewAll = new JButton("View All Clients");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewAll);

        this.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Address"}, 0);
        clientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        this.add(scrollPane, BorderLayout.CENTER);

        setupActionListeners();
    }

    /**
     * Binds click and interaction action event listeners to the control system.
     * Handles field emptiness evaluations, captures entry configurations, maps triggers
     * to business operations, and autofills input fields on click.
     */
    private void setupActionListeners() {
        btnAdd.addActionListener(e -> {
            try {
                String name = txtName.getText();
                String email = txtEmail.getText();
                String address = txtAddress.getText();

                if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                    return;
                }

                Client newClient = new Client(name, email, address);
                clientBL.insertClient(newClient);

                JOptionPane.showMessageDialog(this, "Client Added Successfully!");
                refreshTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnViewAll.addActionListener(e -> refreshTable());

        btnEdit.addActionListener(e -> {
            try {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a client from the table to edit!");
                    return;
                }
                int id = (int) tableModel.getValueAt(selectedRow, 0);

                String name = txtName.getText();
                String email = txtEmail.getText();
                String address = txtAddress.getText();

                if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields with the new data!");
                    return;
                }

                Client updatedClient = new Client(id, name, email, address);
                clientBL.updateClient(updatedClient); // Send to database

                JOptionPane.showMessageDialog(this, "Client Updated Successfully!");
                refreshTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a client from the table to delete!");
                    return;
                }

                int id = (int) tableModel.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    clientBL.deleteClient(id);
                    JOptionPane.showMessageDialog(this, "Client Deleted!");
                    refreshTable();

                    txtName.setText("");
                    txtEmail.setText("");
                    txtAddress.setText("");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        clientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtAddress.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });

    }

    /**
     * Refreshes customer data records within the layout display table framework.
     * Requests fresh tracking profiles from client business engines and updates
     * table properties via reflection pipelines.
     */
    private void refreshTable() {
        List<Client> clients = clientBL.findAllClients();

        if (clients != null && !clients.isEmpty()) {
            tableModel = TableReflection.buildTableModel(clients);
            clientTable.setModel(tableModel);
        } else {
            tableModel.setRowCount(0);
        }
    }

}
