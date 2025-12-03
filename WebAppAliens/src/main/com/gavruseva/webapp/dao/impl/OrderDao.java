package main.com.gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.connection.ConnectionPool;
import main.com.gavruseva.webapp.dao.BaseDao;
import main.com.gavruseva.webapp.dao.columnNames.OrderTableColumnNames;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements BaseDao<Order> {
  private static final Logger logger = LogManager.getLogger();
  private static final String FIND_BY_ID_QUERY = "SELECT id, order_id, user_id, ink_id, bodypart, order_status, order_price FROM orders WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO orders (order_id, user_id, ink_id, bodypart, order_status, order_price) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM orders WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE orders SET order_id = ?, user_id = ?, ink_id = ?, bodypart = ?, order_status, order_price = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, order_id, user_id, ink_id, bodypart, order_status, order_price FROM orders";

  @Override
  public Optional<Order> findById(Long id) throws DAOException, ConnectionException {
    Order order = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No Order with ID = {} has been found", id);
        return Optional.empty();
      }

      order = new Order();
      order.setModelId(resultSet.getLong(OrderTableColumnNames.ID.getFieldName()));
      order.setImageId(resultSet.getLong(OrderTableColumnNames.IMAGE_ID.getFieldName()));
      order.setUserId(resultSet.getLong(OrderTableColumnNames.USER_ID.getFieldName()));
      order.setInkId(resultSet.getLong(OrderTableColumnNames.INK_ID.getFieldName()));
      order.setBodyPart(resultSet.getString(OrderTableColumnNames.BODYPART.getFieldName()));
      order.setOrderStatus(Order.OrderStatus.valueOf(resultSet.getString(OrderTableColumnNames.ORDER_STATUS.getFieldName())));
      order.setOrderPrice(resultSet.getDouble(OrderTableColumnNames.ORDER_PRICE.getFieldName()));
      logger.info("Order {} has been found", order);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(order);
  }



  @Override
  public int insert(Order order) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setLong(1, order.getImageId());
      pStmt.setLong(2, order.getUserId());
      pStmt.setLong(3, order.getInkId());
      pStmt.setString(4, order.getBodyPart());
      pStmt.setString(5, order.getOrderStatus().toString());
      pStmt.setDouble(6, order.getOrderPrice());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Insert", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't insert the Order", e);
      throw new DAOException("Could not insert the Order", e);
    }
    return rowsAffected;
  }

  @Override
  public int update(Order order) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(UPDATE_QUERY)) {
      pStmt.setLong(1, order.getImageId());
      pStmt.setLong(2, order.getUserId());
      pStmt.setLong(3, order.getInkId());
      pStmt.setString(4, order.getBodyPart());
      pStmt.setString(5, order.getOrderStatus().toString());
      pStmt.setDouble(6, order.getOrderPrice());
      pStmt.setLong(7, order.getModelId());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Update", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Order", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public int delete(Long id) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(DELETE_QUERY)) {
      pStmt.setLong(1, id);
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Delete", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Order", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public List<Order> getAll() throws DAOException, ConnectionException {
    List<Order> orders;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(GET_ALL_QUERY)) {
      ResultSet resultSet = pStmt.executeQuery();
      orders = getOrderListByResultSet(resultSet);
      logger.info("{} Orders in the table", orders.size());
    } catch (SQLException e) {
      logger.error("Couldn't retrieve all Orders", e);
      throw new DAOException(e);
    }

    return orders;
  }


  private List<Order> getOrderListByResultSet(ResultSet resultSet) throws SQLException {
    List<Order> orders = new ArrayList<>();

    while (resultSet.next()) {
      Order order = new Order();
      order.setModelId(resultSet.getLong(OrderTableColumnNames.ID.getFieldName()));
      order.setImageId(resultSet.getLong(OrderTableColumnNames.IMAGE_ID.getFieldName()));
      order.setUserId(resultSet.getLong(OrderTableColumnNames.USER_ID.getFieldName()));
      order.setInkId(resultSet.getLong(OrderTableColumnNames.INK_ID.getFieldName()));
      order.setBodyPart(resultSet.getString(OrderTableColumnNames.BODYPART.getFieldName()));
      order.setOrderPrice(resultSet.getDouble(OrderTableColumnNames.ORDER_PRICE.getFieldName()));
      order.setOrderStatus(Order.OrderStatus.valueOf(resultSet.getString(OrderTableColumnNames.ORDER_STATUS.getFieldName())));
      orders.add(order);
    }

    return orders;
  }
}
