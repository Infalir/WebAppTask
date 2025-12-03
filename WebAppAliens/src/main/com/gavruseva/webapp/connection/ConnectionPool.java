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
  private static ConnectionPool instance;

  public static ConnectionPool getInstance() {
    if (instance == null) {
      try {
        lock.lock();
        if (instance == null) {
          instance = new ConnectionPool();
        }
      } catch (Exception e) {
        logger.fatal("Can not get Instance of a connection pool", e);
        throw new ExceptionInInitializerError(e);
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }

  private ConnectionPool() throws SQLException, ConnectionException {
    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
    ConnectionManager.init(freeConnections);
  }

  public Connection getConnection() {
    try {
      Connection connection = freeConnections.take();
      takenConnections.put(connection);
      logger.info("Connection was taken. Connections available {} ", freeConnections.size());
      return connection;
    } catch (InterruptedException e) {
      logger.error("Could not get a connection to the database", e);
      Thread.currentThread().interrupt();
      return null;
    }
  }

  public void releaseConnection(Connection connection) {
    try {
      takenConnections.remove(connection);
      freeConnections.put(connection);
      logger.info("Connection was released. Connections available {} ", freeConnections.size());
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void destroy() {
    for (int i = 0; i < freeConnections.size(); i++) {
      try {
        Connection connection = (Connection) freeConnections.take();
        connection.close();
      } catch (InterruptedException | SQLException e) {
        logger.error("Couldn't close connection while destroying the pool", e);
        Thread.currentThread().interrupt();
      }
    }
    try {
      Enumeration<Driver> drivers = DriverManager.getDrivers();
      while (drivers.hasMoreElements()) {
        java.sql.Driver driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);
      }
    } catch (SQLException e) {
      logger.error("Drivers could not be unregistered", e);
    }
  }
}
