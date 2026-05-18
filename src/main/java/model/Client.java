package model;

/**
 * Represents a Client entity in the system database.
 * Holds personal identification, contact information, and delivery location details.
 * * @author Yasmina
 * @version 1.0
 */

public class Client {
    private int id;
    private String name;
    private String email;
    private String address;


    /**
     * Default constructor for creating an empty Client instance.
     * Useful for reflection mechanisms within the data access layer.
     */
    public Client(){
    }

    /**
     * Constructs a complete Client instance with an explicitly assigned ID.
     * Typically used when fetching existing client data out of the database.
     *
     * @param id      The unique identifier of the client.
     * @param name    The full name of the client.
     * @param email   The email address of the client.
     * @param address The shipping/billing address of the client.
     */
    public Client(int id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }


    /**
     * Constructs a new Client without an ID.
     * Typically used when inserting a new client record into the database before an ID is auto-assigned.
     *
     * @param name    The full name of the client.
     * @param email   The email address of the client.
     * @param address The shipping/billing address of the client.
     */
    public Client(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + "]";
    }

}
