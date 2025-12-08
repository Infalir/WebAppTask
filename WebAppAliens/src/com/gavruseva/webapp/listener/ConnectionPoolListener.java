package com.gavruseva.webapp.listener;

import com.gavruseva.webapp.connection.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {
  private static final Logger logger = LogManager.getLogger();
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ConnectionPool.getInstance();
    logger.info("Connection Pool initialized");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ConnectionPool.getInstance().destroy();
    logger.info("Connection Pool destroyed");
  }
}
