package dataaccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A generic Data Access Object (DAO) that performs basic CRUD operations for any model class.
 * Uses Java Reflection to dynamically map database tables to Java objects.
 *
 * @param <T> The model class type this DAO handles (e.g., Client, Product).
 * @author Yasmina
 * @version 1.0
 */
public class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructor that figures out the actual class type of T at runtime.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Dynamically builds a basic SELECT query filtered by a specific column name.
     * * @param field The column name to filter by in the WHERE clause.
     * @return A SQL query string.
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" = ?");
        return sb.toString();
    }

    /**
     * Finds and returns a single database record by its unique ID.
     * * @param id The unique identifier of the record.
     * @return The found object instance, or null if no record matches.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            resultSet = statement.executeQuery();

            List<T> objects = createObjects(resultSet);
            if(!objects.isEmpty()) {
                return objects.get(0);//return first match
            }
        }catch(SQLException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
        }


        /**
         * Processes a database ResultSet and uses reflection to automatically transform
         * the rows into a list of Java objects.
         * * @param resultSet The query result data from MySQL.
         * @return A list of populated Java objects.
         */
        private List<T> createObjects(ResultSet resultSet) {
            List<T> list = new ArrayList<T>();
            try {
                while (resultSet.next()) {
                    //new empty instance of model class
                    T instance = type.getDeclaredConstructor().newInstance();
                    for (Field field : type.getDeclaredFields()) {
                        String fieldName = field.getName();
                        Object value = resultSet.getObject(fieldName); //get value from database column

                        if (value != null) {
                            // find seeter and invoke to set the value
                            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                            Method method = propertyDescriptor.getWriteMethod();

                            //to avoid missmatches
                            if (value != null) {
                                Class<?> parameterType = method.getParameterTypes()[0];
                                if (parameterType == int.class && value instanceof Number) {
                                    value = ((Number) value).intValue();//forces integer
                                } else if (parameterType == double.class && value instanceof Number) {
                                    value = ((Number) value).doubleValue();//forces double
                                }
                            }
                            method.invoke(instance, value);
                        }
                    }
                    list.add(instance);
                }
            } catch (InstantiationException | IllegalAccessException | SecurityException |
                     IllegalArgumentException | InvocationTargetException | SQLException |
                     IntrospectionException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return list;

    }

    /**
     * Retrieves all rows from the corresponding database table.
     * * @return A list containing all records found in the table.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return  createObjects(resultSet);
        }catch(SQLException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Dynamically builds an INSERT statement and saves an object as a new row in the database.
     * Automatically skips the first field assuming it is an auto-incrementing ID.
     * * @param t The object entity containing the data to be inserted.
     * @return The same object instance if the insertion was successful.
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(type.getSimpleName()).append(" (");

        Field[] fields = type.getDeclaredFields();

        for(int i=1;i<fields.length;i++) {
            query.append(fields[i].getName());
            if(i<fields.length-1){
                query.append(",");
            }
        }
        query.append(") VALUES (");
        for(int i=1;i<fields.length;i++) {
            query.append("?");
            if(i<fields.length-1){
                query.append(",");
            }
        }
        query.append(")");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

            for(int i=1;i<fields.length;i++) {
                String fieldName = fields[i].getName();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);

                //get the getter method
                Method method = propertyDescriptor.getReadMethod();
                Object value = method.invoke(t);//extract value
                statement.setObject(i,value);
            }
            statement.executeUpdate();
            return t;
        }catch(SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        }finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Dynamically builds an UPDATE statement and modifies an existing database row matching the object's ID.
     * * @param t The object containing the modified values.
     * @return The updated object instance if successful.
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(type.getSimpleName()).append(" SET ");

        Field[] fields = type.getDeclaredFields();

        for (int i = 1; i < fields.length; i++) {
            query.append(fields[i].getName()).append(" = ?");
            if (i < fields.length - 1) query.append(", ");
        }
        query.append(" WHERE ").append(fields[0].getName()).append(" = ?");

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());

            for (int i = 1; i < fields.length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[i].getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                Object value = method.invoke(t);
                statement.setObject(i, value);
            }

            PropertyDescriptor idDescriptor = new PropertyDescriptor(fields[0].getName(), type);
            Method idMethod = idDescriptor.getReadMethod();
            Object idValue = idMethod.invoke(t);
            statement.setObject(fields.length, idValue);

            statement.executeUpdate();
            return t;
        }catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        }finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    /**
     * Deletes a record from the database based on its unique ID.
     * * @param id The unique ID of the record to remove.
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
