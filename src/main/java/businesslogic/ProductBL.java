package businesslogic;

import dataaccess.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business Logic Layer class for managing Product entities.
 * Serves as the processing layer between the presentation views and the data access subsystem,
 * handling validation and execution logic for items within the inventory catalog.
 * * @author Yasmina
 * @version 1.0
 */
public class ProductBL {
    private ProductDAO productDAO;


    /**
     * Constructor that initializes the data access object for handling products.
     */
    public ProductBL(){
        productDAO = new ProductDAO();
    }


    /**
     * Searches for a specific product record using its unique database ID.
     * * @param id The unique identifier of the target product.
     * @return The matching Product instance.
     * @throws NoSuchElementException If no product record matches the specified ID.
     */
    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id = " + id + " was not found!");
        }
        return product;
    }


    /**
     * Retrieves all product records currently tracked within the database inventory.
     * * @return A list containing all available product records.
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }


    /**
     * Forwards a new Product configuration to the data access layer to be saved into database records.
     * * @param product The product instance data configuration to be created.
     * @return The stored Product instance if successful.
     */
    public Product insertProduct(Product product) {
        return productDAO.insert(product);
    }


    /**
     * Forwards an altered Product object configuration to the data access layer to update its state in storage.
     * * @param product The updated product instance configuration matching an existing database record ID.
     * @return The updated Product instance if successful.
     */
    public Product updateProduct(Product product) {
        return productDAO.update(product);
    }


    /**
     * Requests the deletion of a specific product entry matching the assigned database identifier.
     * * @param id The unique identifier of the product entry to be erased.
     */
    public void deleteProduct(int id) {
        productDAO.delete(id);
    }
}
