package main.com .gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.connection.ConnectionManager;
import main.com.gavruseva.webapp.dao.PreparedStatementDao;
import main.com.gavruseva.webapp.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements PreparedStatementDao<User> {

  @Override
  public User getById(Long id) {
    User user = null;

    final String query = "SELECT * FROM users WHERE id = ? LIMIT 1";

    try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (resultSet.next()) {
        user = new User();
        user.setModelId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setPasswordHash(resultSet.getBytes("password_hash"));
        user.setStatus(User.UserStatus.valueOf(resultSet.getString("status")));
        user.setRole(User.UserRole.valueOf(resultSet.getString("role")));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return user;
  }



  @Override
  public void insert(User user) {
    final String query = "INSERT INTO users (email, login, password_hash, status, role) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
      pStmt.setString(1, user.getEmail());
      pStmt.setString(2, user.getLogin());
      pStmt.setBytes(3, user.getPasswordHash());
      pStmt.setString(4, user.getStatus().toString());
      pStmt.setString(5, user.getRole().toString());
      pStmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(User user) {
    final String query = "UPDATE users " +
            "SET email = ?, login = ?, password_hash = ?, role = ?, status = ? " +
            "WHERE id = ?";

    try (PreparedStatement pStmt =
                 ConnectionManager.getConnection().prepareStatement(query)) {
      pStmt.setString(1, user.getEmail());
      pStmt.setString(2, user.getLogin());
      pStmt.setBytes(3, user.getPasswordHash());
      pStmt.setString(4, user.getRole().toString());
      pStmt.setString(5, user.getStatus().toString());
      pStmt.setLong(6, user.getModelId());
      pStmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Long id) {
    final String query = "DELETE FROM users WHERE id = ?";

    try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
      pStmt.setLong(1, id);
      pStmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<User> getAll() {
    List<User> users;

    final String query = "SELECT * FROM users";

    try (PreparedStatement pStmt = ConnectionManager.getConnection().prepareStatement(query)) {
      ResultSet resultSet = pStmt.executeQuery();
      users = getUserListByResultSet(resultSet);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return users;
  }


  private List<User> getUserListByResultSet(ResultSet resultSet) throws
          SQLException {
    List<User> users = new ArrayList<>();

    while (resultSet.next()) {
      User user = new User();
      user.setModelId(resultSet.getLong("id"));
      user.setEmail(resultSet.getString("email"));
      user.setLogin(resultSet.getString("login"));
      user.setPasswordHash(resultSet.getBytes("password_hash"));
      user.setRole(User.UserRole.valueOf(resultSet.getString("role")));
      user.setStatus(User.UserStatus.valueOf(resultSet.getString("status")));
      users.add(user);
    }

    return users;
  }
}
