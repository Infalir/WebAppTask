package com.gavruseva.webapp.controller;

import com.gavruseva.webapp.exception.CommandException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.CommandResult;
import com.gavruseva.webapp.command.factory.CommandFactory;
import com.gavruseva.webapp.connection.ConnectionPool;
import com.gavruseva.webapp.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Controller extends HttpServlet {
    private static final String COMMAND = "command";
    private static final String ERROR_MESSAGE = "error_message";
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void destroy() {
      ConnectionPool.getInstance().destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String command = request.getParameter(COMMAND);
      logger.info("Command = {}", command);
      Command action = CommandFactory.create(command);
      CommandResult commandResult;
      try {
        commandResult = action.execute(request, response);
      } catch (CommandException e) {
        logger.error(e.getMessage(), e);
        request.setAttribute(ERROR_MESSAGE, e.getMessage());
        commandResult = new CommandResult(Page.ERROR_PAGE.getPage(), false);
      }
      String page = commandResult.getPage();
      if (commandResult.isRedirect()) {
        response.sendRedirect(page);
      } else {
        forward(request, response, page);
      }
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
      ServletContext servletContext = getServletContext();
      RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(page);
      requestDispatcher.forward(request, response);
    }

}
