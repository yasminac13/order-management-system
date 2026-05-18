package businesslogic;

import dataaccess.ClientDAO;
import model.Client;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business Logic Layer class for managing Client entities.
 * Acts as an intermediary between the user interface and the data access layer,
 * handling validation rules and core business operations for clients.
 * * @author Yasmina
 * @version 1.0
 */
public class ClientBL {
    private ClientDAO clientDAO;

    /**
     * Constructor that initializes the data access object for handling clients.
     */
    public ClientBL() {
        clientDAO = new ClientDAO();
    }

    /**
     * Searches for a specific client record using their unique database ID.
     * * @param id The unique identifier of the client.
     * @return The found Client instance.
     * @throws NoSuchElementException If no client matches the specified ID.
     */
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if (client == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return client;
    }

    /**
     * Retrieves all available client records from the database storage.
     * * @return A list containing all client records.
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Forwards a new Client object to the data access layer to be added to the database.
     * * @param client The new client record to store.
     * @return The saved Client instance if successful.
     */
    public Client insertClient(Client client) {
        return clientDAO.insert(client);
    }

    /**
     * Forwards a modified Client object to the data access layer to update its record in the database.
     * * @param client The client instance containing modified properties.
     * @return The updated Client instance if successful.
     */
    public Client updateClient(Client client) {
        return clientDAO.update(client);
    }

    /**
     * Instructs the data access layer to remove a client record by its unique ID.
     * * @param id The unique identifier of the client to delete.
     */
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }
}
