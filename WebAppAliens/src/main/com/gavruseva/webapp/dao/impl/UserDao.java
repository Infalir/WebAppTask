package main.com .gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.dao.BasicDao;
import main.com.gavruseva.webapp.dao.constant.UserTableConstants;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.User;
import main.com.gavruseva.webapp.connection.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements BasicDao<User> {

  private static final String FIND_BY_ID_QUERY = "SELECT id, email, login, password_hash, status, role FROM users WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO users (email, login, password_hash, status, role) VALUES (?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, password_hash = ?, role = ?, status = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, email, login, password_hash, status, role FROM users";

  @Override
  public Optional<User> findById(Long id) throws DAOException, ConnectionException {
    User user = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        return Optional.empty();
      }

      user = new User();
      user.setModelId(resultSet.getLong(UserTableConstants.ID.getFieldName()));
      user.setEmail(resultSet.getString(UserTableConstants.EMAIL.getFieldName()));
      user.setLogin(resultSet.getString(UserTableConstants.LOGIN.getFieldName()));
      user.setPasswordHash(resultSet.getBytes(UserTableConstants.PASSWORD_HASH.getFieldName()));
      user.setStatus(User.UserStatus.valueOf(resultSet.getString(UserTableConstants.STATUS.getFieldName())));
      user.setRole(User.UserRole.valueOf(resultSet.getString(UserTableConstants.ROLE.getFieldName())));
    } catch (SQLException e) {
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(user);
  }



  @Override
  public int insert(User user) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setString(1, user.getEmail());
      pStmt.setString(2, user.getLogin());
      pStmt.setBytes(3, user.getPasswordHash());
      pStmt.setString(4, user.getStatus().toString());
      pStmt.setString(5, user.getRole().toString());
      rowsAffected = pStmt.executeUpdate();
    } catch (SQLException e) {
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
    } catch (SQLException e) {
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
    } catch (SQLException e) {
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
    } catch (SQLException e) {
      throw new DAOException(e);
    }

    return users;
  }


  private List<User> getUserListByResultSet(ResultSet resultSet) throws SQLException {
    List<User> users = new ArrayList<>();

    while (resultSet.next()) {
      User user = new User();
      user.setModelId(resultSet.getLong(UserTableConstants.ID.getFieldName()));
      user.setEmail(resultSet.getString(UserTableConstants.EMAIL.getFieldName()));
      user.setLogin(resultSet.getString(UserTableConstants.LOGIN.getFieldName()));
      user.setPasswordHash(resultSet.getBytes(UserTableConstants.PASSWORD_HASH.getFieldName()));
      user.setRole(User.UserRole.valueOf(resultSet.getString(UserTableConstants.ROLE.getFieldName())));
      user.setStatus(User.UserStatus.valueOf(resultSet.getString(UserTableConstants.STATUS.getFieldName())));
      users.add(user);
    }

    return users;
  }
}
