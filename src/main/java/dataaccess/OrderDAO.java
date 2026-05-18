package dataaccess;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import model.ProductOrder;

/**
 * Data Access Object specific to the ProductOrder entity.
 * Inherits all generic CRUD operations (find, insert, update, delete) from AbstractDAO.
 * * @author Yasmina
 * @version 1.0
 */
public class OrderDAO extends AbstractDAO<ProductOrder> {
}
