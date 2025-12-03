package main.com .gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.dao.BaseDao;
import main.com.gavruseva.webapp.dao.columnNames.UserTableColumnNames;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.User;
import main.com.gavruseva.webapp.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements BaseDao<User> {
  private static final Logger logger = LogManager.getLogger();
  private static UserDao instance;
  private static final String FIND_BY_ID_QUERY = "SELECT id, email, login, password_hash, status, role FROM users WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO users (email, login, password_hash, status, role) VALUES (?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, password_hash = ?, role = ?, status = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, email, login, password_hash, status, role FROM users";
  private static final String LOGIN_QUERY = "SELECT login, password_hash, role FROM users WHERE login = ?";
  private static final String LOGIN_CHECK_QUERY = "SELECT login FROM users WHERE login = ?";

  public static UserDao getInstance() {
    if (instance == null) {
      instance = new UserDao();
    }
    return instance;
  }

  @Override
  public Optional<User> findById(Long id) throws DAOException, ConnectionException {
    User user = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No User with ID = {} has been found", id);
        return Optional.empty();
      }

      user = new User();
      user.setModelId(resultSet.getLong(UserTableColumnNames.ID.getFieldName()));
      user.setEmail(resultSet.getString(UserTableColumnNames.EMAIL.getFieldName()));
      user.setLogin(resultSet.getString(UserTableColumnNames.LOGIN.getFieldName()));
      user.setPasswordHash(resultSet.getBytes(UserTableColumnNames.PASSWORD_HASH.getFieldName()));
      user.setStatus(User.UserStatus.valueOf(resultSet.getString(UserTableColumnNames.STATUS.getFieldName())));
      user.setRole(User.UserRole.valueOf(resultSet.getString(UserTableColumnNames.ROLE.getFieldName())));
      logger.info("User {} has been found", user);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(user);
  }



  @Override
  public int insert(User user) throws DAOException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setString(1, user.getEmail());
      pStmt.setString(2, user.getLogin());
      pStmt.setBytes(3, user.getPasswordHash());
      pStmt.setString(4, user.getStatus().toString());
      pStmt.setString(5, user.getRole().toString());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Insert", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't insert the User", e);
      throw new DAOException("Could not insert the User", e);
    }
    return rowsAffected;
  }

  @Override
  public int update(User user) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(UPDATE_QUERY)) {
      pStmt.setString(1, user.getEmail());
      pStmt.setString(2, user.getLogin());
      pStmt.setBytes(3, user.getPasswordHash());
      pStmt.setString(4, user.getRole().toString());
      pStmt.setString(5, user.getStatus().toString());
      pStmt.setLong(6, user.getModelId());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Update", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the User", e);
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
      logger.error("Couldn't update the User", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public List<User> getAll() throws DAOException, ConnectionException {
    List<User> users;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(GET_ALL_QUERY)) {
      ResultSet resultSet = pStmt.executeQuery();
      users = getUserListByResultSet(resultSet);
      logger.info("{} Users in the table", users.size());
    } catch (SQLException e) {
      logger.error("Couldn't retrieve all Users", e);
      throw new DAOException(e);
    }

    return users;
  }


  private List<User> getUserListByResultSet(ResultSet resultSet) throws SQLException {
    List<User> users = new ArrayList<>();

    while (resultSet.next()) {
      User user = new User();
      user.setModelId(resultSet.getLong(UserTableColumnNames.ID.getFieldName()));
      user.setEmail(resultSet.getString(UserTableColumnNames.EMAIL.getFieldName()));
      user.setLogin(resultSet.getString(UserTableColumnNames.LOGIN.getFieldName()));
      user.setPasswordHash(resultSet.getBytes(UserTableColumnNames.PASSWORD_HASH.getFieldName()));
      user.setRole(User.UserRole.valueOf(resultSet.getString(UserTableColumnNames.ROLE.getFieldName())));
      user.setStatus(User.UserStatus.valueOf(resultSet.getString(UserTableColumnNames.STATUS.getFieldName())));
      users.add(user);
    }

    return users;
  }

  public Optional<User> login(String login, String password) throws DAOException {
    User user = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(LOGIN_QUERY)) {
      pStmt.setString(1, login);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No User with login = {} has been found", login);
        return Optional.empty();
      }

      user = new User();
      user.setLogin(resultSet.getString(UserTableColumnNames.LOGIN.getFieldName()));
      user.setPasswordHash(resultSet.getBytes(UserTableColumnNames.PASSWORD_HASH.getFieldName()));
      user.setRole(User.UserRole.valueOf(resultSet.getString(UserTableColumnNames.ROLE.getFieldName())));
      logger.info("User {} has been found and logged in", user);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(user);
  }

  public String findLogin(String login) throws DAOException {
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(LOGIN_CHECK_QUERY)) {
      pStmt.setString(1, login);

      ResultSet resultSet = pStmt.executeQuery();

      if (resultSet.next()) {
        logger.warn("User with login = {} already exists, please try another one", login);
        return resultSet.getString(UserTableColumnNames.LOGIN.getFieldName());
      }
      return null;
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }
  }
}
