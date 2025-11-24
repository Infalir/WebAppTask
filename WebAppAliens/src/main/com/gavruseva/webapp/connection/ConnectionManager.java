package main.com.gavruseva.webapp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionManager {
  private static Connection connection = null;

  public static Connection getConnection() {
    if (connection == null) {
      try {
        Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
        closeConnection();
      }

      final String user = "postgres";
      final String password = "postgres";
      final String url = "jdbc:postgresql://localhost:5432/webapp";

      try {
        connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
        closeConnection();
      }
    }

    return connection;
  }

  public static void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {

      } finally {
        connection = null;
      }
    }
  }
}
