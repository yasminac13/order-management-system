package businesslogic;

import dataaccess.BillDAO;
import dataaccess.ClientDAO;
import dataaccess.OrderDAO;
import dataaccess.ProductDAO;
import model.Bill;
import model.Client;
import model.Product;
import model.ProductOrder;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Business Logic Layer class for processing customer transaction orders.
 * Coordinates the full checkout lifecycle, including stock availability verification,
 * inventory level adjustment, order tracking, and sales ledger receipt logging.
 * * @author Yasmina
 * @version 1.0
 */
public class OrderBL {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private ClientDAO clientDAO;

    private BillDAO billDAO;

    /**
     * Constructor that initializes all required data access objects for processing an order lifecycle.
     */
    public OrderBL(){
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        clientDAO = new ClientDAO();
        billDAO = new BillDAO();
    }

    /**
     * Processes a customer order transaction by executing stock verification, updating physical inventory,
     * logging the order details, and generating a persistent receipt (Bill).
     * * @param order The order transaction model containing target customer, product, and item quantity selections.
     * @return The registered ProductOrder instance containing its persistent database tracking ID.
     * @throws Exception If the product record cannot be found, or if current warehouse inventory is insufficient.
     */
    public ProductOrder placeOrder(ProductOrder order) throws Exception {
        Product product = productDAO.findById(order.getProductId());
        if (product == null) {
            throw new Exception("Error: Product does not exist!");
        }

        if (product.getStock() < order.getQuantity()) {
            throw new Exception("Under-stock! Not enough items available to fulfill this order.");
        }

        int newStock = product.getStock() - order.getQuantity();
        product.setStock(newStock);
        productDAO.update(product);

        ProductOrder insertedOrder = orderDAO.insert(order);

        Client client = clientDAO.findById(order.getClientId());
        double totalPrice = product.getPrice() * order.getQuantity();

        Bill bill = new Bill(
                0,
                insertedOrder.getId(),
                client.getName(),
                product.getName(),
                order.getQuantity(),
                totalPrice,
                LocalDateTime.now()
        );

        billDAO.insert(bill);

        return insertedOrder;
    }

    /**
     * Gathers all historical transaction records logged within the system database.
     * * @return A list of all historical ProductOrder entries.
     */
    public List<ProductOrder> findAllOrders() {
        return orderDAO.findAll();
    }

}
