package main.com.gavruseva.webapp.connection;

import main.com.gavruseva.webapp.exception.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
  private static final Logger logger = LogManager.getLogger();
  private static final String PROPERTY_PATH = "db";
  private static final int INITIAL_CAPACITY = 5;
  private ArrayBlockingQueue<Connection> freeConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
  private ArrayBlockingQueue<Connection> takenConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
  private static ReentrantLock lock = new ReentrantLock();
  private volatile static ConnectionPool instance;

  public static ConnectionPool getInstance() throws ConnectionException {
    if (instance == null) {
      try {
        lock.lock();
        if (instance == null) {
          instance = new ConnectionPool();
        }
      } catch (Exception e) {
        logger.error("Can not get Instance of a connection pool", e);
        throw new ConnectionException("Can not get Instance of a connection pool", e);
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }

  private ConnectionPool() throws SQLException, ConnectionException {
    try {
      lock.lock();
      if (instance != null) {
        throw new UnsupportedOperationException();
      } else {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        init();
      }
    } catch (SQLException e) {
      logger.error("Can not initialize a connection pool", e);
      throw new ConnectionException("Can not initialize a connection pool", e);
    }finally {
      lock.unlock();
    }
  }

  private void init() throws ConnectionException {
    ResourceBundle resource = ResourceBundle.getBundle(PROPERTY_PATH, Locale.getDefault());
    if (resource == null) {
      logger.error("Error while reading properties");
    } else {
      String connectionURL = resource.getString(ConnectionConstants.URL.getPropertyName());
      String initialCapacityString = resource.getString(ConnectionConstants.POOLSIZE.getPropertyName());
      String user = resource.getString(ConnectionConstants.USER.getPropertyName());
      String pass = resource.getString(ConnectionConstants.PASSWORD.getPropertyName());

      Integer initialCapacity = Integer.valueOf(initialCapacityString);
      for (int i = 0; i < initialCapacity; i++) {
        try {
          Connection connection = DriverManager.getConnection(connectionURL, user, pass);
          freeConnections.add(connection);
        } catch (SQLException e) {
          logger.error("Pool can not be initialized with given parameters", e);
          throw new ConnectionException("Pool can not be initialized with given parameters", e);
        }
      }
    }
  }

  public Connection getConnection() {
    try {
      Connection connection = freeConnections.take();
      takenConnections.offer(connection);
      logger.info("Connection was taken, the are free connection " + freeConnections.size());
      return connection;
    } catch (InterruptedException e) {
      throw new RuntimeException("Can not get database", e);
    }
  }

  public void releaseConnection(Connection connection) {
    takenConnections.remove(connection);
    freeConnections.offer(connection);
    logger.info("Connection was released, the are free connection " + freeConnections.size());
  }

  public void destroy() {
    for (int i = 0; i < freeConnections.size(); i++) {
      try {
        Connection connection = (Connection) freeConnections.take();
        connection.close();
      } catch (InterruptedException e) {
        logger.error("Connection close exception", e);
      } catch (SQLException e) {
        logger.error("database is not closed", e);
        throw new RuntimeException("database is not closed", e);
      }
    }
    try {
      Enumeration<Driver> drivers = DriverManager.getDrivers();
      while (drivers.hasMoreElements()) {
        java.sql.Driver driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);
      }
    } catch (SQLException e) {
      logger.error("Drivers were not deregistrated", e);
    }
  }
}
