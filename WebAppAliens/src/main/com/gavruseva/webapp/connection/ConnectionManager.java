package main.com.gavruseva.webapp.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

class ConnectionManager {
  private static final Logger logger = LogManager.getLogger();
  private static final String PROPERTY_PATH = "db";

  public static void init(ArrayBlockingQueue<Connection> freeConnections) {
    ResourceBundle resource = ResourceBundle.getBundle(PROPERTY_PATH, Locale.getDefault());
    if (resource == null) {
      logger.error("Error while reading properties");
    } else {
      String connectionURL = resource.getString(ConnectionParameters.URL.getPropertyName());
      String initialCapacityString = resource.getString(ConnectionParameters.POOLSIZE.getPropertyName());
      String user = resource.getString(ConnectionParameters.USER.getPropertyName());
      String pass = resource.getString(ConnectionParameters.PASSWORD.getPropertyName());

      Integer initialCapacity = Integer.valueOf(initialCapacityString);
      for (int i = 0; i < initialCapacity; i++) {
        try {
          Connection connection = DriverManager.getConnection(connectionURL, user, pass);
          freeConnections.add(connection);
        } catch (SQLException e) {
          logger.fatal("Pool can not be initialized with given parameters", e);
          throw new ExceptionInInitializerError(e);
        }
      }
    }
  }
}
