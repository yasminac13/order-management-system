package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Represents a receipt or invoice for a completed order.
 * Automatically generated as an immutable log record whenever an order is successfully processed.
 *
 * @param id          The unique ID of this bill/receipt.
 * @param orderId     The ID of the order associated with this receipt.
 * @param clientName  The name of the client at the time of purchase.
 * @param productName The name of the product at the time of purchase.
 * @param quantity    The number of items purchased.
 * @param totalPrice  The total price calculated for the purchase.
 * @param logDate     The exact date and time the receipt was created.
 * @author Yasmina
 * @version 1.0
 */
public record Bill(int id, int orderId, String clientName, String productName, int quantity, double totalPrice, LocalDateTime logDate){
}


