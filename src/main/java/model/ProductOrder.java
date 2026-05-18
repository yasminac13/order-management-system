package model;

/**
 * Represents a transaction record joining a specific client to a specific product purchase.
 * * @author Yasmina
 * @version 1.0
 */
public class ProductOrder {
    private int id;
    private int clientId;
    private int productId;
    private int quantity;

    /**
     * Default constructor for creating an empty ProductOrder instance.
     */
    public ProductOrder(){
    }

    /**
     * Constructs a complete database-backed transaction record mapping with historical ID log.
     *
     * @param id        The unique identifier for this order instance.
     * @param clientId  The unique database ID of the associated client.
     * @param productId The unique database ID of the associated item.
     * @param quantity  The final item quantity processed.
     */
    public ProductOrder(int id, int clientId, int productId, int quantity) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Constructs an active order instance intended for transaction processing.
     *
     * @param clientId  The unique database ID of the client placing the order.
     * @param productId The unique database ID of the item being purchased.
     * @param quantity  The specific item quantity requested.
     */
    public ProductOrder(int clientId, int productId, int quantity) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductOrder [id=" + id + ", clientId=" + clientId + ", productId=" + productId + ", quantity=" + quantity + "]";
    }
}
