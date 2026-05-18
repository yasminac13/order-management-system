package dataaccess;

import model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object specifically for managing the immutable Bill records.
 * Handles direct SQL statements for writing finalized order receipts to the Log table.
 * * @author Yasmina
 * @version 1.0
 */
public class BillDAO {

    protected static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());
    private static final String INSERT_STATEMENT =
            "INSERT INTO Log (order_id, client_name, product_name, quantity, total_price, log_date) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * Inserts a completed transaction receipt (Bill) into the database Log table.
     * Manually maps the Java record properties to the specific table columns.
     * * @param bill The immutable receipt log containing finalized checkout values.
     * @return The original Bill instance if saved successfully, or null if an error occurs.
     */
    public Bill insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(INSERT_STATEMENT);

            statement.setInt(1, bill.orderId());
            statement.setString(2, bill.clientName());
            statement.setString(3, bill.productName());
            statement.setInt(4, bill.quantity());
            statement.setDouble(5, bill.totalPrice());
            statement.setTimestamp(6, Timestamp.valueOf(bill.logDate()));

            statement.executeUpdate();

            return bill;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
